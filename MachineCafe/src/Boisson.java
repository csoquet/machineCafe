import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;

public class Boisson {


    private String nom;
    private int prix;

    ArrayList<Ingredient> ingredients;


    public Boisson(String nom, int prix, int nbCafe, int nbLait, int nbChocolat, int nbSucre) {
        this.nom = nom;
        this.prix = prix;
        ingredients = new ArrayList<Ingredient>();
        Ingredient cafe = new Ingredient(Ingredient.NomIngredient.cafe, nbCafe);
        Ingredient lait = new Ingredient(Ingredient.NomIngredient.lait, nbLait);
        Ingredient sucre = new Ingredient(Ingredient.NomIngredient.sucre, nbSucre);
        Ingredient chocolat = new Ingredient(Ingredient.NomIngredient.chocolat, nbChocolat);
        ingredients.add(cafe);
        ingredients.add(lait);
        ingredients.add(sucre);
        ingredients.add(chocolat);

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }


    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
