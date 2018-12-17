package pl.rpieja.flat.api

import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.apollographql.apollo.rx2.Rx2Apollo
import com.google.gson.Gson
import com.google.gson.JsonIOException
import io.reactivex.Observable
import okhttp3.*
import pl.memleak.flat.ChargesQuery
import pl.memleak.flat.type.CustomType
import pl.rpieja.flat.R
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.dto.*
import pl.rpieja.flat.util.IsoTimeFormatter
import java.util.*


class FlatAPI(context: Context, cookieJar: CookieJar) {
    private val client: OkHttpClient = OkHttpClient.Builder().cookieJar(cookieJar).build()
    private val gson = Gson()

    private val apiAddress = context.getString(R.string.api_uri)
    private val sessionCheckUrl = apiAddress + "session/check"
    private val createSessionUrl = apiAddress + "session/create"
    private val getChargesUrl = apiAddress + "charge/"
    private val getGraphqlUrl = apiAddress + "graphql"
    private val getTransfersUrl = apiAddress + "transfer/"
    private val createRevenueUrl = apiAddress + "charge/create"
    private val fetchExpenseUrl = apiAddress + "charge/expense/"
    private val getUsersUrl = apiAddress + "user/"
    private val registerFCMUrl = apiAddress + "fcm/device"

    fun login(username: String, password: String): Boolean {
        //TODO: use Gson
        val json = "{\"name\":\"$username\", \"password\": \"$password\"}"

        val request = Request.Builder()
                .url(createSessionUrl)
                .post(RequestBody.create(JSON_MEDIA_TYPE, json))
                .build()
        val response = client.newCall(request).execute()
        return response.isSuccessful
    }

    fun validateSession(): Boolean? {
        val request = Request.Builder()
                .url(sessionCheckUrl)
                .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) return false
        val (error) = gson.fromJson(response.body()!!.string(), SessionCheckResponse::class.java)
                ?: return false
        return "ok" == error
    }

    fun fetchCharges(month: Int, year: Int):
            Observable<com.apollographql.apollo.api.Response<ChargesQuery.Data>> {
        val dateCustomTypeAdapter = object : CustomTypeAdapter<Date> {
            override fun decode(value: CustomTypeValue<*>): Date {
                return IsoTimeFormatter.fromGraphqlDate(value.value.toString())

            }

            override fun encode(value: Date): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(IsoTimeFormatter.toGraphqlDate(value))
            }
        }
        val apolloClient = ApolloClient.builder()
                .serverUrl(getGraphqlUrl)
                .okHttpClient(client)
                .addCustomTypeAdapter(CustomType.DATETIME, dateCustomTypeAdapter)
                .build()

        val query = ChargesQuery.builder()
                .month(month)
                .year(year)
                .build()
        return Rx2Apollo.from(apolloClient.query(query))
    }

    fun fetchExpense(charge_id: Int): Expense {
        val requestUrl = fetchExpenseUrl + charge_id.toString()
        return fetch(requestUrl, Expense::class.java)
    }

    fun registerFCM(registration_token: String){
        post(registerFCMUrl, RegisterFCM(registration_token))
    }

    fun fetchTransfers(month: Int, year: Int): TransfersDTO {
        val requestUrl = getTransfersUrl + Integer.toString(year) + "/" + Integer.toString(month)
        return fetch(requestUrl, TransfersDTO::class.java)
    }

    fun fetchUsers(): List<User> {
        return Arrays.asList(*fetch(getUsersUrl, Array<User>::class.java))
    }

    private fun <T> createEntity(entity: CreateDTO<T>, entityUrl: String): T {
        val response = post(entityUrl, entity)
        return gson.fromJson(response.body()!!.charStream(), entity.entityClass)
    }

    fun createRevenue(revenue: CreateRevenueDTO): Revenue {
        return createEntity(revenue, createRevenueUrl)
    }

    private fun <T> post(url: String, data: T): Response {
        return method("POST", url, data)
    }

    private fun <T> put(url: String, data: T) {
        method("PUT", url, data)
    }

    private fun <T> method(methodName: String, url: String, data: T): Response {
        val json = gson.toJson(data)
        val request = Request.Builder()
                .url(url)
                .method(methodName, RequestBody.create(JSON_MEDIA_TYPE, json))
                .build()

        Log.d(TAG, String.format("Sending %s %s with data %s", methodName, url, json))
        val response = client.newCall(request).execute()
        if (response.code() == 403) throw UnauthorizedException()
        if (!response.isSuccessful) throw FlatApiException()
        return response
    }

    private fun <T> fetch(requestUrl: String, tClass: Class<T>): T {
        val request = Request.Builder()
                .url(requestUrl)
                .build()

        val response = client.newCall(request).execute()
        if (response.code() == 403) throw UnauthorizedException()
        if (!response.isSuccessful) throw FlatApiException()

        return gson.fromJson(response.body()!!.string(), tClass)
                ?: throw JsonIOException("JSON parsing exception")
    }

    companion object {
        private val JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8")
        private val TAG = FlatAPI::class.java.simpleName

        fun getFlatApi(context: Context): FlatAPI {
            if (flatAPI == null) {
                flatAPI = FlatAPI(context, FlatCookieJar(context))
            }

            return flatAPI!!
        }

        fun reset(){
            flatAPI = null
        }

        private var flatAPI: FlatAPI? = null
    }
}
