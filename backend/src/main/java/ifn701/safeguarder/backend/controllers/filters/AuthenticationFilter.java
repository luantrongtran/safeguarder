package ifn701.safeguarder.backend.controllers.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ifn701.safeguarder.backend.Constants;

/**
 * Reference: http://www.journaldev.com/1933/java-servlet-filter-example-tutorial
 * Created by lua on 25/10/2015.
 */
public class AuthenticationFilter implements Filter {
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        this.context.log("Requested Resource::" + uri);

        HttpSession session = req.getSession(false);

        if(uri.endsWith("/controlpanel/authenticate")) {
            //if authenticate account
            chain.doFilter(request, response);
        } else if(uri.endsWith("/controlpanel/login")) {
            //if go to login page
            chain.doFilter(request, response);
        }else if(isLoggedIn(session)== false) {
            //if the user has not been logged in
            String loginPage = "/controlpanel/login";
            res.sendRedirect(loginPage);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * Check if the current user is logged in or not
     */
    public boolean isLoggedIn(HttpSession session) {
        if(session == null) {
            return false;
        }

        if(session.getAttribute(Constants.session_user_id) == null) {
            return false;
        }

        return true;
    }
}
