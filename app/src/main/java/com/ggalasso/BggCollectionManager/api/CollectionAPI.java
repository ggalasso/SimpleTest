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

/***
 * Created by Edward on 9/8/2015.
 *          making a change
 *          change number two
 */
public class CollectionAPI {

    public GameIdManager getIDManager() {
        // GameIdManager gim = GameIdManager.getInstance();
        try {
            return getIDList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private GameIdManager getIDList() throws ExecutionException, InterruptedException {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1";
        //String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=truthd&own=1";
        Log.i("BGCM-CAPI", "Attempting to download data from: " + downloadURL);
        AsyncTask<String, Void, GameIdManager> getGameIDTask = new getGameIDs().execute(downloadURL);
        try {
            return getGameIDTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getGameIDs extends AsyncTask<String, Void, GameIdManager> {
        @Override
        protected GameIdManager doInBackground(String... params) {
            GameIdManager manager = GameIdManager.getInstance();
            Log.i("BGCM-CAPI", "REACHED doInBackground");
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    manager = serializer.read(GameIdManager.class, is, false);

                    Log.i("BGCM-CAPI", "Finished Serializing");
                } finally {
                    con.disconnect();
                }
                Log.i("BGCM-CAPI", "Reached end of input stream.");

            } catch (Exception e) {
                Log.e("BGCM-CAPI", "Logging exception: " + e);
            }
            return manager;
        }
    }
}
