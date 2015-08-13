package com.ggalasso.simpletest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by truthd on 4/20/2015.
 */
public class FileHandler {
    private final String FILENAME = "xml_test.xml";

    //readMyXML().execute();
    private static FileHandler ourInstance = null;

    public static FileHandler getInstance() {
        if (ourInstance == null) {
            ourInstance = new FileHandler();
        }
        return ourInstance;
    }

    private FileHandler() {
        Log.i("INFO", "Instantiated File Handler");
    }

    public void getXMLData() {
        new readMyXML().execute();
    }

    private class readMyXML extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.i("INFO", "REACHED doInBackground");
            String downloadURL = "http://www.androidpeople.com/wp-content/uploads/2010/06/example.xml";
            try {
                URL url = new URL(downloadURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                try {
                    InputStream is = new BufferedInputStream(con.getInputStream());
                    Serializer serializer = new Persister();
                    maintag bg = serializer.read(maintag.class, is);
                    Log.i("EXCEPTION -- MY ERROR!!", "Passed Test!");
                } finally {
                    con.disconnect();
                }

                //BufferedInputStream bis
                //BufferedInputStream bis = new BufferedInputStream(is);

                //Serializer serializer = new Persister();
//                //public void saveGson(Context ctx) throws IOException {
//                    FileOutputStream fOut = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
//                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//                    Gson gson = new GsonBuilder().create();
//                    gson.toJson(getContactList(), myOutWriter);
//                    myOutWriter.close();
//                    fOut.close();
//                //}
//                GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(is));
//                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//                DocumentBuilder db = dbf.newDocumentBuilder();
//                Document xmlDoc = db.parse(is);
                //FileInputStream fis = new FileInputStream(xmlDoc);
//
                //File source = ;
                //maintag bg = serializer.read(maintag.class, zis );
                //maintag bg = serializer.read(maintag.class, is);
                //Log.i("Name:", bg.toString());
//                Element rootEl = xmlDoc.getDocumentElement();

                //maintag bg = serializer.read(maintag.class, is);
                Log.i("INFO XML DATA", "Root Element from file handler:");

                //processXML(is);
            } catch (Exception e) {
                Log.e("EXCEPTION -- MY ERROR!!", "exception" + e);
            }
            return "Executed from file handler.";
        }

        public void processXML(InputStream is) throws Exception {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse(is);
            Element rootEl = xmlDoc.getDocumentElement();
            Log.i("INFO XML DATA", "Root Element from file handler:" + rootEl.getTagName());
        }

    }



}