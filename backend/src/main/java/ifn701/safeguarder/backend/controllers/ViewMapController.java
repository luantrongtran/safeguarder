package ifn701.safeguarder.backend.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.Constants;

/**
 * Created by G1 on 23/10/2015.
 */
public class ViewMapController extends HttpServlet {
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/viewmap.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String jspPage = Constants.jsp_prefix + "/jsp/viewmap.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPage);

//        String temp = req.getParameter("name");
//        req.setAttribute("msg", "Hi " + temp);
//        dispatcher.forward(req, resp);
        req.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(req, resp);
    }
}
