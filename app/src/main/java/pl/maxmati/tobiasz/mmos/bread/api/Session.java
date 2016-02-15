package pl.maxmati.tobiasz.mmos.bread.api;

import pl.maxmati.tobiasz.mmos.bread.User;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;

/**
 * Created by mmos on 10.02.16.
 * @author mmos
 */
public class Session {
    private final User user;
    private final String sessionCookie;

    public Session(User user, String sessionCookie) throws SessionException {
        if(user == null || user.getName() == null || user.getPassword() == null)
            throw new SessionException("Session requires credentials");

        this.user = user;
        this.sessionCookie = sessionCookie;
    }

    public User getUser() {
        return user;
    }

    public String getSessionCookie() {
        return sessionCookie;
    }
}
