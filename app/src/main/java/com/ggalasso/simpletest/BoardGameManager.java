package com.ggalasso.simpletest;

import android.os.AsyncTask;
import android.util.Log;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Root(name="items")
public class BoardGameManager {

    private static BoardGameManager ourInstance = null;
    @ElementList(entry="item", inline=true)
    private ArrayList<BoardGame> BoardGames;

    private BoardGameManager() { Log.i("INFO", "Instantiated BoardGameManager"); }

    public static BoardGameManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new BoardGameManager();
        }
        return ourInstance;
    }

    public ArrayList<BoardGame> getBoardGames() {
        return BoardGames;
    }

    public String getIdListString() {
        String idList = "";
        for (BoardGame game : getBoardGames()) {
            if (idList == "") {
                idList = game.getId();
            } else {
                idList += "," + game.getId();
            }
        }
        return idList;
    }

    public BoardGameManager getDetail(String queryString) throws ExecutionException, InterruptedException {
        String downloadURL = "https://boardgamegeek.com/xmlapi2/thing?id=" + queryString + "&stats=1";
        Log.i("INFO", "Attempting to download data from: " + downloadURL);
        AsyncTask<String, Void, BoardGameManager> getXMLTask = new getBoardGameDetails().execute(downloadURL);
        try {
            return getXMLTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getBoardGameDetails extends AsyncTask<String, Void, BoardGameManager> {
        @Override
        protected BoardGameManager doInBackground(String... params) {
            BoardGameManager bgm = BoardGameManager.getInstance();
            Log.i("INFO", "REACHED doInBackground");
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    bgm = serializer.read(BoardGameManager.class, is, false);

                    Log.i("EXCEPTION", "Finished Serializing");
                } finally {
                    con.disconnect();
                }
                Log.i("INFO", "Reached end of input stream.");

            } catch (Exception e) {
                Log.e("EXCEPTION", "Logging exception: " + e);
            }
            return bgm;
        }
    }
}
