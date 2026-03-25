package pl.rapala.magdalena.railfence.model;

/**
* ExceptionLambda is a functional interface (has one method) used for creation of lambdas; It allows to throw any exceptions that extend Exception.
* 
* @param <V> type of exception to be returned and thrown;  here used for RuntimeException (IllegalArgumentException) and {@link pl.rapala.magdalena.railfence.model.IncorrectNoOfRailsException} custom exception (which extends Exception).
* @param <E> thrown exception constraint, allowing any Exception subclass. Since {@link pl.rapala.magdalena.railfence.model.IncorrectNoOfRailsException} extends Exception, which is a checked exception, compiler requires to declare the throw.
* @author Magdalena Rapala
* @version 1.0
*/
interface ExceptionLambda<V, E extends Exception>{
   /**
    * call method returns and throws an exception instance of type V with the message.
    * 
    * @param message the message to be conveyed with the thrown exception.
    * @return exception instance of type V.
    * @throws E exception constraint, any Exception subclass.
    */
   V call(String message) throws E;
} // Call is just a convention, because it can be called anything, but I am following the Callable<V> interface.