
package filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Authentication Filter - Ensures users are logged in before accessing
 * protected pages
 * Redirects unauthenticated users to login page
 */
public class AuthenticationFilter implements Filter {

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
        String contextPath = req.getContextPath();

        // Only allow login, register pages and static resources without authentication
        boolean isLoginPage = uri.endsWith("/LoginServlet") || uri.endsWith("/login.jsp");
        boolean isRegisterPage = uri.endsWith("/RegisterServlet") || uri.endsWith("/register.jsp");
        boolean isStaticResource = uri.contains("/css/") || uri.contains("/js/") ||
                uri.contains("/images/") || uri.endsWith(".css") ||
                uri.endsWith(".js") || uri.endsWith(".png") ||
                uri.endsWith(".jpg") || uri.endsWith(".ico") ||
                uri.endsWith(".gif") || uri.endsWith(".svg");

        if (isLoginPage || isRegisterPage || isStaticResource) {
            // Allow access to login, register pages and static resources
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (isLoggedIn) {
            // User is authenticated, allow access
            chain.doFilter(request, response);
        } else {
            // User not authenticated, redirect to login
            res.sendRedirect(contextPath + "/LoginServlet");
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
