package pl.maxmati.tobiasz.flat.activity;

import android.os.AsyncTask;

/**
 * Created by mmos on 21.02.16.
 *
 * @author mmos
 */
public abstract class ProgressBarAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress,
        Result> {
    protected final ProgressBarActivity progressBarActivity;

    public ProgressBarAsyncTask(ProgressBarActivity progressBarActivity) {
        this.progressBarActivity = progressBarActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarActivity.showProgress(true);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        progressBarActivity.showProgress(false);
    }
}
