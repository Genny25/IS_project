package controller.gestioneProduzione;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dao.ProductionDAO;
import model.dao.ProductionManagerDAO;
import model.dto.ProductionDTO;
import model.dto.ProductionManagerDTO;
import model.dto.UserDTO;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@WebServlet("/pm/productions")
public class GetProductions extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null || user.getRole() != UserDTO.Role.ProductionManager) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("ds");
        ProductionDAO prodDAO = new ProductionDAO(ds);
        ProductionManagerDAO pmDAO = new ProductionManagerDAO(ds);

        try {
            ProductionManagerDTO pmDTO = pmDAO.getByUserID(user.getUserID());

            if (pmDTO == null) {
                req.setAttribute("error", "Errore: Profilo PM non trovato.");
                req.setAttribute("productions", new ArrayList<ProductionDTO>());
            } else {
                Collection<ProductionDTO> productions = prodDAO.getByPmID(pmDTO.getPmID());
                req.setAttribute("productions", productions);
            }

            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/pm/view-productions.jsp");
            dispatcher.forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Errore Database: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/pm/view-productions.jsp").forward(req, resp);
        }
    }
}
