package control;

import business.User;
import data.DBQuery;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** LoginUserServlet
    Log user in to the system!
    @author CS160 Semicolon
    */

public class LoginUserServlet extends HttpServlet
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
    { ; }

    // ------[GET METHOD (UNUSED)]------ //

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
        throw new ServletException();
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
        // get parameters:
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // do business:
        User goodguy = DBQuery.getUser(username, password);
        ServletContext context = getServletContext();

        if (goodguy != null)
        {
            context.setAttribute("user", goodguy);
            context.setAttribute("logged_in", true);
            context.log("Successfully logged in "
                    + goodguy.getUSER_NAME() + ".");
            request.setAttribute("message", "Login successful: "
                    + goodguy.getUSER_NAME());
            request.setAttribute("login_success", true);
            request.setAttribute("username", goodguy.getUSER_NAME());
        }
        else request.setAttribute("message", "Bad username or password");

        RequestDispatcher dispatch = context
                .getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }

    /** Returns a short description of the servlet.
        @return a String containing servlet description
        */

    @Override
    public String getServletInfo()
    {
        return "User Login Servlet";
    }
}
