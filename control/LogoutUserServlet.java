package control;

import business.User;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** LogoutUserServlet
    Boot the user from the system.
 */

public class LogoutUserServlet extends HttpServlet
{
    // ------[REQUEST PROCESSING]------ //

    /** Processes requests for both HTTP <code>GET</code> and
        <code>POST</code> methods.
        @param request servlet request
        @param response servlet response
        @throws ServletException if a servlet-specific error occurs
        @throws IOException if an I/O error occurs
        */

    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        ServletContext context = getServletContext();

        // Generate logout message:
        User leaving = (User)context.getAttribute("user");
        Boolean logged_in = (Boolean)context.getAttribute("logged_in");
        if (logged_in != null && logged_in)
            request.setAttribute("message", leaving.getUSER_NAME()
                    + " has been logged out.");
        else request.setAttribute("message",
                "No one was logged in, and that's somehow still true.");

        logged_in = false;
        context.setAttribute("logged_in", logged_in);

        if (leaving == null) // prevent crashes
            context.setAttribute("user", new User());
        else leaving.clear(); // clear data

        // Back to view:
        RequestDispatcher dispatch = context
                .getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }

    // ------[GET METHOD]------ //

    /** Handles the HTTP <code>GET</code> method.
        @param request servlet request
        @param response servlet response
        @throws ServletException if a servlet-specific error occurs
        @throws IOException if an I/O error occurs
        */

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**  Handles the HTTP <code>POST</code> method.
         @param request servlet request
         @param response servlet response
         @throws ServletException if a servlet-specific error occurs
         @throws IOException if an I/O error occurs
         */

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
        @return a String containing servlet description
        */

    @Override
    public String getServletInfo()
    {
        return "Logout Servlet";
    }
}
