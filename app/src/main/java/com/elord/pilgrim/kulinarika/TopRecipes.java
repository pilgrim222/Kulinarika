package com.elord.pilgrim.kulinarika;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
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

import java.util.Iterator;


public class TopRecipes extends ActionBarActivity {

    ListView recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_recipes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_recipes, menu);
        new TopRecipesGetter().execute(Communicator.KULINARIKA_RECEPTI_TOP_VSI_URL);
        recipeList = (ListView) findViewById(R.id.recipesList);

        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displayDetails = new Intent(TopRecipes.this, RecipeDetails.class);
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

    private class TopRecipesGetter extends Communicator {

        @Override
        protected void onPostExecute(Document doc) {
            Elements allTimeBest = doc.getElementsByTag("a");

            Iterator<Element> bestRecipeIterator = allTimeBest.iterator();
            Recipe[] bestRecipes = new Recipe[allTimeBest.size()];

            int i = 0;
            Element current;
            while(bestRecipeIterator.hasNext()) {
                current = bestRecipeIterator.next();
                bestRecipes[i++] = new Recipe(current.text(), Communicator.KULINARIKA_ROOT_URL + current.attr("href"));
            }

            ArrayAdapter<Recipe> topRecipes = new ArrayAdapter<Recipe>(TopRecipes.this,
                    android.R.layout.simple_list_item_1, bestRecipes);

            recipeList.setAdapter(topRecipes);
        }
    }
}
