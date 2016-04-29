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

    public void getIDManager() {
//         GameIdManager gim = GameIdManager.getInstance();
//        try {
//            //return getIDList();
//            //return getIDList(new Foo<GameIdManager>() );
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////         comenting this comment to update github
//        return null;
    }

    /// HERE IS MORE DOCUMENTATION TO HELP US FIGURE THIS OUT
    // http://www.google.com/url?q=http%3A%2F%2Fstackoverflow.com%2Fquestions%2F5735320%2Fjava-generics-on-an-android-asynctask&sa=D&sntz=1&usg=AFQjCNGVZyeY3DG0ugtn1p0OJp9AfTHU5A
    private <T> GameIdManager getIDList() throws ExecutionException, InterruptedException {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1";
        //String downloadURL = "https://boardgamegeek.com/xmlapi2/collection?username=truthd&own=1";
        Log.i("BGCM-CAPI", "Attempting to download data from: " + downloadURL);
        AsyncTask<String, Void, GameIdManager> getGameIDTask = new getGameIDs<GameIdManager>(GameIdManager.class).execute(downloadURL);
        try {
            return getGameIDTask.get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 2014 apr 28 | Trying to get GameIDManager into this method Generically so we can
    //  do the same with BoardGameManager and use one method to populate both.
    //  Coordinate with SQLControllerTest of Generics with the SQLController class
    //  Thinking we need to wrap FOO around the Manager somehow and access the methods et. al.
    // ALSO: see if we can pass in just type private variable to the Simple Call currently "manager = serializer.read(params[0].class, is, false);" below
    private class getGameIDs<T> extends AsyncTask<String, Void,  T> {
        private Class<T> type;
        public getGameIDs(Class<T> classType) {
            type = classType;
        }

        @Override
        protected T doInBackground(String... manager) {

            //manager = GameIdManager.getInstance();
// NEED TO BETTER UNDERSTAND THIS, LOOKS LIKE I MIGHT BE LOOKING TO HAVE CLASS<T> as my PARAMETER
//            Log.i("BGCM-CAPI", "REACHED doInBackground");
//            try {
//                // be sure to address this hard code after a successful test
//                URL url = new URL("https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1");
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                try {
//                    InputStream is = new BufferedInputStream(con.getInputStream());
//                    Serializer serializer = new Persister();
//                    manager = serializer.read(params[0].class, is, false);
//
//                    Log.i("BGCM-CAPI", "Finished Serializing");
//                } finally {
//                    con.disconnect();
//                }
//                Log.i("BGCM-CAPI", "Reached end of input stream.");
//
//            } catch (Exception e) {
//                Log.e("BGCM-CAPI", "Logging exception: " + e);
//            }
            return null;
        }
    }
}

