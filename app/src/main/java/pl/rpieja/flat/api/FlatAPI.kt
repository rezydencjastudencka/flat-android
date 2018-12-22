package pl.rpieja.flat.api

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import pl.memleak.flat.*
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
    private val getGraphqlUrl = apiAddress + "graphql"

    private val apolloClient = ApolloClient.builder()
            .serverUrl(getGraphqlUrl)
            .okHttpClient(httpClient)
            .addCustomTypeAdapter(CustomType.DATETIME, dateCustomTypeAdapter)
            .addCustomTypeAdapter(CustomType.DATE, dateCustomTypeAdapter)
            .build()

    fun login(username: String, password: String): Single<Boolean> {
        val mutation = LoginMutation.builder()
                .username(username)
                .password(password)
                .build()

        return Rx2Apollo.from(apolloClient.mutate(mutation))
                .map { parseErrors(it) }
                .map { true }
                .onErrorReturnItem(false)
                .first(false)
    }

    fun validateSession(): Single<Boolean> {
        val query = MeQuery.builder().build()

        return Rx2Apollo.from(apolloClient.query(query))
                .map { parseErrors(it)}
                .map { true }
                .onErrorReturnItem(false)
                .first(false)
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

    private fun <T> parseErrors(resp: Response<T>) : Response<T> {
        if (resp.hasErrors()) {
            onUnauthorized()
            throw GraphqlException(resp.errors())
        } else {
            return resp
        }
    }

    fun fetchExpense(chargeId: Int): Maybe<Expense> {
        val query = ExpenseQuery.builder()
                .id(chargeId.toString())
                .build()

        return Rx2Apollo.from(apolloClient.query(query))
                .map { parseErrors(it) }
                .map { Expense(it.data()?.expense()!!) }
                .firstElement()
    }

    fun registerFCM(registrationToken: String) : Single<Boolean> {
        val mutation = RegisterDeviceMutation.builder()
                .token(registrationToken)
                .build()

        return Rx2Apollo.from(apolloClient.mutate(mutation))
                .map { parseErrors(it) }
                .map { true }
                .onErrorReturnItem(false)
                .first(false)
    }

    companion object {

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
