package pl.rapala.magdalena.railfence.view;
import pl.rapala.magdalena.railfence.model.LogEntry;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import lombok.Getter;

/** 
 * A class containing many methods that create the GUI and read/write information from/to the user and uses @Getter from Lombok.
 * 
 * @author Magdalena Rapala
 * @version 1.4
 */
@Getter
public class View extends JPanel
                  implements ActionListener{

    /**
    * textField field is for reading the text from the user.
    */
    private JTextField textField;
    /**
    * railsField field is for reading the number of rails information from the user.
    */
    private JTextField railsField;
    /**
    * modeBox contains two options, Encryption and Decryption and is allows for selecting the mode.
    */
    private JComboBox<String> modeBox;
    /**
    * runButton runs the algorithm.
    */
    private JButton runButton;

    /**
    * outputArea is located on the right side of the window and it shows the output of the cipher.
    */
    private JTextArea outputArea;
    /**
    * logArea is located on the bottom of the window and it prints out all the history of performed actions.
    */
    private JTable logArea;

    /**
     * logTableModel is an implementation of TableModel that uses a Vector of Vectors to store the cell value objects.
     */
    private DefaultTableModel logTableModel;

    /**
    * TEXT_ENTERED is a command text set for {@link #textField}.
    */
    private static final String TEXT_ENTERED = "text";
    /**
    * NUM_ENTERED is a command text set for {@link #railsField}.
    */
    private static final String NUM_ENTERED = "number";
    /**
    * RUN is a command text set for {@link #runButton}.
    */
    private static final String RUN = "run";
    /**
    * MODE is a command text set for {@link #modeBox}.
    */
    private static final String MODE = "mode";
    /**
    * MENU_OPTION1 is a command text set for the first menu option.
    */
    private static final String MENU_OPTION1 = "menu option 1";
    /**
    * MENU_OPTION2 is a command text set for the second menu option.
    */
    private static final String MENU_OPTION2 = "menu option 2";

     /**
     * Initiates a {@link View} object.
     */
    public View() {
        // Empty constructor.
    }

    /**
    * addButtons is a method that adds the buttons to the left JPanel.
    * 
    * @param panel panel which will obtain buttons.
    */
    protected void addButtons(JPanel panel)
    {
        // Creating a panel on the left side of the window where we will have the input fields.
        panel.setBorder(BorderFactory.createTitledBorder("Input"));

        // Rows, columns, horizontal gap and vertical gap.
        JPanel lastRow = new JPanel(new GridLayout(1,2,6,6));

        // Creating the label for the text input field.
        JLabel textLabel = new JLabel("Text");

        // The text inside will be empty.
        textField = new JTextField("");
        textLabel.setLabelFor(textField);
        // There is no need for columns now.

        textField.addActionListener(this);
        // Assigning an action command.
        textField.setActionCommand(TEXT_ENTERED);
        // Accessibility.
        textField.setToolTipText("Enter the text for the cipher here.");
        textField.getAccessibleContext().setAccessibleDescription("Text input field for the cipher.");
        // Setting a shortcut alt + T.
        textLabel.setDisplayedMnemonic('T');

        // Add the text field with the label to the panel.
        panel.add(setLabelForComponent(textField,textLabel));

        // Creating a new label and a text field to input the number of the rails.
        JLabel railsLabel = new JLabel("Rails: ");
        // The text inside will be empty.
        railsField = new JTextField("");
        railsLabel.setLabelFor(railsField);
        railsField.addActionListener(this);
        // Assigning an action command.
        railsField.setActionCommand(NUM_ENTERED);
        railsField.setToolTipText("Enter the number of the rails here.");
        railsField.getAccessibleContext().setAccessibleDescription("Number of rails input field for the cipher.");
        // Setting a shortcut alt + N.
        railsLabel.setDisplayedMnemonic('N');

        // Adding the field with the label to the panel.
        panel.add(setLabelForComponent(railsField,railsLabel));

        // Creating a label for the mode input field in the form of a JComboBox.
        JLabel modeLabel = new JLabel("Mode");

        // Creating a JComboBox for two options Encryption and Decryption.
        modeBox = new JComboBox<>(new String[]{"Encryption","Decryption"});

        modeLabel.setLabelFor(modeBox);
        // Assigning an action command.
        modeBox.setActionCommand(MODE);
        // Accessibility.
        modeBox.setToolTipText("Select one of the two modes. Encryption or Decryption.");
        modeBox.getAccessibleContext().setAccessibleDescription("Mode selection field. Encryption or Decryption.");
        // Setting a shortcut alt + M.
        modeLabel.setDisplayedMnemonic('M');

        // Adding the combobox to the lastRow panel.
        lastRow.add(setLabelForComponent(modeBox,modeLabel));

        // Creating a label and a new JButton for initiating the cipher.
        JLabel runLabel = new JLabel("Run the cipher");
        runButton = new JButton("Run");
        runButton.setActionCommand(RUN);
        // Accessibility options.
        runButton.setToolTipText("Click here to run the cipher.");
        runButton.getAccessibleContext().setAccessibleDescription("Button to run the cipher.");
        // Setting a shortcut alt + R.
        runButton.setMnemonic('R');

        // Adding the runButton to the lastRow panel.
        lastRow.add(setLabelForComponent(runButton,runLabel));

        // Adding the lastRow with the comboBox and the runButton to the panel.
        panel.add(lastRow);
    }

    /**
    * createMenuBar is a method that creates a JMenuBar, expands it and returns it.
    * 
    * @return menuBar - created JMenuBar with added menuItems.
    */
    protected JMenuBar createMenuBar()
    {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        // Creating the menuBar.
        menuBar = new JMenuBar();

        // Creating the menu.
        menu = new JMenu("Menu");        
        // Shortcut alt + 0.
        menu.setMnemonic(KeyEvent.VK_0);
        // Accessibility.
        menu.getAccessibleContext().setAccessibleDescription(
                "Menu with two options.");
        menu.setToolTipText("Menu with two options.");
        menuBar.add(menu);

        // Adding the first option in the menu.
        menuItem = new JMenuItem("Help information.");
        // Shortcut alt + 1, setMnemonic does not work on this.
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        // Setting the important accessibility.
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This option shows a pop up window with information about the program usage.");
        menuItem.setToolTipText("Information about the program usage.");
        menuItem.addActionListener(this);
        menuItem.setActionCommand(MENU_OPTION1);
        menu.add(menuItem); // Adding the item to the menu.

        // Adding the second option in the menu.
        menuItem = new JMenuItem("Clear the output.");
        // Shortcut alt + 2.
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        // setting the accessibility help ;)
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This option clears the two text fields, History and Output.");
        menuItem.setToolTipText("Clear the two text fields, History and Output.");
        menuItem.addActionListener(this);
        // Assigning the command.
        menuItem.setActionCommand(MENU_OPTION2);
        menu.add(menuItem); // Adding the item to the menu.

        return menuBar;
    }

    /**
    * createSplitPane is a method that creates a JSplitPane, expands it greatly, adding many JPanels and a separate JSplitPane, and then returns it.
    * 
    * @return JSplitPane splitPane.
    */
    protected JSplitPane createSplitPane()
    {
        JPanel inputPanel = new JPanel(new GridLayout(3,1,6,6));   
        addButtons(inputPanel);

        // Creating a text area for the output and assiging a label to it.
        JLabel outputAreaLabel = new JLabel("Output");
        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        // Accessibility.
        outputArea.setToolTipText("Cipher results.");
        outputArea.getAccessibleContext().setAccessibleDescription("A text field with the cipher results.");
        outputAreaLabel.setLabelFor(outputArea);
        // Shortcut alt + o.
        outputAreaLabel.setDisplayedMnemonic(KeyEvent.VK_O);

        JScrollPane scrollPane = new JScrollPane(setLabelForComponent(outputArea,outputAreaLabel));

        // Creating a label, table model and a table to store all performed actions.
        JLabel logAreaLabel = new JLabel("History");
        logTableModel = new DefaultTableModel(new Object[]{"Action","Input Text","Rails","Mode","Result"},0);
        logArea = new JTable(logTableModel);
        logArea.setFillsViewportHeight(true);
        logArea.setToolTipText("History of all actions performed.");
        logArea.getAccessibleContext().setAccessibleDescription("A text field with history of all actions perfomed.");
        logAreaLabel.setLabelFor(logArea);
        // Shortcut alt + H.
        logAreaLabel.setDisplayedMnemonic(KeyEvent.VK_H);

        JScrollPane scrollLogPane = new JScrollPane(logArea);

        JPanel userSide = new JPanel(new BorderLayout());
        JPanel outputSide = new JPanel(new BorderLayout());

        JPanel logSide = new JPanel(new BorderLayout());

        userSide.add(inputPanel, BorderLayout.CENTER);
        outputSide.add(scrollPane, BorderLayout.CENTER);

        JSplitPane upPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userSide, outputSide);
        setSplitPaneOptions(upPane);

        logSide.add(scrollLogPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(SwingConstants.HORIZONTAL, upPane, logSide);
        setSplitPaneOptions(splitPane);

        return splitPane;
    }

    /**
    * createAndShowGUI is a method that creates the JFrame frame and builds it alongside the JPanel.
    */
    public void createAndShowGUI() {
        // Setting a nicer look for our window.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Creating and preparing our window.
        JFrame frame = new JFrame("RailFenceCipher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(650,500));

        frame.setJMenuBar(this.createMenuBar());

        // Working on JPanel which is our View.
        this.setLayout(new BorderLayout());
        this.add(this.createSplitPane(), BorderLayout.CENTER);
        this.setOpaque(true); 

        // Adding the JPanel content to our frame (window).
        frame.setContentPane(this);

        // Packing our window (stuffing JPanel in it) and making our window (frame) visible.
        frame.pack();
        frame.setVisible(true);
    }

    /**
    * setSplitPaneOptions is a method that sets the options for JSplitPane component to make it look and behave better.
    * 
    * @param splitPane is a JSplitPane that will have options set.
    */
    protected void setSplitPaneOptions(JSplitPane splitPane)
    {
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        // The line between the splitPanes.
        splitPane.setDividerSize(10);
    }

    /**
    * setLabelForComponent is a method that sets the label above the component.
    * 
    * @param upperField could be any JComponent.
    * @param label JLabel.
    * @return panel with the label set.
    */
    protected JPanel setLabelForComponent(JComponent upperField, JLabel label)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(upperField, BorderLayout.CENTER);
        return panel;
    }

    /**
    * setResult is a method that prints out to the {@link #outputArea} and {@link #logArea} the result of the cipher.
    * 
    * @param inputText the text that was given by the user.
    * @param mode is the chosen mode (Encryption or Decryption).
    * @param noOfRails is the number of rails provided by the user.
    * @param resultText is the result of the cipher.
    */    
    public void setResult(String inputText, String mode, int noOfRails, String resultText)
    {
        String outputText = "Input text: "+inputText+"\nSelected mode: "+mode+"\nNumber of rails: "+ noOfRails+"\nResult: "+resultText+'\n';
        outputArea.append(outputText);
        
        // Creating a LogEntry record.
        LogEntry entry = new LogEntry("Cipher operation",
            inputText,
            noOfRails,
            mode,
            resultText);
        
        logTableModel.addRow(new Object[]
        {
            entry.action(),
            entry.inputText(),
            entry.rails(),
            entry.mode(),
            entry.result()
        });
    }

    /**
    * showError is a method that via a JOptionPane shows a message dialog informing the user of an error.
    * 
    * @param errorText the text that was obtained from catching an error.
    */
    public void showError(String errorText)
    {
        JOptionPane.showMessageDialog(this,errorText);
    }

    /**
    * getTextInput is a method that reads the text input from {@link #textField}.
    * 
    * @return text input from the user.
    */
    public String getTextInput()
    {
        return textField.getText();
    }

    /**
    * getMode is a method that reads from the {@link #modeBox} the selected option and based on it, returns the cipher mode.
    * 
    * @return either 'en', which stands for Encryption or 'de' - Decryption.
    */
    public String getMode()
    {
        String mode = (String) modeBox.getSelectedItem();
        if(mode.equals("Encryption"))
            return "en";
        else
            return "de";
    }

    /**
    * getRails is a method that reads the number of rails from the {@link #railsField} and returns the integer value.
    * 
    * @return the integer value of the number of rails.
    */
    public int getRails(){
        return Integer.parseInt(railsField.getText());
    }

    /**
    * addRunButtonListener is a method that adds an Action Listener to the {@link #runButton}. Used in the {@link pl.rapala.magdalena.railfence.controller.Controller} class.
    * 
    * @param listener interface for receiving action events. It allows the controller to communicate with the Model and the View.
    */
    public void addRunButtonListener(ActionListener listener)
    {
        runButton.addActionListener(listener);
    }

    /**
    * actionPerfomed is a method that responds to an event and assigns a reaction tied to it based on command texts.
    * 
    * @param e is the ActionEvent.
    */
    @Override
    public void actionPerformed(ActionEvent e){
        String cmd = e.getActionCommand();

        // For each interactable component.
        if (TEXT_ENTERED.equals(cmd)) // Entered the text.
        { 
            onTextEntered(e);
        } else if (NUM_ENTERED.equals(cmd)) // Entered the number of rails.
        {
            onNumberEntered(e);
        }else if (MODE.equals(cmd)) // Entered the mode option.
        {
            onModeSelected(e);
        } else if (RUN.equals(cmd)) // Pressed the run button.
        {
            onRunButtonPressed(e);
        } else if(MENU_OPTION1.equals(cmd)) // Selected the first menu option.
        {
            onMenuOption1Selected(e);
        }else if(MENU_OPTION2.equals(cmd)) // Selected the second menu option.
        {
            onMenuOption2Selected(e);
        }

    }

    /**
    * onTextEntered is a method that responds to an event and is called for the {@link #textField}.
    * 
    * @param e is the ActionEvent.
    */
    protected void onTextEntered(ActionEvent e)
    {
        JTextField text = (JTextField) e.getSource();
        displayResult("Text entered: \"" + text.getText() + "\"");
    }

    /**
    * onNumberEntered is a method that responds to an event and is called for the {@link #railsField}.
    * 
    * @param e is the ActionEvent.
    */
    protected void onNumberEntered(ActionEvent e)
    {
        JTextField text = (JTextField) e.getSource();
        displayResult("Number of rails entered: \"" + text.getText() + "\"");
    }

    /**
    * onRunButtonPressed is a method that responds to an event and is called for the {@link #runButton}.
    * 
    * @param e is the ActionEvent.
    */
    protected void onRunButtonPressed(ActionEvent e)
    {
        displayResult("Run button pressed.");
    }

    /**
    * onModeSelected is a method that responds to an event and is called for the {@link #modeBox}.
    * 
    * @param e is the ActionEvent.
    */
    protected void onModeSelected(ActionEvent e)
    {
        JTextField text = (JTextField) e.getSource();
        displayResult("Number of rails entered: \"" + text.getText() + "\"");

    }

    /**
    * onMenuOption1Selected is a method that responds to an event and is called for the first menu option.
    * 
    * @param e is the ActionEvent.
    */
    protected void onMenuOption1Selected(ActionEvent e)
    {
        displayResult("Information menu option has been selected.");
        String message = "<html>This program can help you encrypt or decrypt a text using Rail fence cipher.<br>"
                + "The program consists of:"
                + "<ul><li><i>Menu</i> located on the upper panel of the window.<br><i>(Shortcut: alt + 0)</i>. Menu has two options:"
                + "<ul><li><i>Information option</i> which is what You have selected a moment ago.<br>Here you can read the description behind each object inside the program.<br><i>(Shortcut: alt + 1)</i>"
                + "<li><i>Clear text fields</i>. This option clears both History and Output fields.<br><i>(Shortcut: alt + 2)</i></ul>"
                + "<li><i>Input panel</i> located on the left side and contains:"
                + "<ul><li><i>Text field</i>. Write your input text there.<br>It <u>cannot</u> contain non-English letters. <i>(Shortcut: alt + T)</i>"
                + "<li><i>Number of rails input field</i>. Write the number of rails there.<br>It <u>cannot</u> be shorter or longer than the text's length. <i>(Shortcut: alt + N)</i>"
                + "<li><i>Box with the mode selection</i>.<br>Select your desired mode - Encryption or Decryption. <i>(Shortcut: alt + M)</i>"
                + "<li><i>Run button</i>. Press it if you want to run the cipher. <i>(Shortcut: alt + R)</i></ul>"
                + "<li><i>Output (result) field</i> located on the right side.<br>It contains the results of the cipher. <i>(Shortcut: alt + O)</i>"
                + "<li><i>History (log) field</i> located on the lower part of the window.<br>It contains the information about taken actions. <i>(Shortcut: alt + H)</i></ul>"
                + "<i>Thank You for reading.</i><br>- Magdalena Rapala</html>";
        JOptionPane.showMessageDialog(this,message);
    }

    /**
    * onMenuOption2Selected is a method that responds to an event and is called for the second menu option.
    * 
    * @param e is the ActionEvent.
    */
    protected void onMenuOption2Selected(ActionEvent e)
    {
        displayResult("Clearing the output and history fields.");

        // Replace all text with 'empty' string.
        outputArea.selectAll();
        outputArea.replaceSelection("");
        logTableModel.setRowCount(0);

        JOptionPane.showMessageDialog(this,"Cleared the output and history text fields.");
    }

    /**
    * displayResult is a method that displays the action performed by the user.
    * 
    * @param actionDescription the text to be displayed.
    */
    protected void displayResult(String actionDescription)
    {
        LogEntry entry = new LogEntry( actionDescription,
            "-",
            0,
            "-",
            "-");
        logTableModel.addRow(new Object[]{
            entry.action(),
            entry.inputText(),
            entry.rails(),
            entry.mode(),
            entry.result()
        });
    }
}