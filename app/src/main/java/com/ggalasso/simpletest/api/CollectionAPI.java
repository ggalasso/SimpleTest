package com.ggalasso.simpletest.api;

import android.os.AsyncTask;
import android.util.Log;

import com.ggalasso.simpletest.controller.GameIDManager;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Edward on 9/8/2015.
 */
public class CollectionAPI {

    public GameIDManager getIDList() throws ExecutionException, InterruptedException {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1";
        //String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=truthd&own=1";
        Log.i("INFO", "Attempting to download data from: " + downloadURL);
        AsyncTask<String, Void, GameIDManager> getGameIDTask = new getGameIDs().execute(downloadURL);
        try {
            return getGameIDTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getGameIDs extends AsyncTask<String, Void, GameIDManager> {
        @Override
        protected GameIDManager doInBackground(String... params) {
            GameIDManager manager = GameIDManager.getInstance();
            Log.i("INFO", "REACHED doInBackground");
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    manager = serializer.read(GameIDManager.class, is, false);

                    Log.i("EXCEPTION", "Finished Serializing");
                } finally {
                    con.disconnect();
                }
                Log.i("INFO", "Reached end of input stream.");

            } catch (Exception e) {
                Log.e("EXCEPTION", "Logging exception: " + e);
            }
            return manager;
        }
    }
}
