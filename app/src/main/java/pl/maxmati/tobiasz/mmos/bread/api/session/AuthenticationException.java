package pl.maxmati.tobiasz.mmos.bread.api.session;

/**
 * Created by mmos on 11.02.16.
 * @author mmos
 */
public class AuthenticationException extends SessionException {
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String detailMessage) {
        super(detailMessage);
    }

    public AuthenticationException(Throwable throwable) {
        super(throwable);
    }

    public AuthenticationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
