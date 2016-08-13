package pl.maxmati.tobiasz.mmos.bread.api.session;

/**
 * Created by mmos on 14.02.16.
 *
 * @author mmos
 */
public class InvalidCredentialsException extends SessionException {
    public InvalidCredentialsException() {
    }

    public InvalidCredentialsException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidCredentialsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidCredentialsException(Throwable throwable) {
        super(throwable);
    }
}
