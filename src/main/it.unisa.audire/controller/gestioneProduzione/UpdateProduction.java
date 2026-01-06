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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pm/edit-production")
public class UpdateProduction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        if (user == null || user.getRole() != UserDTO.Role.ProductionManager) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/pm/productions");
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("ds");
        ProductionDAO prodDAO = new ProductionDAO(ds);
        ProductionManagerDAO pmDAO = new ProductionManagerDAO(ds);

        try {
            int prodID = Integer.parseInt(idStr);
            ProductionDTO production = prodDAO.getByID(prodID);
            ProductionManagerDTO currentPm = pmDAO.getByUserID(user.getUserID());

            if (production == null) {
                NotificationUtil.sendNotification(req, "Produzione non trovata.", "error");
                resp.sendRedirect(req.getContextPath() + "/pm/productions");
                return;
            }

            if (currentPm == null || production.getPmID() != currentPm.getPmID()) {
                NotificationUtil.sendNotification(req, "Non hai i permessi per modificare questa produzione.", "error");
                resp.sendRedirect(req.getContextPath() + "/pm/productions");
                return;
            }

            req.setAttribute("production", production);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/pm/edit-production.jsp");
            dispatcher.forward(req, resp);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/pm/productions");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        if (user == null || user.getRole() != UserDTO.Role.ProductionManager) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String typeStr = req.getParameter("type");

        List<String> errors = new ArrayList<>();
        if (title == null || title.trim().isEmpty()) errors.add("Il titolo è obbligatorio.");

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            doGet(req, resp);
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("ds");
        ProductionDAO prodDAO = new ProductionDAO(ds);
        ProductionManagerDAO pmDAO = new ProductionManagerDAO(ds);

        try {
            int prodID = Integer.parseInt(idStr);

            ProductionDTO production = prodDAO.getByID(prodID);
            ProductionManagerDTO currentPm = pmDAO.getByUserID(user.getUserID());

            if (production == null || currentPm == null || production.getPmID() != currentPm.getPmID()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            production.setTitle(title.trim());
            try {
                production.setType(ProductionDTO.Type.valueOf(typeStr));
            } catch (IllegalArgumentException e) {
                errors.add("Tipologia non valida.");
                req.setAttribute("errors", errors);
                req.setAttribute("production", production);
                req.getRequestDispatcher("/WEB-INF/views/pm/edit-production.jsp").forward(req, resp);
                return;
            }

            prodDAO.save(production);

            NotificationUtil.sendNotification(req, "Produzione aggiornata con successo!", "success");

            resp.sendRedirect(req.getContextPath() + "/pm/productions");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("error", "Errore durante il salvataggio.");
            doGet(req, resp);
        }
    }
}