/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import business.User;
import data.DBQuery;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** SendMessageServlet
    Allows a user to send a message to another user.  Sends the
    resulting message to the database.
    */

public class SendMessageServlet extends HttpServlet
{
    // ------[REQUEST PROCESSING]------ //

    /** Processes requests for both HTTP <code>GET</code> and
        <code>POST</code> methods.
        @param request servlet request
        @param response servlet response
        @throws ServletException if a servlet-specific error occurs
        @throws IOException if an I/O error occurs
        */

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        // first set headers (e.g. content type):
        response.setContentType("text/html;charset=UTF-8");

        // then extract print writer:
        PrintWriter out = response.getWriter();
        out.close(); // close the PrintWriter! :D
    }

    // ------[OTHER SERVLET METHODS]------ //

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
        // create variable pointing to servlet context variable "user":
        User user = (User)getServletContext().getAttribute("user");
        if (user != null)
        {
            // create variable pointing to servlet context "logged_in":
            Boolean logged_in = (Boolean)getServletContext()
                    .getAttribute("logged_in"); // stupid autoboxing :-/
            if (logged_in == null || !logged_in)
                return;
        }

        // retrieve form info:
        String sendername, receivername, messagetext;

        if (user != null)
            sendername = user.getUSER_NAME();
        else sendername = "Anonymous";
        receivername = request.getParameter("receivername");
        messagetext = request.getParameter("messagetext");

        if (messagetext == null || messagetext.length() < 1)
            return; // don't send an empty

        ServletContext context = getServletContext();
        context.log("Sending message from " + sendername + " to " + receivername
                + ": " + messagetext);

        // do business:
        DBQuery.saveMessage(sendername, receivername, messagetext);
    }

    /** Returns a short description of the servlet.
        @return a String containing servlet description
        */

    @Override
    public String getServletInfo() { return "Send Message Servlet"; }
}
