package pl.rpieja.flat.api;

/**
 * Created by radix on 10/30/17.
 */

public class APIContainer {
    private FlatAPI flatAPI;
    private String username, password;

    public APIContainer(FlatAPI flatAPI, String username, String password) {
        this.flatAPI = flatAPI;
        this.username = username;
        this.password = password;
    }

    public FlatAPI getFlatAPI() {
        return flatAPI;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
