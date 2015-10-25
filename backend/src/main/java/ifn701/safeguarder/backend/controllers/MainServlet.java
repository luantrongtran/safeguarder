package ifn701.safeguarder.backend.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ifn701.safeguarder.backend.Constants;

/**
 * Created by lua on 25/10/2015.
 */
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(true);
        if(session == null || session.getAttribute(Constants.session_user_id) == null) {
            resp.sendRedirect("/controlpanel/");
        } else {
            if(session.getAttribute(Constants.session_user_id).toString().isEmpty()) {
                resp.sendRedirect("/controlpanel/");
                return;
            }

            String jspPage = Constants.jsp_prefix + "index.jsp";
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(jspPage);
            requestDispatcher.forward(req, resp);
        }
    }
}
