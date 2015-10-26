package ifn701.safeguarder.backend.controllers.auto_upload_events;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

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

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String blobUploadUrl = blobstoreService.createUploadUrl("/controlpanel/receiveUploadFiles");
        blobUploadUrl = blobUploadUrl.replace(Constants.computerName, Constants.IPAddress);

        String jspPage = Constants.jsp_prefix + "auto_load_events.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPage);

        req.setAttribute("uploadUrl", blobUploadUrl);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String blobUploadUrl = blobstoreService.createUploadUrl("/receiveUploadFiles");
        blobUploadUrl = blobUploadUrl.replace(Constants.computerName, Constants.IPAddress);

        String jspPage = Constants.jsp_prefix + "auto_load_events.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(jspPage);

        req.setAttribute("uploadUrl", blobUploadUrl);
        dispatcher.forward(req, resp);
    }
}