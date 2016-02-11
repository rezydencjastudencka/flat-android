package pl.maxmati.tobiasz.mmos.bread.api;

/**
 * Created by mmos on 11.02.16.
 * @author mmos
 */
public class SessionException extends Exception {
    public SessionException() {
        super();
    }

    public SessionException(String detailMessage) {
        super(detailMessage);
    }

    public SessionException(Throwable throwable) {
        super(throwable);
    }

    public SessionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
