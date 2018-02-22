package pl.maxmati.tobiasz.mmos.bread.api;

/**
 * Created by mmos on 08.03.16.
 *
 * @author mmos
 */
public class RestException extends Exception {
    public RestException() {
    }

    public RestException(String detailMessage) {
        super(detailMessage);
    }

    public RestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RestException(Throwable throwable) {
        super(throwable);
    }
}
