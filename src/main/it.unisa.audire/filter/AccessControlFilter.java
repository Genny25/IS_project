package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.UserDTO;

import java.io.IOException;

@WebFilter(filterName = "AccessControlFilter", urlPatterns = "/*")
public class AccessControlFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        HttpSession session = request.getSession(false);
        UserDTO user = (session != null) ? (UserDTO) session.getAttribute("user") : null;
        boolean isLoggedIn = (user != null);


        if (path.contains("/cd/")) {
            if (!isLoggedIn) {
                redirectToLogin(request, response);
                return;
            }
            if (user.getRole() != UserDTO.Role.CastingDirector) {
                handleUnauthorized(response);
                return;
            }
        }

        else if (path.contains("/pm/")) {
            if (!isLoggedIn) {
                redirectToLogin(request, response);
                return;
            }
            if (user.getRole() != UserDTO.Role.ProductionManager) {
                handleUnauthorized(response);
                return;
            }
        }

        else if (path.contains("/performer/")) {
            if (!isLoggedIn) {
                redirectToLogin(request, response);
                return;
            }
            if (user.getRole() != UserDTO.Role.Performer) {
                handleUnauthorized(response);
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.getSession().setAttribute("redirectAfterLogin", request.getRequestURI());
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void handleUnauthorized(HttpServletResponse response) throws IOException {
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso Negato");
        response.sendRedirect(response.encodeRedirectURL("/"));
    }
}