package pl.rpieja.flat.tasks

import android.os.AsyncTask
import pl.rpieja.flat.api.NoInternetConnectionException
import pl.rpieja.flat.api.UnauthorizedException
import java.io.IOException


open class AsyncRequest<Result>(
        private val onSuccess: (Result) -> Unit,
        private val unauthorized: () -> Unit,
        private val process: () -> Result,
        private val neutral: Result)
    : AsyncTask<Void, Void, Result>() {

    override fun doInBackground(vararg p0: Void?): Result {
        try {
            return process()
        } catch (e: NoInternetConnectionException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UnauthorizedException) {
            unauthorized()
        }
        return neutral
    }

    override fun onPostExecute(result: Result) {
        onSuccess(result)
    }
}