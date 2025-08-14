package main.java.exceptions;

/**
 * Thrown when an update to the hold fails (e.g., invalid slot name).
 */
public class HoldUpdateException extends RuntimeException {
    public HoldUpdateException(String message) {
        super(message);
    }
}
