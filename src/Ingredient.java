public class Ingredient {

    enum NomIngredient {
        lait,
        sucre,
        chocolat,
        cafe,
        the;

    }

    private NomIngredient nom;

    private int stock;

    public Ingredient(NomIngredient nom) {
        this.nom = nom;
    }

    public Ingredient(NomIngredient nom, int stock) {
        this.nom = nom;
        this.stock = stock;
    }

    public NomIngredient getNom() {
        return nom;
    }

    public void setNom(NomIngredient nom) {
        this.nom = nom;
    }

    public void setStock(int unite) {
        this.stock = unite;
    }


    public int getStock() {
        return stock;
    }


}
