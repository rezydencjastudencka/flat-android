package pl.rpieja.flat.authentication;

import android.content.Context;

import com.franmontiel.persistentcookiejar.persistence.SerializableCookie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by maxmati on 11/19/17.
 */

public class FlatCookieJar implements CookieJar {

    private final Set<Cookie> cookieStore = new LinkedHashSet<>();
    private static final String SESSIONID_NAME = "sessionid";

    public FlatCookieJar(Context context) {
        String authToken = AccountService.getAuthToken(context);
        if (authToken == null) return;
        cookieStore.add(new SerializableCookie().decode(authToken));
    }

    public String getSessionId() {
        for (Cookie c : cookieStore) {
            if (c.name().equals(SESSIONID_NAME))
                return new SerializableCookie().encode(c);
        }
        return null;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.addAll(cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> matchingCookies = new ArrayList<>();
        Iterator<Cookie> it = cookieStore.iterator();
        while (it.hasNext()) {
            Cookie cookie = it.next();
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                it.remove();
            } else if (cookie.matches(url)) {
                matchingCookies.add(cookie);
            }
        }
        return matchingCookies;
    }
}
