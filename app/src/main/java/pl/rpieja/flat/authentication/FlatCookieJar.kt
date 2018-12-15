package pl.rpieja.flat.authentication

import android.content.Context

import com.franmontiel.persistentcookiejar.persistence.SerializableCookie

import java.util.ArrayList
import java.util.LinkedHashSet

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class FlatCookieJar(context: Context) : CookieJar {

    private val cookieStore = LinkedHashSet<Cookie>()

    val sessionId: String?
        get() {
            for (c in cookieStore) {
                if (c.name() == SESSIONID_NAME)
                    return SerializableCookie().encode(c)
            }
            return null
        }

    init {
        val authToken = AccountService.getAuthToken(context)
        if (authToken != null)
            cookieStore.add(SerializableCookie().decode(authToken))
    }

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.addAll(cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val matchingCookies = ArrayList<Cookie>()
        val it = cookieStore.iterator()
        while (it.hasNext()) {
            val cookie = it.next()
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                it.remove()
            } else if (cookie.matches(url)) {
                matchingCookies.add(cookie)
            }
        }
        return matchingCookies
    }

    companion object {
        private const val SESSIONID_NAME = "sessionid"
    }
}
