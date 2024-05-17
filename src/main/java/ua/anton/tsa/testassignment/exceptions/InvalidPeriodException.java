package ua.anton.tsa.testassignment.exceptions;

/**
 * Exception to thrown by in case of invalid period [from, to]
 */
public class InvalidPeriodException extends Exception{
    public InvalidPeriodException(String message) {
        super(message);
    }
}
