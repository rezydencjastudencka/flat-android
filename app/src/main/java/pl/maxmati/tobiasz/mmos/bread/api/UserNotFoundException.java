package pl.maxmati.tobiasz.mmos.bread.api;

/**
 * Created by mmos on 11.02.16.
 * @author mmos
 */
public class UserNotFoundException extends SessionException {
    public UserNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
