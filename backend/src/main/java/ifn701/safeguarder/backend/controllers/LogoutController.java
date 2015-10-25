package ifn701.safeguarder.backend.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ifn701.safeguarder.backend.Constants;

/**
 * Created by lua on 25/10/2015.
 */
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(session != null) {
            session.removeAttribute(Constants.session_user_id);
        }

        resp.sendRedirect("/controlpanel/login");
    }
}
