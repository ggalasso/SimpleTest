package com.ggalasso.BggCollectionManager.api;

import android.os.AsyncTask;
import android.util.Log;

import com.ggalasso.BggCollectionManager.controller.GameIdManager;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Edward on 3/20/2016.
 */
public class XMLApi<T> {

    private Class<T> genericType;
    private String url;

    public XMLApi(Class<T> classType, String url) {
        this.genericType = classType;
        this.url = url;
    }

    public <T> T getAPIManager() {
        try {
            return callAPI();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /// HERE IS MORE DOCUMENTATION TO HELP US FIGURE THIS OUT
    // http://www.google.com/url?q=http%3A%2F%2Fstackoverflow.com%2Fquestions%2F5735320%2Fjava-generics-on-an-android-asynctask&sa=D&sntz=1&usg=AFQjCNGVZyeY3DG0ugtn1p0OJp9AfTHU5A
    private <T> T callAPI() throws ExecutionException, InterruptedException {
        Log.i("BGCM-CAPI", "Attempting to download data from: " + url);
        AsyncTask<String, Void, T> getGameIDTask = new getAPIResponse<T>((Class<T>) genericType).execute(url);
        try {
            T result = getGameIDTask.get();
            return result;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getAPIResponse<T> extends AsyncTask<String, Void,  T> {

        private Class<T> type;

        public getAPIResponse(Class<T> classType) {
            type = classType;
        }

        @Override
        protected T doInBackground(String... params) {
            Log.i("BGCM-XAPI", "REACHED doInBackground with class type: " + type);

            T manager = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    manager = serializer.read(type, is, false);

                    Log.i("BGCM-CAPI", "Finished Serializing");
                } finally {
                    con.disconnect();
                }
                Log.i("BGCM-XAPI", "Reached end of input stream.");

            } catch (Exception e) {
                Log.e("BGCM-XAPI", "Logging exception: " + e);
            }
            return manager;
        }
    }
}

