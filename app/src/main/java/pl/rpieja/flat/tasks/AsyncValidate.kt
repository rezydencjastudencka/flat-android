package pl.rpieja.flat.tasks

import android.os.AsyncTask

import java.io.IOException

import pl.rpieja.flat.api.FlatAPI


class AsyncValidate(private val flatAPI: FlatAPI, private val onSuccess: (Boolean) -> Unit)
    : AsyncTask<Void, Void, Boolean>() {

    override fun doInBackground(vararg voids: Void): Boolean? {
        return try {
            flatAPI.validateSession()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

    }

    override fun onPostExecute(result: Boolean?) {
        onSuccess(result!!)
    }
}
