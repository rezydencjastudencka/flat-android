package pl.rpieja.flat.api

import com.apollographql.apollo.api.Error

class GraphqlException(val errors: MutableList<Error>) : FlatApiException()
