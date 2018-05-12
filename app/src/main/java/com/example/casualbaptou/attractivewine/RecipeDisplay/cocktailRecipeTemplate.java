package com.example.casualbaptou.attractivewine.RecipeDisplay;

import java.util.List;

public class cocktailRecipeTemplate {
    String name;
    String category;
    String mainRecipe;
    List<doubleString> ingredients;
    String imageLink;
    String lastTimeModified;
    String glassType;
    String alcoholic;


    public List<doubleString> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<doubleString> ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMainRecipe() {
        return mainRecipe;
    }

    public void setMainRecipe(String mainRecipe) {
        this.mainRecipe = mainRecipe;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLastTimeModified() {
        return lastTimeModified;
    }

    public void setLastTimeModified(String lastTimeModified) {
        this.lastTimeModified = lastTimeModified;
    }

    public String getGlassType() {
        return glassType;
    }

    public void setGlassType(String glassType) {
        this.glassType = glassType;
    }

    public String getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(String alcoholic) {
        this.alcoholic = alcoholic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}

class doubleString{
    public String A;
    public String B;

    public doubleString(String A, String B){
        this.A = A;
        this.B = B;
    }
}