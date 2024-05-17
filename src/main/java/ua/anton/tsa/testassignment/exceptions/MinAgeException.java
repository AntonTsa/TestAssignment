package ua.anton.tsa.testassignment.exceptions;

/**
 * Exception thrown, when the difference between current date and user's date of birth is less than "user.age.min" years
 */

public class MinAgeException extends Exception{
    public MinAgeException(String message) {
        super(message);
    }
}
