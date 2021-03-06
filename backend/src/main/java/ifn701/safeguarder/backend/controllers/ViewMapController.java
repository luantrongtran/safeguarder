package ifn701.safeguarder.backend.controllers;

import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.Constants;
import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.entities.Accident;

/**
 * Created by G1 on 23/10/2015.
 */
public class ViewMapController extends HttpServlet {
    public static final String VIEW_TEMPLATE_PATH = "/WEB-INF/jsp/viewmap.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AccidentDao accidentDao = new AccidentDao();
        List<Accident> lstAccidents = accidentDao.getAllAccidents();

        req.setAttribute("lstAccidents", lstAccidents);

        req.getRequestDispatcher(VIEW_TEMPLATE_PATH).forward(req, resp);


    }
}
