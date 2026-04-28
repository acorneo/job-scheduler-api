package me.acorneo.jobschedulerapi.exception;

/**
 * This exception should be thrown when service is not available for any user. For example, when database is down.
 */
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
