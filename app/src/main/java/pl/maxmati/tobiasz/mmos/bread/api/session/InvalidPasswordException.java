package pl.maxmati.tobiasz.mmos.bread.api.session;

/**
 * Created by mmos on 11.02.16.
 * @author mmos
 */
public class InvalidPasswordException extends AuthenticationException {
    public InvalidPasswordException(Throwable throwable) {
        super(throwable);
    }
}
