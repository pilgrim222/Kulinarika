package com.elord.pilgrim.kulinarika;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.elord.pilgrim.kulinarika.data.Recipe;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;


public class SearchActivity extends ActionBarActivity {

    ListView recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_recipes);

        String query = "";
        try {
            query = URLEncoder.encode(getIntent().getStringExtra(SearchManager.QUERY), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("Kulinarika", e.toString());
        }

        new SearchResultsGetter().execute(Communicator.KULINARIKA_RECEPTI_ISKANJE + query);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_recipes, menu);

        recipeList = (ListView) findViewById(R.id.recipesList);

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displayDetails = new Intent(SearchActivity.this, RecipeDetails.class);
                RecipeDetails.displayedRecipe = (Recipe) parent.getItemAtPosition(position);
                startActivity(displayDetails);
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SearchResultsGetter extends Communicator {

        @Override
        protected void onPostExecute(Document doc) {
            //Elements table = doc.getElementsByClass("tablica_recepti").first().nextElementSibling().getElementsByTag("tr");

            Elements table = doc.getElementsByClass("tablica_recepti").first().nextElementSibling().getElementsByTag("tr");

            Iterator<Element> resultsIterator = table.iterator();
            Recipe[] results = new Recipe[table.size()];

            int i = 0;
            Element current;
            while(resultsIterator.hasNext()) {
                current = resultsIterator.next();
                current = current.getElementsByTag("td").first().getElementsByTag("a").last();
                results[i++] = new Recipe(current.text(), Communicator.KULINARIKA_ROOT_URL + current.attr("href"));
            }

            ArrayAdapter<Recipe> resultAdapter = new ArrayAdapter<Recipe>(SearchActivity.this,
                    android.R.layout.simple_list_item_1, results);

            recipeList.setAdapter(resultAdapter);
        }
    }
}
