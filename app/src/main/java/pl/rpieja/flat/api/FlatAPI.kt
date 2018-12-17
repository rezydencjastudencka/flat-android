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
import pl.memleak.flat.NewRevenueMutation
import pl.memleak.flat.TransfersQuery
import pl.memleak.flat.UsersQuery
import pl.memleak.flat.type.CustomType
import pl.rpieja.flat.R
import pl.rpieja.flat.authentication.AccountService
import pl.rpieja.flat.authentication.FlatCookieJar
import pl.rpieja.flat.dto.*
import pl.rpieja.flat.util.IsoTimeFormatter
import java.util.*


class FlatAPI private constructor(context: Context, cookieJar: CookieJar) {
    private val httpClient: OkHttpClient = OkHttpClient
            .Builder()
            .cookieJar(cookieJar)
            .build()

    private val gson = Gson()

    private val dateCustomTypeAdapter = object : CustomTypeAdapter<Date> {
        override fun decode(value: CustomTypeValue<*>): Date {
            return IsoTimeFormatter.fromGraphqlDate(value.value.toString())

        }

        override fun encode(value: Date): CustomTypeValue<*> {
            return CustomTypeValue.GraphQLString(IsoTimeFormatter.toGraphqlDate(value))
        }
    }

    private val onUnauthorized = { AccountService.removeCurrentAccount(context) }

    private val apiAddress = context.getString(R.string.api_uri)
    private val sessionCheckUrl = apiAddress + "session/check"
    private val createSessionUrl = apiAddress + "session/create"
    private val getGraphqlUrl = apiAddress + "graphql"
    private val fetchExpenseUrl = apiAddress + "charge/expense/"
    private val registerFCMUrl = apiAddress + "fcm/device"

    private val apolloClient = ApolloClient.builder()
            .serverUrl(getGraphqlUrl)
            .okHttpClient(httpClient)
            .addCustomTypeAdapter(CustomType.DATETIME, dateCustomTypeAdapter)
            .addCustomTypeAdapter(CustomType.DATE, dateCustomTypeAdapter)
            .build()

    fun login(username: String, password: String): Boolean {
        //TODO: use Gson
        val json = "{\"name\":\"$username\", \"password\": \"$password\"}"

        val request = Request.Builder()
                .url(createSessionUrl)
                .post(RequestBody.create(JSON_MEDIA_TYPE, json))
                .build()
        val response = httpClient.newCall(request).execute()
        return response.isSuccessful
    }

    fun validateSession(): Boolean? {
        val request = Request.Builder()
                .url(sessionCheckUrl)
                .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return false
        val (error) = gson.fromJson(response.body()!!.string(), SessionCheckResponse::class.java)
                ?: return false
        return "ok" == error
    }

    fun fetchCharges(month: Int, year: Int): Observable<ChargesDTO> {
        val query = ChargesQuery.builder()
                .month(month)
                .year(year)
                .build()
        return Rx2Apollo.from(apolloClient.query(query))
                .map { parseErrors(it) }
                .map { ChargesDTO(it.data()!!) }
    }

    fun fetchTransfers(month: Int, year: Int): Observable<TransfersDTO> {
        val query = TransfersQuery.builder()
                .month(month)
                .year(year)
                .build()
        return Rx2Apollo.from(apolloClient.query(query))
                .map { parseErrors(it) }
                .map { TransfersDTO(it.data()!!) }
    }

    fun fetchUsers(): Observable<List<User>> {
        val query = UsersQuery.builder().build()
        return Rx2Apollo.from(apolloClient.query(query))
                .map { parseErrors(it) }
                .map { it.data()?.users()?.map { User(it) }.orEmpty() }
    }

    fun createRevenue(revenue: CreateRevenueDTO): Observable<Revenue> {
        val mutation = NewRevenueMutation.builder()
                .amount(revenue.rawAmount)
                .date(revenue.date)
                .name(revenue.name)
                .to(revenue.to)
                .build()

        return Rx2Apollo.from(apolloClient.mutate(mutation))
                .map { parseErrors(it) }
                .map { Revenue(it.data()?.addRevenue()!!) }
    }

    private fun <T> parseErrors(resp: com.apollographql.apollo.api.Response<T>)
            : com.apollographql.apollo.api.Response<T> {
        if (resp.hasErrors()) {
            onUnauthorized()
            throw GraphqlException(resp.errors())
        } else {
            return resp
        }
    }

    fun fetchExpense(charge_id: Int): Expense {
        val requestUrl = fetchExpenseUrl + charge_id.toString()
        return fetch(requestUrl, Expense::class.java)
    }

    fun registerFCM(registration_token: String){
        post(registerFCMUrl, RegisterFCM(registration_token))
    }

    private fun <T> post(url: String, data: T): Response {
        return method("POST", url, data)
    }

    private fun <T> method(methodName: String, url: String, data: T): Response {
        val json = gson.toJson(data)
        val request = Request.Builder()
                .url(url)
                .method(methodName, RequestBody.create(JSON_MEDIA_TYPE, json))
                .build()

        Log.d(TAG, String.format("Sending %s %s with data %s", methodName, url, json))
        val response = httpClient.newCall(request).execute()
        if (response.code() == 403) throw UnauthorizedException()
        if (!response.isSuccessful) throw FlatApiException()
        return response
    }

    private fun <T> fetch(requestUrl: String, tClass: Class<T>): T {
        val request = Request.Builder()
                .url(requestUrl)
                .build()

        val response = httpClient.newCall(request).execute()
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
                flatAPI = FlatAPI(context, getCookieJar(context))
            }

            return flatAPI!!
        }

        fun getCookieJar(context: Context): FlatCookieJar {
            if (cookieJar == null) {
                cookieJar = FlatCookieJar(context)
            }

            return cookieJar!!
        }

        fun reset(){
            flatAPI = null
        }

        private var flatAPI: FlatAPI? = null
        private var cookieJar: FlatCookieJar? = null
    }
}
