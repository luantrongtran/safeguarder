package ifn701.safeguarder.backend.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.Constants;

/**
 * Created by lua on 22/10/2015.
 */
public class GoToAutoLoadEventsPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jspPage = Constants.jsp_prefix + "auto_load_events.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPage);

        String temp = req.getParameter("name");
        req.setAttribute("msg", "Hi " + temp);
        dispatcher.forward(req, resp);
    }
}
