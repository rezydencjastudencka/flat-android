package pl.maxmati.tobiasz.flat.api.session;

/**
 * Created by mmos on 11.02.16.
 * @author mmos
 */
public class InvalidPasswordException extends InvalidCredentialsException {
    public InvalidPasswordException(Throwable throwable) {
        super(throwable);
    }
}
