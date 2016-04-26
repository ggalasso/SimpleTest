package com.ggalasso.BggCollectionManager.api;

import android.os.AsyncTask;
import android.util.Log;

import com.ggalasso.BggCollectionManager.controller.GameIdManager;
import com.ggalasso.BggCollectionManager.model.Foo;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Edward on 3/20/2016.
 */
public class XMLApi {
    String url;

    public XMLApi(){

    }

    public XMLApi(String url) {
        this.url = url;
    }

    public GameIdManager getIDManager() {
        // GameIdManager gim = GameIdManager.getInstance();
//        try {
            return GameIdManager.getInstance();//getIDList();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//         comenting this comment to update github
//        return null;
    }

    private <T> Foo<T> getIDList(Class<T> foo) throws ExecutionException, InterruptedException {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1";
        //String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=truthd&own=1";
        Log.i("BGCM-CAPI", "Attempting to download data from: " + downloadURL);
        AsyncTask<String, Void, Foo<T>> getGameIDTask = new getGameIDs().execute(downloadURL);
//        try {
            return new Foo<T>(); //getGameIDTask.get();
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    private class getGameIDs<T> extends AsyncTask<String, Void,  Foo<T>> {
        @Override
        protected Foo<T> doInBackground(String... params) {
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
            return null;
        }
    }
}

