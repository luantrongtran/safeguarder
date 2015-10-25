package ifn701.safeguarder.backend.controllers;

import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.entities.Accident;

/**
 * Created by lua on 26/10/2015.
 */
public class GetAccidentsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        AccidentDao accidentDao = new AccidentDao();
        List<Accident> lstAccidents = accidentDao.getAllAccidents();
        resp.getWriter().write(new Gson().toJson(lstAccidents));
    }
}
