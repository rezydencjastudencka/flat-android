package pl.maxmati.tobiasz.flat.api.session;

/**
 * Created by mmos on 10.02.16.
 * @author mmos
 */
public class Session {
    private final String sessionCookie;

    public Session(String sessionCookie) throws SessionException {
        if(sessionCookie == null)
            throw new SessionException("Session requires session cookie");
        this.sessionCookie = sessionCookie;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }
}
