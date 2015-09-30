package com.ggalasso.simpletest.controller;

import android.util.Log;

import com.ggalasso.simpletest.model.GameID;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name="items")
public class GameIDManager {

    private static GameIDManager ourInstance = null;

    @ElementList(entry="item", inline=true, required=false)
    private ArrayList<GameID> GameIDs;

    private GameIDManager() { Log.i("INFO", "Instantiated GameID Manager"); }

    public static GameIDManager getInstance() {
        if (ourInstance == null) {
            synchronized (GameIDManager.class) {
                if (ourInstance == null) {
                    ourInstance = new GameIDManager();
                }
            }
        }
        return ourInstance;
    }

    public ArrayList<GameID> getGameIDs() {
        return GameIDs;
    }

    public String getIdListString() {
        String idList = "";
        for (GameID game : getGameIDs()) {
            if (idList == "") {
                idList = game.getObjectid();
            } else {
                idList += "," + game.getObjectid();
            }
        }
        return idList;
    }
 /*
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
    }*/

}
