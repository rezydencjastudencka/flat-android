package pl.maxmati.tobiasz.mmos.bread.api;

/**
 * Created by mmos on 11.02.16.
 * @author mmos
 */
public class InvalidPasswordException extends SessionException {
    public InvalidPasswordException(Throwable throwable) {
        super(throwable);
    }
}
