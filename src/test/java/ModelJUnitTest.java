import pl.rapala.magdalena.railfence.model.Model;
import pl.rapala.magdalena.railfence.model.IncorrectNoOfRailsException;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * ModelJUnitTest is a class consisting of multiple test methods for the Model class.
 * 
 * @author Magdalena Rapala
 * @version 1.1
 */
public class ModelJUnitTest {
    
    /**
     * model is an instance of {@link pl.rapala.magdalena.railfence.model.Model} class, whose public methods will be tested here.
     */
    private Model model;
    
    /**
     * setUp method is a method that initiates the new Model instance before each test.
     */
    @BeforeEach
    public void setUp() {
        model = new Model();
    }
    
    /**
     * ModelJUnitTest is an empty constructor.
     */
    public ModelJUnitTest() {
        //empty
    }
 
    /**
     * testSetModeCorrect is a method that tests whether the method {@link pl.rapala.magdalena.railfence.model.Model#setMode(java.lang.String)} method correctly converts the input mode into lowercase mode.
     * 
     * @param input the string mode value provided by the user.
     * @param expected the correct mode value.
     */
    @ParameterizedTest
    @CsvSource({"en,en", "En,en", "eN,en", "EN,en", "de,de", "De, de", "dE,de", "DE,de",  "en ,en", "de ,de", " en,en", " de,de"})
    public void testSetModeCorrect(String input, String expected){
        try{
            model.setMode(input);
            assertEquals(expected, model.getModeSelect(), "The mode '"+input+"' is set regardless of its case or spaces, correct.");
        }catch(IllegalArgumentException e)
        {
            fail("The correct mode '"+input+"' was not set properly, fails.");
        }
    }
 
    /**
     * testSetModeIncorrect is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#setMode(java.lang.String)} for incorrect option.
     * 
     * @param input the incorrect value which differs from the accepted 'en', 'de' and all lowercase|uppercase permutations of it.
     */
    @ParameterizedTest
    @CsvSource({"MODE", "Mode", "enn","dee", "e n", "d e"})
    @ValueSource(strings = {" ", " ", "\t", "\n"})
    public void testSetModeIncorrect(String input)
    {
        try{
            model.setMode(input);
            fail("This value '"+input+"' should not have been accepted, fails.");
        }catch(IllegalArgumentException e)
        {
            assert true: "The value '"+input+"' which is different than accepted modes was rejected, correct.";
        }
    }
    
    /**
     * testSetTextCorrect is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#setText(java.lang.String)}.
     * 
     * @param input the correct input text.
     */
    @ParameterizedTest
    @CsvSource({"gliwice","Katowice","Ala ma kota", "  Ala   ", "Hej. Kotku, gdzie jestes?!","02.05.1989", "'.,?!'"}) //the interpunction is acceptable, falls within the correct signs.
    public void testSetTextCorrect(String input){
        try{
            model.setText(input);
            assert true: "This correct input '"+input+"' text was accepted, correct.";
        }catch(IllegalArgumentException e)
        {
            fail("This text '"+input+"' should have been accepted, fails.");
        }
    }
    
    /**
     * testSetTextIncorrect is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#setText(java.lang.String)}.
     * 
     * @param input the incorrect input text.
     */
    @ParameterizedTest
    @CsvSource({"Będzin", "/","_","\\",")","(","*","&","^","%","$","#"})
    @ValueSource(strings = {" ", "", "\t", "\n"})
    public void testSetTextIncorrect(String input){
        try{
            model.setText(input);
            fail("This text '"+input+"' should not have been accepted, fails.");
        }catch(IllegalArgumentException e)
        {
            assert true: "This wrong input text '"+input+"' was not accepted, correct.";
        }
    }
    
    /**
     * testSetNoOfRailsCorrect is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#setNoOfRails(int)}.
     * 
     * @param inputText the correct input text.
     * @param inputRails the correct number of rails.
     */
    @ParameterizedTest
    @CsvSource({"Ala ma kota,2", "Ala ma kota,11", "Ala ma kota,1"})
    public void testSetNoOfRailsCorrect(String inputText, int inputRails)
    {
        try{
            model.setText(inputText);
            model.setNoOfRails(inputRails);assert true: "The number of rails is lower or equal than the length of the string, correct.";
        }catch(IncorrectNoOfRailsException e)
        {
            fail("The number of rails was incorrect even though the length of the text was greater or equal.");
        }
    }
   
    /**
     * testSetNoOfRailsIncorrect is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#setNoOfRails(int)}.
     * 
     * @param inputText the incorrect input text.
     * @param inputRails the incorrect number of rails.
     */
    @ParameterizedTest
    @CsvSource({"Ala ma kota,0", "Ala ma kota,-1", "Ala ma kota,200","Ala ma kota,-0", "Ala ma kota,12"}) //11 + 1 boundary 
    public void testSetNoOfRailsIncorrect(String inputText, int inputRails)
    {
        try{
            model.setText(inputText);
            model.setNoOfRails(inputRails);
            fail("The number of rails is '"+inputRails+"', which is incorrect, it does not match the inputText length size, which is: "+inputText.length()+", fails.");
        }catch(IncorrectNoOfRailsException e)
        {
            assert true: "The number of rails is not supposed to be larger or smaller than the length of the input text, correct.";
        }
    }
    
     /**
     * testSetNoOfRailsNoText is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#setNoOfRails(int)}.
     * 
     * @param inputRails the number of rails, that cannot be set without the text.
     */
    @ParameterizedTest
    @CsvSource({"0", "1", "200", "12"}) 
    public void testSetNoOfRailsNoText(int inputRails)
    {
        try{
            model.setNoOfRails(inputRails);
            fail("The number of rails cannot be set without the text, fails.");
        }catch(IllegalArgumentException | IncorrectNoOfRailsException e)
        {
            assert true: "The number of rails can only be set once the text is set as well, correct.";
        }
    }
    
    /**
     * testRailFenceCorrect is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#railFence()} and its later called methods for encryption and decryption called {@link pl.rapala.magdalena.railfence.model.Model#encode()} and {@link pl.rapala.magdalena.railfence.model.Model#decode()}.
     * 
     * @param mode the correct mode (Encoding, Decoding) for the cipher.
     * @param inputText the correct input text.
     * @param inputRails the correct number of rails.
     * @param expectedResult the expected result.
     */
    @ParameterizedTest
    @CsvSource({"en,Ala ma kota,2,Aam oal akt","en,Ala ma kota,1,Ala ma kota","de,Aam oal akt,2,Ala ma kota","de,Aam oal akt,1,Aam oal akt", "en,okay,2,oaky", "de,oaky,2,okay", "en,ok,2,ok", "de,ok,2,ok", "en,a,1,a", "de,a,1,a"})
    public void testRailFenceCorrect(String mode, String inputText, int inputRails, String expectedResult){

        try{
            model.setMode(mode);
            model.setText(inputText);
            model.setNoOfRails(inputRails);
            
            assertEquals(model.railFence(), expectedResult, "The texts should equal. Correct");
        }catch(IllegalArgumentException | IncorrectNoOfRailsException e)
        {
            fail("The cipher failed.");
        }

    }
    
    /**
     * testRailFenceIncorrect is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#railFence()} and its later called methods for encryption and decryption called {@link pl.rapala.magdalena.railfence.model.Model#encode()} and {@link pl.rapala.magdalena.railfence.model.Model#decode()}.
     * 
     * @param mode the correct mode (Encoding, Decoding) for the cipher.
     * @param inputText the correct input text.
     * @param inputRails the correct number of rails.
     * @param wrongResult the incorrect result of the cipher, should not be equal to the inputText.
     */
    @ParameterizedTest
    @CsvSource({"en,Ala ma kota,2,Ala ma kota","en,Ala ma kota,1,Aam oal akt","de,Aam oal akt,1,Ala ma kota","de,Aam oal akt,2,Aam oal akt"})
    public void testRailFenceIncorrect(String mode, String inputText, int inputRails, String wrongResult){

        try{
            model.setMode(mode);
            model.setText(inputText);
            model.setNoOfRails(inputRails);
            
            assertNotEquals(model.railFence(), wrongResult, "The texts should not equal. Correct");
        }catch(IllegalArgumentException | IncorrectNoOfRailsException e)
        {
            fail("The cipher failed.");
        }

    }
    
    /**
     * testRailFenceNoMode is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#railFence()} without mode set.
     * 
     * @param inputText the correct input text.
     * @param inputRails the correct number of rails for the cipher.
     */
    @ParameterizedTest
    @CsvSource({"Ala ma kota,2","Aam oal akt,2"})
    public void testRailFenceNoMode(String inputText, int inputRails)
    {
        try{
            model.setText(inputText);
            model.setNoOfRails(inputRails);
            
            model.railFence();
            fail("The lack of set mode should have thrown an exception, fails.");
        }catch(IllegalArgumentException | IncorrectNoOfRailsException e)
        {
            assert true: "The lack of set mode has thrown an exception, correct.";
        }
    }
    
    
    /**
     * testRailFenceNoModeOneRail is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#railFence()} without mode set, but with one rail.
     * 
     * @param inputText the correct input text for the cipher.
     * @param inputRails the correct number of rails for the cipher.
     * @param expectedResult the expected result, which should equal to the inputText.
     */
    @ParameterizedTest
    @CsvSource({"Ala ma kota,1,Ala ma kota","Aam oal akt,1,Aam oal akt"})
    public void testRailFenceNoModeOneRail(String inputText, int inputRails, String expectedResult)
    {
        try{
            model.setText(inputText);
            model.setNoOfRails(inputRails);
            
            model.railFence();
            assertEquals(model.railFence(), expectedResult, "The lack of selected mode does not stop the method from returning the untouched text, since the number of rails is 1. Correct");
        }catch(IllegalArgumentException | IncorrectNoOfRailsException e)
        {
            fail("As the lack of mode for the cipher is checked later than the number of rails, the cipher should return an untouched text, fails.");
        }
    }
    
    /**
     * testRailFenceNoRails is a test method that tests the method {@link pl.rapala.magdalena.railfence.model.Model#railFence()} without rails set.
     * 
     * @param mode the correct mode for the cipher.
     * @param inputText the correct input text for the cipher.
     */
    @ParameterizedTest
    @CsvSource({"en,Ala ma kota","de,Aam oal akt"})
    public void testRailFenceNoRails(String mode, String inputText)
    {
        try{
            model.setMode(mode);
            model.setText(inputText);
            
            model.railFence();
            fail("The lack of set noOfRails should have thrown an exception, fails.");
        }catch(IllegalArgumentException | IncorrectNoOfRailsException e)
        {
            assert true: "The lack of set noOfRails has thrown an exception, correct.";
        }
    }
    
}