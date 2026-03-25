package pl.rapala.magdalena.railfence6.model;

/**
 * LogEntry record is a record that will hold all history.
 * @param action The action performed ex. Cipher.
 * @param inputText The text that was provided by the user.
 * @param rails The number of rails for the cipher.
 * @param mode The mode of the cipher, either Encoding or Decoding.
 * @param result The result of the Cipher.
 * @author Magdalena Rapala
 * @version 1.0
 */
public record LogEntry(
    /**
     * action is the summary of the performed action.
     */
    String action,
     /**
     * inputText is the text that was provided by the user.
     */
    String inputText,
    /**
     * rails is the number of rails provided by the user.
     */
    int rails,
    /**
     * mode is either Encoding or Decoding as selected by the user.
     */
    String mode,
    /**
     * result is the result of the cipher.
     */
    String result
) {}
