package ifn701.safeguarder.backend.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ifn701.safeguarder.backend.Constants;
import ifn701.safeguarder.backend.dao.UserDao;
import ifn701.safeguarder.backend.entities.User;

/**
 * Created by lua on 25/10/2015.
 */
public class AuthenticationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email="";
        String password="";
        if(req.getParameter("email") != null) {
            email = req.getParameter("email");
        }
        if(req.getParameter("password") != null) {
            password = req.getParameter("password");
        }

        if(email.isEmpty() || password.isEmpty()) {
            String jspPage = Constants.jsp_prefix + "login_failed.jsp";
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(jspPage);
            requestDispatcher.forward(req, resp);
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.loginUser(email, password);
        if(user == null) {
            //login faield
            String jspPage = Constants.jsp_prefix + "login_failed.jsp";
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(jspPage);
            requestDispatcher.forward(req, resp);
            return;
        } else if(!user.isAdmin()) {
            //Only admin allowed to be logged in(Admin id should be 0)
            //login faield
            String jspPage = Constants.jsp_prefix + "login_failed.jsp";
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(jspPage);
            requestDispatcher.forward(req, resp);
            return;
        } else {

            //login successfully
            HttpSession session = req.getSession(true);
            session.setAttribute(Constants.session_user_id, user.getId());

//            String jspPage = Constants.jsp_prefix + "index.jsp";
//            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(jspPage);
//            requestDispatcher.forward(req, resp);

            resp.sendRedirect("/");
        }
    }
}
