package pl.rapala.magdalena.railfence6.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author Magdalena Rapala
 * @version 1.2
 */
public class ModelContextListener implements ServletContextListener {

    /**
     *  model instance to be used by the entire application.
     */
    private Model model;
    
    /**
     * emf is the instance of EntityManagerFactory which will be used by the entire application.
     */
    private EntityManagerFactory emf;
    
    /**
     * em is the instance of the EntityManager which will be used by the entire application.
     */
    private EntityManager em;
    
    /**
     * ModelContextListener empty constructor.
     */
    public ModelContextListener(){
        // Empty constructor.
    }
    
    /**
     * contextInitialized is a class method that initialises the context for the entire application lifecycle.
     * 
     * @param sce ServletContextEvent.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        // One-time creation of a model object to handle all requests throughout the entire application life cycle.
        model = new Model();
        
        // No embedded info.
        String puName = sce.getServletContext().getInitParameter("puNAME");
        emf = Persistence.createEntityManagerFactory(puName);
        em = emf.createEntityManager();
        
        sce.getServletContext().setAttribute("model", model);
        sce.getServletContext().setAttribute("EntityManager", em);

    }

    /**
     * contextDestroyed is a method that gets called when the application life cycle is ending.
     * 
     * @param sce ServletContextEvent.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
