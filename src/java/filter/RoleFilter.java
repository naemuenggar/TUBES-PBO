
package filter;

import model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Role-Based Authorization Filter
 * Restricts access to admin-only features for regular users
 */
public class RoleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // Define admin-only servlets/pages
        boolean isAdminResource = uri.contains("UserServlet") ||
                uri.contains("KategoriServlet") ||
                uri.contains("user-list.jsp") ||
                uri.contains("user-form.jsp");

        // Skip role check for login/logout/register and non-admin resources
        boolean isLoginLogout = uri.endsWith("/LoginServlet") || uri.endsWith("/LogoutServlet")
                || uri.endsWith("/login.jsp");
        boolean isRegisterPage = uri.endsWith("/RegisterServlet") || uri.endsWith("/register.jsp");
        boolean isStaticResource = uri.contains("/css/") || uri.contains("/js/") ||
                uri.contains("/images/") || uri.endsWith(".css") ||
                uri.endsWith(".js") || uri.endsWith(".png") ||
                uri.endsWith(".jpg") || uri.endsWith(".ico") ||
                uri.endsWith(".gif") || uri.endsWith(".svg");

        if (isLoginLogout || isRegisterPage || isStaticResource || !isAdminResource) {
            // Not an admin resource or login/register/static, allow access
            chain.doFilter(request, response);
            return;
        }

        // Check user role for admin resources
        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");

            if (user != null && "admin".equals(user.getRole())) {
                // User is admin, allow access
                chain.doFilter(request, response);
            } else {
                // User is not admin, deny access
                res.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Access Denied: You don't have permission to access this resource. Admin privileges required.");
            }
        } else {
            // No session, should be caught by AuthenticationFilter, but just in case
            res.sendRedirect(req.getContextPath() + "/LoginServlet");
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
