package cz.muni.fi.xharting.classic.test.startup;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The servlet creates a new {@link HttpSession} (if not yet exists) on any GET request.
 * 
 * @author pan
 *
 */
@SuppressWarnings("serial")
@WebServlet("/classic/test")
public class SessionCreatorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // trigger the HttpSessionListener notification
        req.getSession(true);
        resp.setContentType("text/plain");
        resp.setStatus(200);
        return;
    }

}
