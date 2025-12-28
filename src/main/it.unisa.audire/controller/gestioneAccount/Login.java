package controller.gestioneAccount;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dao.UserDAO;
import model.dto.UserDTO;
import utils.NotificationUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/login")
public class Login extends HttpServlet {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        List<String> errors = new ArrayList<>();
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/login.jsp");


        if (email == null || email.trim().isEmpty()) {
            errors.add("Il campo email non può essere vuoto.");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Il formato dell'email non è valido.");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("Il campo password non può essere vuoto.");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
            return;
        }

        email = email.trim();

        DataSource ds = (DataSource) getServletContext().getAttribute("ds");
        UserDAO userDAO = new UserDAO(ds);
        UserDTO user = null;

        try {
            user = userDAO.getByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            errors.add("Errore inaspettato del server.");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
            return;
        }

        boolean loginSuccess = false;

        if (user != null) {
            if (userDAO.verifyPassword(password, user.getPasswordHash())) {
                loginSuccess = true;
            }
        }

        if (!loginSuccess) {
            errors.add("Credenziali non valide.");
            req.setAttribute("errors", errors);
            dispatcher.forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();

        session.setAttribute("user", user);

        NotificationUtil.sendNotification(req, "Bentornat*, " + user.getFirstName() + "!", "success");

        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");

        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            session.removeAttribute("redirectAfterLogin");
            resp.sendRedirect(redirectUrl);
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(req, resp);
    }
}