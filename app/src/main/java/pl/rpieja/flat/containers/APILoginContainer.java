package pl.rpieja.flat.containers;

import pl.rpieja.flat.api.FlatAPI;

/**
 * Created by radix on 10/30/17.
 */

public class APILoginContainer {
    private FlatAPI flatAPI;
    private String username, password;

    public APILoginContainer(FlatAPI flatAPI, String username, String password) {
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
