package com.elord.pilgrim.kulinarika.data;

/**
 * Created by pilgrim on 6.2.2015.
 */
public class Recipe {
    private String name;
    private String url;
    private String[] ingredients;
    private String[] procedure;

    public Recipe(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String[] getProcedure() {
        return procedure;
    }

    public String[] getIngredients(){
        return ingredients;
    }

    public void setProcedure(String[] procedure) {
        this.procedure = procedure;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }


    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
