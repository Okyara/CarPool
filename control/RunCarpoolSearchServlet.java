package control;

import business.User;
import business.UserType;
import business.Carpool;
import data.DBQuery;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/** RunCarpoolSearchServlet
    Search for matching carpools.
\ */
public class RunCarpoolSearchServlet extends HttpServlet
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
        // first set headers (e.g. content type):
        response.setContentType("text/html;charset=UTF-8");

        // then extract print writer:
        PrintWriter out = response.getWriter();
        out.close(); // close the PrintWriter! :D
    }

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
        doPost(request, response);
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
        ServletContext context = getServletContext();

        // Generate logout message:
        User searching = (User)context.getAttribute("user");
        Boolean logged_in = (Boolean)context.getAttribute("logged_in");
        if (logged_in == null || searching == null)
        {
            searching = new User(); // work as non-logged-in entity
            logged_in = false;
        }

        Date start = java.sql.Date.valueOf(
                    (request.getParameter("startYear")) + "-" +
                    (request.getParameter("startMonth")) + "-" +
                    (request.getParameter("startDay")));
        Date end = Date.valueOf(
                    (request.getParameter("endYear")) + "-" +
                    (request.getParameter("endMonth")) + "-" +
                    (request.getParameter("endDay")));
        Time time = Time.valueOf(
                    (request.getParameter("hour")) + ":" +
                    (request.getParameter("minutes")) + ":00");
        boolean once = Boolean.parseBoolean(request.getParameter("frequency"));
        boolean toCampus = Boolean.parseBoolean(request.getParameter("direction"));
        boolean sun = (request.getParameter("sun") != null && request.getParameter("sun").length() > 0);
        boolean mon = (request.getParameter("mon") != null && request.getParameter("mon").length() > 0);
        boolean tue = (request.getParameter("tue") != null && request.getParameter("tue").length() > 0);
        boolean wed = (request.getParameter("wed") != null && request.getParameter("wed").length() > 0);
        boolean thu = (request.getParameter("thu") != null && request.getParameter("thu").length() > 0);
        boolean fri = (request.getParameter("fri") != null && request.getParameter("fri").length() > 0);
        boolean sat = (request.getParameter("sat") != null && request.getParameter("sat").length() > 0);
        boolean days[] = {sun, mon, tue, wed, thu, fri, sat};
        
        Carpool searchCrit = new Carpool();

        searchCrit.setStartDate(start);
        searchCrit.setEndDate(end);
        searchCrit.setCampusTime(time);
        searchCrit.setOneTime(once);
        searchCrit.setDaysOfWeek(days);
        searchCrit.setToSJSU(toCampus);

        // I presume "days" ENDS in the oneTime bit, not BEGINS? the LOAD data
        // seems to take it that way
        String dayStr = "";
        for (boolean b : searchCrit.getDaysOfWeek())
            if (b) dayStr += "1"; else dayStr += "0";
        if (searchCrit.getOneTime()) dayStr += "1"; else dayStr += "0";

        Collection<Carpool> results =
                DBQuery.searchCarpools(start.toString(),
                                       end.toString(),
                                       dayStr,
                                       toCampus,
                                       time.toString());

        request.setAttribute("results", results);
        
        // little test message to see what we're looking for
        
        String testTime = new String();
        String testStart = new String();
        String testEnd = new String();
        String testFrequency = new String();
        String testDirection = new String();
        String testDays = new String();
        String testDrive = new String();

        if(toCampus) {
            testDirection += "to SJSU";
            testTime += "that gets to school before " + time;
            }
        else {
            testDirection += "to home";
            testTime += "that leaves school at or after " + time;
        }
        if(once) {
            testFrequency += "a one-time";
            testStart += "on" + start;
            }
        else {
            testFrequency += "a recurring";
            testDays += " on " + request.getParameter("day");
            testStart += "starting on " + start;
            testEnd += "and ending on " + end;
        }
        if(searching.getUserType() != UserType.Passenger)
            testDrive += ".  I am willing and able to drive.";
        else
            testDrive += ".  I'm a cheap, lazy bastard.";

        request.setAttribute("message", "Returned from Search Servlet.  "
                + "I am looking for " + testFrequency + " carpool " + testDirection 
                + " " + testTime + " " + testStart + " " + testEnd + " " + testDays
                + "." + "  BTW, " + testDrive + ".");

        // Back to view:
        RequestDispatcher dispatch = context
                .getRequestDispatcher("/DisplaySearchResults.jsp");
        dispatch.forward(request, response);
    }

    /** Returns a short description of the servlet.
        @return a String containing servlet description
        */

    @Override
    public String getServletInfo()
    {
        return "Carpool Search Servlet";
    }
}
