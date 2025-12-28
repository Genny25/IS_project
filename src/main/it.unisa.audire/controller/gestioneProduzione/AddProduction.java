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
import utils.NotificationUtil;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pm/add-production")
public class AddProduction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null || user.getRole() != UserDTO.Role.ProductionManager) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/pm/add-production.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null || user.getRole() != UserDTO.Role.ProductionManager) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String title = req.getParameter("title");
        String typeStr = req.getParameter("type");

        List<String> errors = new ArrayList<>();

        if (title == null || title.trim().isEmpty()) {
            errors.add("Il titolo della produzione è obbligatorio.");
        }
        if (typeStr == null || typeStr.trim().isEmpty()) {
            errors.add("Seleziona una tipologia di produzione.");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/pm/add-production.jsp").forward(req, resp);
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("ds");
        ProductionDAO productionDAO = new ProductionDAO(ds);

        ProductionManagerDAO pmDAO = new ProductionManagerDAO(ds);

        try {
            ProductionManagerDTO pmDTO = pmDAO.getByUserID(user.getUserID());

            if (pmDTO == null) {
                throw new ServletException("Errore critico: Profilo PM non trovato.");
            }

            ProductionDTO production = new ProductionDTO();
            production.setTitle(title.trim());
            production.setCreationDate(LocalDateTime.now());
            production.setPmID(pmDTO.getPmID());

            try {
                production.setType(ProductionDTO.Type.valueOf(typeStr));
            } catch (IllegalArgumentException e) {
                errors.add("Tipologia non valida.");
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/WEB-INF/views/pm/add-production.jsp").forward(req, resp);
                return;
            }

            productionDAO.save(production);

            NotificationUtil.sendNotification(req, "Produzione creata con successo!", "success");
            resp.sendRedirect(req.getContextPath() + "/pm/productions");

        } catch (SQLException e) {
            e.printStackTrace();
            errors.add("Errore del database: " + e.getMessage());
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/pm/add-production.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            errors.add("Errore imprevisto durante la creazione.");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/pm/add-production.jsp").forward(req, resp);
        }
    }
}