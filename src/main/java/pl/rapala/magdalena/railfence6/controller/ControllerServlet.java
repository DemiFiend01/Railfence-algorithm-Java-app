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
import java.util.HashMap;
import java.util.List;
import pl.rapala.magdalena.railfence6.model.CipherParams;
import pl.rapala.magdalena.railfence6.model.IncorrectNoOfRailsException;

import pl.rapala.magdalena.railfence6.model.Model;
import pl.rapala.magdalena.railfence6.model.ResultHistory;
/**
 * ControllerServlet is a class that extends the HttpServlet and acts as a Controller for the RailFence application.
 * 
 * @author Magdalena Rapala
 * @version 1.2
 */
@WebServlet(name = "MyServlet", urlPatterns = {"/Form"})
public class ControllerServlet extends HttpServlet {
    
    /**
     * model - an instance of {@link pl.rapala.magdalena.railfence6.model.Model} class, conducts the Rail Fence cipher.
     */
    private Model model; 
    
    /**
     * result - the end result of the conducted cipher.
     */
    private String result;
    
    /**
     * stats - is HashMap being used to count the events happening in the {@link pl.rapala.magdalena.railfence6.controller.ControllerServlet}.
     */
    private final HashMap<String, Integer> stats;

    /**
     * readFromCookies - a Boolean value regarding the initialization of {@link #stats } being read from cookies.
     */
    private boolean readFromCookies = false;
    
    /**
     * Constructor initiating statistics collection.
     */
    public ControllerServlet() {
        
        // Initializing the stats
        this.stats = new HashMap<>();

        stats.put("noOfErrors", 0);
        stats.put("en", 0);
        stats.put("de", 0);
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     * @throws IllegalArgumentException when there is some error regarding the cipher arguments.
     * @throws pl.rapala.magdalena.railfence6.model.IncorrectNoOfRailsException if an error regarding the number of rails occurs.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, IllegalArgumentException, IncorrectNoOfRailsException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        if(!this.readFromCookies)
        {
            Cookie[] cookies = request.getCookies();
            
            // Read cookies and obtain their values.
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("noOfErrors"))
                    {
                        stats.put("noOfErrors",Integer.valueOf(cookie.getValue()));
                    }
                    if(cookie.getName().equals("en"))
                    {
                        stats.put("en",Integer.valueOf(cookie.getValue()));
                    }
                    if(cookie.getName().equals("de"))
                    {
                        stats.put("de", Integer.valueOf(cookie.getValue()));
                    }
                }
            }
            this.readFromCookies = true;
        }
        
        // Reading the model from the context.
        model = (Model) getServletContext().getAttribute("model");
         
        try{
            // Assign the values to the model.
            model.setMode(request.getParameter("Mode"));
            model.setText(request.getParameter("inputText"));
            model.setNoOfRails(Integer.parseInt(request.getParameter("noOfRails")));
            // Let the model obtain the result.
            result = model.railFence();
            
            // Add it all to the database here.
            CipherParams cipherParams = new CipherParams();
            cipherParams.setModeSelect(request.getParameter("Mode"));
            cipherParams.setTextCipher(request.getParameter("inputText"));
            cipherParams.setNoOfRails(Integer.parseInt(request.getParameter("noOfRails")));
            
            ResultHistory resultHistory = new ResultHistory();
            resultHistory.setResult(result);
            
            // Link the two tables together.
            resultHistory.setCipherParams(cipherParams);
            cipherParams.setResultHistory(resultHistory);
            
            // Persist the two objects.
            try{
                persistObject(resultHistory); // Will cascade so no need to persist cipherParams.
            }catch(PersistenceException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString() + "Could not persist the results to the database.");
                return;
            }
            

        }catch(IllegalArgumentException | IncorrectNoOfRailsException e) // Try to catch the exceptions that are thrown in the model.
        {
            // Increment the counter of errors.
            stats.put("noOfErrors", stats.get("noOfErrors") + 1);
            
            Cookie cookieError = new Cookie("noOfErrors", stats.get("noOfErrors").toString()); // Converting int to String.
            response.addCookie(cookieError);
            
            // Send an error.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
            return;
        }

        // Counting the popularity of modes, after checking for errors to count only the valid attempts.  
        stats.put(model.getModeSelect(), stats.get(model.getModeSelect()) + 1);

        Cookie cookieMode = new Cookie(model.getModeSelect(), stats.get(model.getModeSelect()).toString()); // Converting int to String
        response.addCookie(cookieMode);
        
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Cipher result</title>");
            out.println("</head>");
            
            out.println("<body style=\"background-color:lightgreen;\">");
            
            List<CipherParams> readParams = null;
            try{
                readParams = findCipherObjects(request.getParameter("Mode"),request.getParameter("inputText"),Integer.parseInt(request.getParameter("noOfRails")));
            }catch (PersistenceException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString() + "Could not read the cipher parameters from the database.");
                return;
            }
            
            // Read the result linked to the data.
            List<ResultHistory> readResult = null;
            
            try{
                readResult = findLinkedHResult(readParams.getFirst());
            }catch (PersistenceException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString() + "Could not read the result of the cipher from the database.");
                return;
            }
            
            out.println("<h1>The end result:<br>"+ readResult.getFirst().getResult() + "</h1>");
            out.println("<h2>For the data:</h2>");
            
            
            out.println("<h3>For text: " + readParams.getFirst().getTextCipher() + "</h3>");
            out.println("<h3>For number of rails: " + readParams.getFirst().getNoOfRails() + "</h3>");
            out.println("<h3>For mode: " + readParams.getFirst().getModeSelect()+"</h3>");
            out.println("</br>");
            
            // Button to go back.
            out.println("<form action=\""+ request.getContextPath()+"/index.html\" method='post'>");
            out.println("<input type=\"submit\" style=\"height:40px;width:100px;background-color:mediumseagreen\" value=\"Go back\"/>");
            out.println("</form>");
            
            out.println("<br/><img src=\"images/duke_swinging.gif\" width=\"100\" height=\"100\" /><br/>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /**
     * persistObject is a method which takes the object and saves it into the database.
     * 
     * @param object that is to be saved into the database.
     * @throws PersistenceException when something goes wrong with the transaction.
     */
    void persistObject(Object object) throws PersistenceException{
        
        EntityManager em = (EntityManager) getServletContext().getAttribute("EntityManager");
        em.getTransaction().begin();
        synchronized(em){ //to not close it automatically
            try {
                em.persist(object);
                em.getTransaction().commit();
            } catch (PersistenceException e) {
                em.getTransaction().rollback();
                throw e;
            }
        }
    }

    /**
     * findCipherObjects is a method which by given parameters searches the database and returns the object with equal parameters.
     * 
     * @param mode the mode of the cipher operation - either Encryption or Decryption (en, de).
     * @param text the input text for the cipher.
     * @param numberOfRails the number of rails for the cipher.
     * @return the list of all CipherParams which satisfy the equal parameters requirement.
     * @throws PersistenceException when something goes wrong with the transaction.
     */
    public List<CipherParams> findCipherObjects(String mode, String text, int numberOfRails) throws PersistenceException{
        List<CipherParams> cipherList = null;
        EntityManager em = (EntityManager) getServletContext().getAttribute("EntityManager");
        
        synchronized(em)
        {
            try{
                Query query = em.createQuery("SELECT p FROM CipherParams p where p.modeSelect= :mode and p.textCipher= :text and p.noOfRails= :numberOfRails");
                query.setParameter("mode", mode);
                query.setParameter("text", text);
                query.setParameter("numberOfRails", numberOfRails);
                cipherList = (List<CipherParams>) query.getResultList();
            } catch (PersistenceException e) {
                throw e;
            }
        }

        return cipherList;
    }
    
    /**
     * findLinkedHResult is a method that takes the CipherParams object and returns a list (one) of ResultHistory linked to that object.
     * 
     * @param cipherParams is the object for which the linked ResultHistory is returned.
     * @return the ResultHistory linked to the param.
     * @throws PersistenceException when something goes wrong with the transaction.
     */
    public List<ResultHistory> findLinkedHResult(CipherParams cipherParams) throws PersistenceException{
        List<ResultHistory> linkedHResult = null;
        
        EntityManager em = (EntityManager) getServletContext().getAttribute("EntityManager");
        
        synchronized(em)
        {
            try {
                Query query = em.createQuery("SELECT p FROM ResultHistory p WHERE p.cipherParams = :cipherParams");
                query.setParameter("cipherParams", cipherParams);
                linkedHResult = (List<ResultHistory>) query.getResultList();
            } catch (PersistenceException e) {
                throw e;
            }
        }
        
        return linkedHResult;
    }

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
        try {
            processRequest(request, response);
        } catch (IllegalArgumentException ex) {
            System.getLogger(ControllerServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IncorrectNoOfRailsException ex) {
            System.getLogger(ControllerServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (IllegalArgumentException ex) {
            System.getLogger(ControllerServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IncorrectNoOfRailsException ex) {
            System.getLogger(ControllerServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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
