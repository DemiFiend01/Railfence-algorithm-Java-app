package pl.rapala.magdalena.railfence.controller;

import lombok.Getter;
import pl.rapala.magdalena.railfence.model.IncorrectNoOfRailsException;
import pl.rapala.magdalena.railfence.view.View;
import pl.rapala.magdalena.railfence.model.Model;

/**
 * A class containing one method that controls the cipher program, calling
 * appropriate methods to conduct Rail Fence cipher, It also uses @Getter from Lombok.
 *
 * @author Magdalena Rapala
 * @version 1.2
 */
@Getter
public class Controller {

    /**
     * view - an instance of a View class needed for the GUI and input/output.
     */
    private final View view;
    /**
     * model - an instance of Model class, conducts the Rail Fence cipher.
     */
    private final Model model;

    /**
     * Initiates a {@link Controller} object.
     * @param view class managing GUI and I/O.
     * @param model class managing the logic and data.
     */
    public Controller(View view, Model model)
    {
        this.view = view;
        this.model = model;
        

        // Adds the method cipher as a runButton from view listener.
        this.view.addRunButtonListener(e->cipher());
    }

    /**
    * cipher is a method that is tied to the {@link pl.rapala.magdalena.railfence.view.View#runButton}.
    * When the runButton is triggered, this method is called.
    */
    private void cipher()
    {

        try{
            // Obtain the input values from the user via view.
            String text = view.getTextInput();
            String mode = view.getMode();
            int rails = view.getRails();

            // Assign the values to the model.
            model.setText(text);
            model.setMode(mode);
            model.setNoOfRails(rails);

            // Let the model obtain the result.
            String result = model.railFence();
            // Display the result via view.
            view.setResult(text,mode,rails,result);
        }catch(IllegalArgumentException | IncorrectNoOfRailsException e) // Try to catch the exceptions that are thrown in the model.
        {
            view.showError(e.toString());
        }
    }
    /**
     * Main method that reads the console line arguments or asks the user for
     * them, conducting the Rail Fence cipher accordingly to those arguments.
     *
     * @param args app call parameters.
     */
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater( () -> {
                View view = new View();
                Model model = new Model();

                // Initiate the GUI.
                view.createAndShowGUI();
                Controller controller = new Controller(view, model);
            });

    }
}