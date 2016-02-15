package pl.maxmati.tobiasz.mmos.bread;

/**
 * Created by mmos on 12.02.16.
 *
 * @author mmos
 */
public class User {
    private final String name;
    private final String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
