package pl.maxmati.tobiasz.mmos.bread.api.session;

/**
 * Created by mmos on 14.02.16.
 *
 * @author mmos
 */
public class SessionException extends Exception {
    public SessionException() {
    }

    public SessionException(String detailMessage) {
        super(detailMessage);
    }

    public SessionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SessionException(Throwable throwable) {
        super(throwable);
    }
}
