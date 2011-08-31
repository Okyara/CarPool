package control;

import business.Feedback;
import business.User;
import data.DBQuery;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** AddFeedbackServlet
    Allows a user to evaluate another user.  Sends the
    resulting feedback to the database.
 */

public class AddFeedbackServlet extends HttpServlet
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
        if (user == null)
        {
            forwardFailureNotice(request, response);
            return;
        }

        // create variable pointing to servlet context "logged_in":
        Boolean logged_in = (Boolean)getServletContext()
                .getAttribute("logged_in"); // stupid autoboxing :-/
        if (logged_in == null || !logged_in)
        {
            forwardFailureNotice(request, response);
            return;
        }

        // retrieve form info:
        String rec_id;
        int score_gd, score_time, score_reli;

        try
        {
            rec_id = request.getParameter("receiver");
            score_gd = Integer.parseInt(request.getParameter("gd"));
            score_time = Integer.parseInt(request.getParameter("tim"));
            score_reli = Integer.parseInt(request.getParameter("reli"));
        }
        catch(NumberFormatException e) { throw new ServletException(); }

        // do business:
        boolean success = false;
        boolean hasPrevFeedback = user.hasLeftFeedbackOn(rec_id);
        if (!hasPrevFeedback)
            success = DBQuery.addFeedback(user.getSJSU_ID(),
                rec_id, score_gd, score_time, score_reli);
        else success = DBQuery.updateFeedback(user.getSJSU_ID(),
                rec_id, score_gd, score_time, score_reli);

        request.setAttribute("commenter", user.getUSER_NAME());
        request.setAttribute("receiver_id", rec_id);
        request.setAttribute("score_gd", score_gd);
        request.setAttribute("score_time", score_time);
        request.setAttribute("score_reli", score_reli);

        // send response to browser:
        RequestDispatcher dispatch;
        if (success)
        {
            dispatch = getServletContext()
                    .getRequestDispatcher("/index.jsp");
            if (!hasPrevFeedback)
            {
                request.setAttribute("message", "Your feedback was "
                    + "successfully saved.");
                user.addFeedback(new Feedback(user.getSJSU_ID(),
                        rec_id, "", score_gd, score_time, score_reli));
            }
            else
            {
                request.setAttribute("message", "Your feedback was "
                    + "successfully updated.");
                user.updateFeedback(rec_id, score_gd, score_time, score_reli);
            }

            dispatch.forward(request, response);
        }
        else forwardFailureNotice(request, response);

    }

    /** Helper method: what to do if could not save feedback.
         @param request servlet request
         @param response servlet response
         @throws ServletException if a servlet-specific error occurs
         @throws IOException if an I/O error occurs
         */

    private void forwardFailureNotice(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        RequestDispatcher dispatch = getServletContext()
                .getRequestDispatcher("/index.jsp");
        request.setAttribute("message", "Your feedback could not "
                + "be saved.  You are a horrible person.");
        dispatch.forward(request, response);
    }

    /** Returns a short description of the servlet.
        @return a String containing servlet description
        */

    @Override
    public String getServletInfo() { return "Add Feedback Servlet"; }
}
