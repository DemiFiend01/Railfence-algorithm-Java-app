package pl.rapala.magdalena.railfence6.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import pl.rapala.magdalena.railfence6.model.CipherParams;
import pl.rapala.magdalena.railfence6.model.Model;

import pl.rapala.magdalena.railfence6.model.LogEntry;
import pl.rapala.magdalena.railfence6.model.ResultHistory;

/**
 * HistoryServlet is a HttpServlet class handling the cipher operation history.
 * 
 * @author Magdalena Rapala
 * @version 1.1
 */
@WebServlet(name = "HistoryServlet", urlPatterns = {"/History"})
public class HistoryServlet extends HttpServlet {
    
    /**
     * model - an instance of {@link pl.rapala.magdalena.railfence6.model.Model} class, allows for the access of actionHistory list.
     */
    private Model model;
    
    /**
     * history - a list of {@link pl.rapala.magdalena.railfence6.model.LogEntry} class records containing the history of all cipher operations.
     */
    private List<LogEntry> history;
    
    /**
     * Empty HistoryServlet constructor.
     */
    public HistoryServlet(){
        // Empty constructor.
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        // Reading the model class from the context.
        model = (Model) getServletContext().getAttribute("model");
        
        try (PrintWriter out = response.getWriter()) {
            
            // Read the history from the model class.
            history = model.getActionHistory();
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>History</title>");
            out.println("</head>");
            
            out.println("<body>");
            out.println("<body style=\"background-color:lightgreen;\">");
            out.println("<h1>Cipher operation history</h1>");
            
            out.println("<h2>Table with data from this session only:</h2>");
      
            if(history.isEmpty())
            {
                out.println("<h3>No actions have been taken yet. The history is empty.</h3>");

            }else{
  
                // Create the table.
                out.println("<table border='3'>");
                out.println("<tr>");
                out.println("<th>Action</th>");
                out.println("<th>Input</th>");
                out.println("<th>Rails</th>");
                out.println("<th>Mode</th>");
                out.println("<th>Result</th>");
                out.println("</tr>");
                // Create a record in the table for each record in the list.
                for(LogEntry record: history)
                {
                    out.println("<tr>");
                    out.println("<td>" + record.action() + "</td>");
                    out.println("<td>" + record.inputText() + "</td>");
                    out.println("<td>" + record.rails() + "</td>");
                    out.println("<td>" + record.mode() + "</td>");
                    out.println("<td>" + record.result() + "</td>");                                
                    out.println("</tr>");
                }
                out.println("</table>");
            }
            
            out.println("<h2>Table with data from the database:</h2>");
            
            List<ResultHistory> historyObjects = null;
            try{
                
                historyObjects = findHistoryObjects();
                
            }catch(PersistenceException e){ 
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString() + "Could not find the results from the database.");
                return;
            }
            
            if(historyObjects.isEmpty())
            {
                out.println("<h3>The database is empty.</h3>");
            }else
            {
                // Create the table.
                out.println("<table border='3'>");
                out.println("<tr>");
                out.println("<th>Input</th>");
                out.println("<th>Rails</th>");
                out.println("<th>Mode</th>");
                out.println("<th>Result</th>");
                out.println("</tr>");
                
                // Create a record in the table for each record in the list.
                for(ResultHistory historyFromDB: historyObjects)
                {
                    
                    List<CipherParams> cipherParams = null;
                    try{
                        cipherParams = findLinkedCParams(historyFromDB);
                    }catch(PersistenceException e)
                    {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString() + "Could not find the linked cipher parameters to the result record from the database.");
                        return;
                    }

                    out.println("<tr>");
                    out.println("<td>" + cipherParams.getFirst().getTextCipher()+ "</td>");
                    out.println("<td>" + cipherParams.getFirst().getNoOfRails() + "</td>");
                    out.println("<td>" + cipherParams.getFirst().getModeSelect() + "</td>");
                    out.println("<td>" + historyFromDB.getResult()+ "</td>");                           
                    out.println("</tr>");
                }
                out.println("</table>");
            }
            
            // Cookies and statistics.
            out.println("<br><h2>Statistics from cookies:</h2>");
            Cookie[] cookies = request.getCookies();
            int noOfErrors = 0;
            int noOfEn = 0;
            int noOfDe = 0;
            String mostPopularMode = null;
            
            // Read cookies and obtain their values.
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("noOfErrors"))
                    {
                        noOfErrors = Integer.parseInt(cookie.getValue());
                    }
                    if(cookie.getName().equals("en"))
                    {
                        noOfEn = Integer.parseInt(cookie.getValue());
                    }
                    if(cookie.getName().equals("de"))
                    {
                        noOfDe = Integer.parseInt(cookie.getValue());
                    }
                }
            }
            if(noOfEn == noOfDe)
            {
                if(noOfEn == 0)
                {
                    mostPopularMode = "The cipher has not been run yet.";
                }else
                {
                    mostPopularMode = "Both are equally popular.";
                }
            }else
            {
                mostPopularMode = noOfEn > noOfDe ? "Encryption." : "Decryption.";
            }
            
            out.println("Number of errors: " + noOfErrors +"</br>");
            out.println("The number of times each mode has been run:</br>");
            out.println("Encryption: " + noOfEn + "</br>");
            out.println("Decryption: " + noOfDe + "</br>");
            out.println("Most popular mode: " + mostPopularMode+"</br>");
            
            out.println("</br>");
            
            // Button to go back.
            out.println("<form action=\""+ request.getContextPath()+"/index.html\" method='post'>");
            out.println("<input type=\"submit\" style=\"height:40px;width:100px;background-color:mediumseagreen\" value=\"Go back\"/>");
            out.println("</form>");

            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /**
     * findHistoryObjects is a method which accesses the database and returns a list of all ResultHistory objects.
     * 
     * @return historyList is a list of all ResultHistory objects.
     * @throws PersistenceException when something goes wrong with the SQL query.
     */
    public List<ResultHistory> findHistoryObjects() throws PersistenceException{
        List<ResultHistory> historyList = null;
        EntityManager em = (EntityManager) getServletContext().getAttribute("EntityManager");
        //em.getTransaction().begin(); //select only
        synchronized(em)
        {
            try {
                Query query = em.createQuery("SELECT p FROM ResultHistory p");
                historyList = (List<ResultHistory>) query.getResultList();
            } catch (PersistenceException e) {
                throw e;
            }
        }
        
        return historyList;
    }
    
    /**
     * findLinkedCParams is a method which accesses the database and returns the list (one) CipherParams linked to the param resultHistory.
     * 
     * @param resultHistory the ResultHistory object for which the CipherParams is found.
     * @return linkedCParams a list (one) of linked CipherParams object to the resultHistory.
     * @throws PersistenceException when something goes wrong with the SQL query.
     */
    public List<CipherParams> findLinkedCParams(ResultHistory resultHistory) throws PersistenceException {
        List<CipherParams> linkedCParams = null;
        
        EntityManager em = (EntityManager) getServletContext().getAttribute("EntityManager");
        
        synchronized(em)
        {
            try {
                Query query = em.createQuery("SELECT p FROM CipherParams p WHERE p.resultHistory = :resultHistory");
                query.setParameter("resultHistory", resultHistory);
                linkedCParams = (List<CipherParams>) query.getResultList();
            } catch (PersistenceException e) {
                throw e;
            }
        }
        
        return linkedCParams;
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
