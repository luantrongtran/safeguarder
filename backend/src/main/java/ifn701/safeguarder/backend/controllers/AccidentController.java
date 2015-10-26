package ifn701.safeguarder.backend.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ifn701.safeguarder.backend.Constants;
import ifn701.safeguarder.backend.dao.AccidentDao;
import ifn701.safeguarder.backend.entities.Accident;


/**
 * Created by mutanthybrid on 25/10/2015.
 */
public class AccidentController extends HttpServlet{
    AccidentDao accidentDao = new AccidentDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Accident acc;

        String jspPage = Constants.jsp_prefix + "accident.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPage);

        String accId = req.getParameter("accidentId");
        acc = accidentDao.findById(Integer.parseInt(accId));

        if(acc == null) {
            return;
        }

        //getTime
        long time = acc.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date(time);
        String strTime = sdf.format(date);

        //getObservation_Level
        if (acc.getObservation_level() == 1) {
            req.setAttribute("accObsLvl", "Low");
        }
        if (acc.getObservation_level() == 2) {
            req.setAttribute("accObsLvl", "Medium");
        }
        if (acc.getObservation_level() == 3) {
            req.setAttribute("accObsLvl", "High");
        }
        if (acc.getObservation_level() == 4) {
            req.setAttribute("accObsLvl", "Highest");
        }

        if(acc.getImage1() != null) {
            req.setAttribute("Image1", acc.getImage1());
        } else {
            req.setAttribute("Image1", "");
        }
        if(acc.getImage2() != null) {
            req.setAttribute("Image2", acc.getImage2());
        } else {
            req.setAttribute("Image2", "");
        }
        if(acc.getImage3() != null) {
            req.setAttribute("Image3", acc.getImage3());
        } else {
            req.setAttribute("Image3", "");
        }

        req.setAttribute("accName", "" + acc.getName());
        req.setAttribute("accType", "" + acc.getType());
        req.setAttribute("accTime", "" + strTime);
        req.setAttribute("accLoc", "");
        req.setAttribute("accDesc", "" + acc.getDescription());
        req.setAttribute("accId", "" + acc.getId());

        dispatcher.forward(req, resp);
    }
}
