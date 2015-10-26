package ifn701.safeguarder.backend.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.Constants;
import ifn701.safeguarder.backend.dao.AccidentDao;

/**
 * Created by mutanthybrid on 26/10/2015.
 */
public class DeleteAccidentController extends HttpServlet{
    AccidentDao accidentDao = new AccidentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Boolean deleted = false;
        String jspPage = Constants.jsp_prefix + "viewmap.jsp";

        String accId = req.getParameter("accidentId");
        deleted = accidentDao.deleteAccidentById(Integer.parseInt(accId));

        if(deleted) {
//            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPage);
//            dispatcher.forward(req, resp);
            resp.sendRedirect("/controlpanel/viewmap");
        }
    }
}