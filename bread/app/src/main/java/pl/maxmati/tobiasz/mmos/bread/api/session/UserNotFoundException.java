package pl.maxmati.tobiasz.mmos.bread.api.session;

/**
 * Created by mmos on 11.02.16.
 * @author mmos
 */
public class UserNotFoundException extends InvalidCredentialsException {
    public UserNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
