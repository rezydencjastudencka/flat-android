package pl.maxmati.tobiasz.mmos.bread.api.session;

/**
 * Created by mmos on 15.02.16.
 *
 * @author mmos
 */
public class SessionExpiredException extends SessionException {
    public SessionExpiredException() {
    }

    public SessionExpiredException(String detailMessage) {
        super(detailMessage);
    }

    public SessionExpiredException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SessionExpiredException(Throwable throwable) {
        super(throwable);
    }
}
