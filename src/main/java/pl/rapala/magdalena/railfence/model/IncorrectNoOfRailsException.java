package pl.rapala.magdalena.railfence.model;

/**
 * A derived exception class for errors relating to the number of rails.
 * 
 * @author Magdalena Rapala
 * @version 1.0
 */
public class IncorrectNoOfRailsException extends Exception{
    
    /**
     * Initiates a {@link IncorrectNoOfRailsException} object.
     */
    public IncorrectNoOfRailsException()
    {
        //empty
    }
    
    /**
     * Initiates a {@link IncorrectNoOfRailsException} object.
     * 
     * @param message cause | the message of the caught exception.
     */
    public IncorrectNoOfRailsException(String message)
    {
        super(message); // Calling the parent's constructor.
    }
    
}
