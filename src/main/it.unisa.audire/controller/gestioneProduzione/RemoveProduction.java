package controller.gestioneProduzione;

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

@WebServlet("/pm/delete-production")
public class RemoveProduction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null || user.getRole() != UserDTO.Role.ProductionManager) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            NotificationUtil.sendNotification(req, "ID Produzione non valido.", "error");
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
                NotificationUtil.sendNotification(req, "Non hai i permessi per eliminare questa produzione.", "error");
                resp.sendRedirect(req.getContextPath() + "/pm/productions");
                return;
            }

            boolean deleted = prodDAO.delete(prodID);

            if (deleted) {
                NotificationUtil.sendNotification(req, "Produzione eliminata con successo.", "success");
            } else {
                NotificationUtil.sendNotification(req, "Impossibile eliminare la produzione.", "error");
            }

        } catch (NumberFormatException e) {
            NotificationUtil.sendNotification(req, "Formato ID non valido.", "error");
        } catch (SQLException e) {
            e.printStackTrace();
            NotificationUtil.sendNotification(req, "Errore Database: Impossibile eliminare (controlla dati collegati).", "error");
        } catch (Exception e) {
            e.printStackTrace();
            NotificationUtil.sendNotification(req, "Errore imprevisto del server.", "error");
        }

        resp.sendRedirect(req.getContextPath() + "/pm/productions");
    }
}
