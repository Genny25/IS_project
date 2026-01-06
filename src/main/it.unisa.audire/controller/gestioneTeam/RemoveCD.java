package controller.gestioneTeam;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dao.*;
import model.dto.*;
import utils.NotificationUtil;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/pm/remove-team-member")
public class RemoveCD extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;

        if (user == null || user.getRole() != UserDTO.Role.ProductionManager) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String prodIdStr = req.getParameter("productionId");
        String userIdStr = req.getParameter("userId");

        if (prodIdStr == null || userIdStr == null) {
            NotificationUtil.sendNotification(req, "Dati mancanti per la rimozione.", "error");
            resp.sendRedirect(req.getContextPath() + "/pm/productions");
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("ds");
        TeamDAO teamDAO = new TeamDAO(ds);
        ProductionDAO prodDAO = new ProductionDAO(ds);
        ProductionManagerDAO pmDAO = new ProductionManagerDAO(ds);
        CastingDirectorDAO cdDAO = new CastingDirectorDAO(ds);

        try {
            int prodID = Integer.parseInt(prodIdStr);
            int userId = Integer.parseInt(userIdStr);

            ProductionDTO production = prodDAO.getByID(prodID);
            ProductionManagerDTO pmDTO = pmDAO.getByUserID(user.getUserID());

            if (production == null || pmDTO == null || production.getPmID() != pmDTO.getPmID()) {
                NotificationUtil.sendNotification(req, "Non hai i permessi per modificare questo team.", "error");
                resp.sendRedirect(req.getContextPath() + "/pm/productions");
                return;
            }

            CastingDirectorDTO cdDTO = cdDAO.getByUserID(userId);

            if (cdDTO != null) {
                boolean removed = teamDAO.delete(prodID, cdDTO.getCdID());

                if (removed) {
                    NotificationUtil.sendNotification(req, "Membro rimosso dal team con successo.", "success");
                } else {
                    NotificationUtil.sendNotification(req, "Impossibile rimuovere: il membro non era nel team.", "warning");
                }
            } else {
                NotificationUtil.sendNotification(req, "Errore: Profilo Casting Director non trovato.", "error");
            }

            resp.sendRedirect(req.getContextPath() + "/pm/team?id=" + prodID);

        } catch (NumberFormatException e) {
            NotificationUtil.sendNotification(req, "ID non validi.", "error");
            resp.sendRedirect(req.getContextPath() + "/pm/productions");
        } catch (SQLException e) {
            e.printStackTrace();
            NotificationUtil.sendNotification(req, "Errore Database durante la rimozione.", "error");
            resp.sendRedirect(req.getContextPath() + "/pm/team?id=" + prodIdStr);
        }
    }
}