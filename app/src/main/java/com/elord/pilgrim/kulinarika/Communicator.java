package com.elord.pilgrim.kulinarika;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pilgrim on 5.2.2015.
 */
public abstract class Communicator extends AsyncTask<String, Void, Document> {

    public static String KULINARIKA_ROOT_URL = "http://www.kulinarika.net/";
    public static String KULINARIKA_RECEPTI_ROOT_URL = KULINARIKA_ROOT_URL + "recepti/";
    public static String KULINARIKA_RECEPTI_TOP_VSI_URL = KULINARIKA_ROOT_URL +
            "inc/vsicasi.asp?vsi=1&kategorija=";
    public static String KULINARIKA_RECEPTI_TOP_MESEC = KULINARIKA_ROOT_URL +
            "inc/ajaxlestvica-recepti.asp?kategorija=&mesec=";
    public static String KULINARIKA_RECEPTI_ISKANJE = KULINARIKA_ROOT_URL +
            "zadetki.asp?splosno_besede=";

    @Override
    protected Document doInBackground(String... params) {
        return getPageDOM(constructURL(params[0]));
    }

    protected abstract void onPostExecute(Document doc);

    /**
     * Constructs an URL object with target as its url. To have a single try/catch point
     * @param target
     * @return
     */
    private URL constructURL(String target) {
        try{
            return new URL(target);
        } catch(MalformedURLException e) {
            Log.e("Miha", "Malformed URL: " + target);
            return null;
        }
    }

    /**
     * Returns the root of the parsed source of the page.
     * @param page the page to be arsed
     * @return the Document obtained by parsing page using jsoup
     */
    public Document getPageDOM(URL page) {
        Document pageDocument;
        try {
            pageDocument = Jsoup.parse(page, 10000);
        } catch (IOException e) {
            Log.e("Miha", "Error accessing page " + page);
            pageDocument = Jsoup.parse("");
        }
        return pageDocument;
    }

}
