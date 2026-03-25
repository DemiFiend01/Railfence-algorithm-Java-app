package pl.rapala.magdalena.railfence6.model;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;

/** 
 * A class that realizes the Rail Fence cipher, This class contains many methods and uses @Getter from Lombok.
 * 
 * @author Magdalena Rapala
 * @version 1.5
 */
@Getter // Because most fields cannot be set freely.
public class Model {

    /**
     * modeSelect - either en - encoding or de - decoding.
     */
    private String modeSelect;
    /**
     * textCipher - the text to either encode or decode.
     */
    private String textCipher;
    /**
     * the number of rails needed for the cipher.
     */
    private int noOfRails;
    
    /**
     * actionHistory - arrayList from collections package containing all the history behind each action.
     */
    private final List<LogEntry> actionHistory = new ArrayList<>();

    /**
     * Initiates a {@link Model} object and assigns a View class to it.
     */
    public Model() {
       //empty
    }

     /**
     * Method setting the mode needed for the cipher and checking the validity.
     * 
     * @param mode en - encoding; de - decoding.
     * @throws IllegalArgumentException when the mode is neither 'en' nor 'de'.
     */
    public void setMode(String mode) throws IllegalArgumentException{
        
        // Lambda expression - V (RuntimeException) is the parent class of E (IllegalArgumentException), which is an unchecked exception.
        // message is a string that will be shown with the exception.
        // Runtimeexception is the base class for unchecked exceptions.
        // Compiler does not force to declare or catch them, its up to the coder to handle them.
        ExceptionLambda<RuntimeException, IllegalArgumentException> argExc = message -> new IllegalArgumentException(message);
        
        if(mode == null)
        {
            throw argExc.call("\nMode cannot be empty.");
        }
        
        mode = mode.trim().toLowerCase(); // Just in case, but it will not happen now with GUI.
        if(!mode.equals("en") && !mode.equals("de"))
        {
            // Using the lambda expression, call is a method from the functional interface ExceptionLambda.
            throw argExc.call("\nInvalid mode selected: "+mode+".\nCorrect are:\n'en' for encryption\n'de' for decryption.");
        }
        this.modeSelect = mode;
    }

     /**
     * Method setting the text for the cipher.
     * 
     * @param text text to be encoded or decoded.
     * @throws IllegalArgumentException when the text is empty.
     */
    public void setText(String text) throws IllegalArgumentException{
        
        ExceptionLambda<RuntimeException, IllegalArgumentException> argExc = message -> new IllegalArgumentException(message);

        text = text.trim(); // Trims off the leading and trailing spaces if any.
        Pattern p = Pattern.compile("[^A-Za-z0-9.,!? ]"); // non-Word character (any type of character that is not a letter, digit, or underscore).
        Matcher m = p.matcher(text);
        if(text.isEmpty())
        {
            throw argExc.call("\nText cannot be empty.");
        }
        if(m.find())
        {
            throw argExc.call("\nInput cannot contain any non-English characters.");
        }
        this.textCipher = text;
    }

     /**
     * Method setting the number of rails for the Rail Fence cipher, checking for its validity.
     * 
     * @param rails number of rails.
     * @throws IllegalArgumentException when the text is null or empty.
     * @throws pl.rapala.magdalena.railfence6.model.IncorrectNoOfRailsException when the value of rails is either too small or too great.
     */
    public void setNoOfRails(int rails) throws IllegalArgumentException, IncorrectNoOfRailsException{

        // lambda expression - V (IncorrectNoOfRailsException) is the class of E, which is a checked exception.
        // I could have used V as Exception class, but it is not necessary and would require many adjustments to be annotations and code in Controller class.
        // message is a string that will be shown with the exception.
        ExceptionLambda<IncorrectNoOfRailsException, IncorrectNoOfRailsException> railsExc = message -> new IncorrectNoOfRailsException(message);
        
        ExceptionLambda<RuntimeException, IllegalArgumentException> argExc = message -> new IllegalArgumentException(message);
        
        if(this.textCipher == null)
        {
            throw argExc.call("\nSetting the number of rails requires a set text, which is null.\n");
        }else if(this.textCipher.isEmpty())
        {
            throw argExc.call("\nSetting the number of rails requires a set text, which is empty.\n");
        }else if(rails > this.textCipher.length())
        {
            throw railsExc.call("\nNumber of rails is too great, it exceeds the length of the text."
                    + "\nLength of the text: "+this.textCipher.length()+"\nNumber of rails provided: "+rails+"\n");
        }else if(rails <= 0)
        {
            // using the lambda expression, call is a method from the functional interface ExceptionLambda
            throw railsExc.call("\nNumber of rails is too small, must be at least 1");
        }
        this.noOfRails = rails;
    }

     /**
     * Method managing the Rail Fence cipher according to the mode.
     * 
     * @return either decoded or encoded text.
     * @throws IllegalArgumentException when the mode is incorrect.
     * @throws pl.rapala.magdalena.railfence6.model.IncorrectNoOfRailsException when the value of rails is either too small or too great.
     */
    public String railFence() throws IllegalArgumentException, IncorrectNoOfRailsException
    {
        ExceptionLambda<RuntimeException, IllegalArgumentException> argExc = message -> new IllegalArgumentException(message);
        
        ExceptionLambda<IncorrectNoOfRailsException, IncorrectNoOfRailsException> railsExc = message -> new IncorrectNoOfRailsException(message);

        // The result of the cipher.
        String result = "";
                
        if(this.textCipher == null)
        {
            throw argExc.call("\nThe text should not be null.");
        }else if(this.textCipher.length()==0)
        {
            throw argExc.call("\nThe text should not be empty.");
        }
        
        // Primitive ints will default to 0 and cannot be null.
        if(this.noOfRails == 1)
        {
            // Making sure in case the later cipher functions do not get called, the history is updated anyway.
            LogEntry cipherEntry = new LogEntry(
                    "Cipher operation",
                    this.textCipher,
                    this.noOfRails,
                    this.modeSelect,
                    this.textCipher
            );

            addHistory(cipherEntry);
            return this.textCipher; // Nothing to encode/decode.
        }else if(this.noOfRails <= 0)
        {
            throw railsExc.call("\nThe number of rails should be positive.");
        }else if(this.noOfRails > this.textCipher.length())
        {
            throw railsExc.call("\nThe number of rails should not exceed the length of the text.");
        }
        
        if("en".equals(this.modeSelect))
        {
           result = encode();
        }else if("de".equals(this.modeSelect))
        {
            result = decode();
        }
        else
        {
            throw argExc.call("\nThe mode is incorrect."); // Checks for all.
        }
        
        // Updating the history.
        LogEntry cipherEntry = new LogEntry(
                "Cipher operation",
                this.textCipher,
                this.noOfRails,
                this.modeSelect,
                result
        );
        
        addHistory(cipherEntry);
        
        return result;
    }

     /**
     * Method encoding the text according to the number of rails.
     * 
     * @return encoded text.
     * @throws IllegalArgumentException when either the number of rails is incorrect or when the text is empty.       
     * @throws pl.rapala.magdalena.railfence6.model.IncorrectNoOfRailsException when the value of rails is either too small or too great.
     */
    private String encode() throws IllegalArgumentException, IncorrectNoOfRailsException
    {
        // Lambda expression - V (RuntimeException) is the parent class of E (IllegalArgumentException), which is an unchecked exception.
        // message is a string that will be shown with the exception.
        ExceptionLambda<RuntimeException, IllegalArgumentException> argExc = message -> new IllegalArgumentException(message);
        // Lambda expression - V (IncorrectNoOfRailsException) is the class of E, which is a checked exception.
        // I could have used V as Exception class, but it is not necessary and would require many adjustments to be annotations and code in Controller class.
        // message is a string that will be shown with the exception.
        ExceptionLambda<IncorrectNoOfRailsException, IncorrectNoOfRailsException> railsExc = message -> new IncorrectNoOfRailsException(message);

        if(this.noOfRails == 1)
        {
            return this.textCipher; // nothing to encode.
        }else if(this.noOfRails <= 0)
        {
            throw railsExc.call("\nThe number of rails should be positive.");
        }else if(this.noOfRails > this.textCipher.length())
        {
            throw railsExc.call("\nThe number of rails should not exceed the length of the text.");
        }
        if(this.textCipher.length()==0)
        {
            throw argExc.call("\nThe text should not be empty.");
        }
        if(this.textCipher == null)
        {
            throw argExc.call("\nThe text should not be null.");
        }else if(this.textCipher.length()==0)
        {
            throw argExc.call("\nThe text should not be empty.");
        }

        // The result of the cipher.
        // Instead of adding one element.
        List<StringBuilder> result = IntStream.range(0, 1)
                .mapToObj(i -> new StringBuilder())
                .collect(Collectors.toList());
        
        // rails - rows.
        // Stream instead of a for loop.
        List<StringBuilder> rails = IntStream.range(0, this.noOfRails)
                .mapToObj(i -> new StringBuilder())
                .collect(Collectors.toList());
        
        int y = 0;
        // goDown the rails.
        boolean goDown = true;
        
        // For each loop.
        for(char ch: textCipher.toCharArray()) // Translate the textCipher into a char array for the for each loop to work.
        {
            // Read the rows.
            // Get the index by using .get(idx).
            rails.get(y).append(ch);

            // zig-zag.
            if(y == 0)
                goDown = true;
            else if(y == this.noOfRails - 1)
                goDown = false;

            y += goDown ? 1 : -1;
        }
        
        // For each loop.
        // Append all the rails together to create the result.
        for(StringBuilder rail: rails)
            result.add(rail);

        // Instead of a simple return result.toString();.
        return result.stream() // stream.
             .map(StringBuilder::toString) // Converting all to StringBuilders.
             .reduce("", String::concat); // Reduce the stream of many results into one, starts with "", then it concatenates each string.
    }
    
     /**
     * Method decoding the text according to the number of rails.
     * 
     * @return decoded text.
     * @throws IllegalArgumentException when the text is empty.
     * @throws IncorrectNoOfRailsException when the number of rails is incorrect.
     */
    private String decode() throws IllegalArgumentException, IncorrectNoOfRailsException
    {
        // Lambda expression - V (RuntimeException) is the parent class of E (IllegalArgumentException), which is an unchecked exception.
        // message is a string that will be shown with the exception.
        ExceptionLambda<RuntimeException, IllegalArgumentException> argExc = message -> new IllegalArgumentException(message);
        // Lambda expression - V (IncorrectNoOfRailsException) is the class of E, which is a checked exception.
        // I could have used V as Exception class, but it is not necessary and would require many adjustments to be annotations and code in Controller class.
        // message is a string that will be shown with the exception.
        ExceptionLambda<IncorrectNoOfRailsException, IncorrectNoOfRailsException> railsExc = message -> new IncorrectNoOfRailsException(message);

        if(this.noOfRails == 1)
        {
            return this.textCipher; // Nothing to decode.
        }else if(this.noOfRails <= 0)
        {
            throw railsExc.call("\nThe number of rails should be positive.");
        }else if(this.noOfRails > this.textCipher.length())
        {
            throw railsExc.call("\nThe number of rails should not exceed the length of the text.");
        }
        if(this.textCipher.length()==0)
        {
            throw argExc.call("\nThe text should not be empty.");
        }
        
        // The result of the cipher.
        // Instead of adding one element.
        //type safe collection
        List<StringBuilder> result = IntStream.range(0, 1)
                .mapToObj(i -> new StringBuilder())
                .collect(Collectors.toList());
        
        // rails - rows
        // Instead of initialisation and a for loop.
        List<StringBuilder> rails = IntStream.range(0, this.noOfRails)
                .mapToObj(i -> new StringBuilder())
                .collect(Collectors.toList());

        int y = 0;
        // goDown the rails.
        boolean goDown = true;
        
        // Using the mapping to objects & collecting examples.
        // Instead of for loop.
        // Creating an int stream from 0 to noOfRails, filling it all with 0 and then "collecting" it all into a list.
        List<Integer> railLength = IntStream.range(0, this.noOfRails)
                .mapToObj(i -> 0)
                .collect(Collectors.toList());
        
        for(int x=0; x < this.textCipher.length(); x++)
        {
            railLength.set(y, railLength.get(y)+1);
            if(y == 0)
                goDown = true;
            else if(y == this.noOfRails - 1)
                goDown = false;

            y += goDown ? 1 : -1;
        }

        y = 0;
        
        // Cut the text into substrings appropriately for each rail (row).
        for(int i = 0; i< this.noOfRails; i++)
        {
            rails.get(i).append(this.textCipher.substring(y, y+ railLength.get(i)));
            y += railLength.get(i);
        }
        
        // The use of stream instead of initialisation and a for loop.
        List<Integer> railPosition = IntStream.range(0, this.noOfRails)
                .mapToObj(i -> 0)
                .collect(Collectors.toList());
        
        // goDown the rails.
        goDown = true;
        y = 0;
        
        // Append all the rails together via zig-zag to create the result.
        for(int i = 0; i < this.textCipher.length(); i++)
        {
            int pos = railPosition.get(y);
            result.get(0).append(rails.get(y).charAt(pos));
            
            railPosition.set(y, pos+1);
            // zig-zag.
            if(y == 0)
                goDown = true;
            else if(y == this.noOfRails - 1)
                goDown = false;

            y += goDown ? 1 : -1;
        }
        
        // Instead of a simple return result.toString();.
        return result.stream() // stream.
             .map(StringBuilder::toString) // Converting all to StringBuilders.
             .reduce("", String::concat); // Reduce the stream of many results into one, starts with "", then it concatenates each string.
    }
    
    /**
     * addHistory is a method for updating the {@link #actionHistory} list with the record of a new operation.
     * @param entry - the LogEntry record containing the data of the operation.
     * @throws IllegalArgumentException when the record is empty.
     */
    private void addHistory(LogEntry entry) throws IllegalArgumentException{
        if(entry == null)
        {
            // Lambda expression - V (RuntimeException) is the parent class of E (IllegalArgumentException), which is an unchecked exception.
            // message is a string that will be shown with the exception.
            ExceptionLambda<RuntimeException, IllegalArgumentException> argExc = message -> new IllegalArgumentException(message);
            throw argExc.call("\nThe record should not be empty.");
        }
        actionHistory.add(entry);
    }

}