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
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        if(session != null) {

            if (session.getAttribute(Constants.session_user_id) != null) {
                String userId = session.getAttribute(Constants.session_user_id).toString();
                if (!userId.isEmpty()) {
//                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/");
//                    dispatcher.forward(req, resp);
                    resp.sendRedirect("/");
                    return;
                }
            }
        }
        String jspPage = Constants.jsp_prefix + "login.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPage);
        dispatcher.forward(req, resp);
    }
}
