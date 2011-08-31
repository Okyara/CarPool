/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import data.*;
import business.*;
import javax.servlet.ServletContext;

/
public class RegisterUserServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterNewUserServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterNewUserServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
        } finally { 
            out.close();
        }
    } 

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        Gender gender = Gender.valueOf(request.getParameter("gender"));
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String SJSU_ID = request.getParameter("SJSU_ID");
        String USER_NAME = request.getParameter("USER_NAME");
        String password = request.getParameter("password");
        UserType userType = UserType.valueOf(request.getParameter("userType"));

        User user = new User(firstName, lastName, USER_NAME,
                email, password, SJSU_ID, gender, userType, phone);

        // save user to db:
        if(DBQuery.addUser(user)) {
            ServletContext context = getServletContext();
            context.setAttribute("user", user);
            Boolean logged_in = true;
            context.setAttribute("logged_in", logged_in);

            RequestDispatcher dispatch = getServletContext().getRequestDispatcher
                    ("/RegisterUser.jsp");
            dispatch.forward(request, response);
        }
        else
            System.out.println("DOESNT WORK");
    }

    /** Returns a short description of the servlet.
        @return a String containing servlet description
        */
}
