package api.model;

import java.util.List;

public class Ingredients {
    private List<Ingredient> ingredients;
    private String success;

    public Ingredients(List<Ingredient> ingredients, String success) {
        this.ingredients = ingredients;
        this.success = success;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
