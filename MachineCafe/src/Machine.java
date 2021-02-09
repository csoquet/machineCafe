import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.abs;

public class Machine {

    ArrayList<Boisson> listeBoisson;
    ArrayList<Ingredient> listeIngredientsTotal;
    Scanner s=new Scanner(System.in);

    public Machine() {
        listeBoisson = new ArrayList<Boisson>(3);
        listeIngredientsTotal = new ArrayList<>();
        Ingredient cafe = new Ingredient(Ingredient.NomIngredient.cafe, 0);
        Ingredient lait = new Ingredient(Ingredient.NomIngredient.lait, 0);
        Ingredient sucre = new Ingredient(Ingredient.NomIngredient.sucre, 0);
        Ingredient chocolat = new Ingredient(Ingredient.NomIngredient.chocolat, 0);
        listeIngredientsTotal.add(cafe);
        listeIngredientsTotal.add(lait);
        listeIngredientsTotal.add(sucre);
        listeIngredientsTotal.add(chocolat);
    }


    public void menu() {
            Scanner s=new Scanner(System.in);
            boolean rester = true;
            while (rester) {
                String choix;
                System.out.println("1. Achat Boisson");
                System.out.println("2. Ajouter Boisson");
                System.out.println("3. Modifier Boisson");
                System.out.println("4. Supprimer Boisson");
                System.out.println("5. Ajouter quantité d'un ingredient");
                System.out.println("6. Vérifier stock");
                System.out.println("7. Liste Boisson");
                System.out.println("8. Quitter");
                choix = s.nextLine();
                switch (choix) {
                    case "1":
                        if (listeBoisson.size() <= 0) {
                            System.out.println("Il n'y a pas de boissons dans la machine");
                            break;
                        }
                        acheterBoisson();
                        break;
                    case "2":
                        if (listeBoisson.size() == 3) {
                            System.out.println("Impossible d'ajouter une boisson, il y en a déjà 3");
                            break;
                        }
                        ajouterBoisson();
                        break;
                    case "3":
                        if (listeBoisson.size() <= 0) {
                            System.out.println("Il n'y a pas de boissons dans la machine");
                            break;
                        }
                        modifierCompositionBoisson();
                        break;
                    case "4":
                        if (listeBoisson.size() <= 0) {
                            System.out.println("Il n'y a pas de boissons dans la machine");
                            break;
                        }
                        supprimerBoisson();
                        break;
                    case "5":
                        ajouterQuantiteStock();
                        break;
                    case "6":
                        verifierStock();
                        break;
                    case "8":
                        rester = false;
                        s.close();
                        break;
                    case "7":
                        afficherBoisson();
                        break;
                    default:
                        System.err.println("Ce numéro n'existe pas");
                        break;
                }
            }
            System.out.println("A bientot !");
    }


    public void acheterBoisson(){
        Scanner s=new Scanner(System.in);
        afficherBoisson();
        int choix;
        System.out.println("Choisissez la boisson que vous voulez (donner le numéro)?");
        if(s.hasNextInt()){
            choix=Integer.parseInt(s.nextLine());
            Boisson choisi = listeBoisson.get(choix);
            System.out.println("Vous avez sélectionné la boisson " + choisi.getNom() + ", elle coûte " + choisi.getPrix() + " €");
            System.out.println("Veuillez insérer votre monnaie");
            int monnaie;
            monnaie=Integer.parseInt(s.nextLine());
            while(monnaie < choisi.getPrix()){
                System.out.println("");
                System.out.println("Vous n'avez pas mis assez de monnaie pour la boisson");
                System.out.println("Votre monnaie vous est rendue : " + monnaie + " €");
                System.out.println("");
                System.out.println("Vous avez sélectionné la boisson " + choisi.getNom() + ", elle coûte " + choisi.getPrix() + " €");
                System.out.println("Veuillez insérer votre monnaie");
                monnaie=Integer.parseInt(s.nextLine());
            }
            int solde = 0;
            if(monnaie > choisi.getPrix()){
                solde = monnaie - choisi.getPrix();
            }
            boolean suffisant = true;
            for(Ingredient e : choisi.getIngredients()){
                for(Ingredient p : listeIngredientsTotal){
                    if(p.getNom().equals(e.getNom())){
                        if(p.getStock() < e.getStock()){
                            suffisant = false;
                            break;
                        }
                    }

                }
            }

            if(!suffisant){
                System.out.println("-----------------------------------------------------------");
                System.out.println("Il n'y a pas assez d'ingrédients pour vous fournir la boisson");
                System.out.println("Votre monnaie vous est rendue : " + monnaie + " €");
                System.out.println("-----------------------------------------------------------");
            }
            else
            {
                for(Ingredient e : choisi.getIngredients()){
                    for(Ingredient p : listeIngredientsTotal){
                        if(p.getNom().equals(e.getNom())){
                            p.setStock(p.getStock()-e.getStock());
                        }

                    }
                }
                System.out.println("-----------------------------------------------------------");
                System.out.println("Votre boisson " + choisi.getNom() + " est en cours de préparation");
                for (int i = 0; i <= 100; i = i + 20) {
                    progressPercentage(i, 100);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                }

                System.out.println("-----------------------------------------------------------");
                System.out.println("Votre boisson " + choisi.getNom() + " est prête");
                if(solde != 0){
                    System.out.println("Votre monnaie vous est rendue : " + solde + " €");
                }
                System.out.println("-----------------------------------------------------------");
            }

        }
        else{
            System.out.println("Veuillez rentrer un numéro");
        }


    }

    public static void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10; // 10unit for 100%
        int remainProcent = ((100 * remain) / total) / maxBareSize;
        char defaultChar = '-';
        String icon = "*";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("[");
        for (int i = 0; i < remainProcent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainProcent, bare.length());
        System.out.print("\r" + bareDone + bareRemain + " " + remainProcent * 10 + "%");
        if (remain == total) {
            System.out.print("\n");
        }
    }

    public void ajouterBoisson(){

        boolean existe = true;
        String nom = "";
        Scanner s = new Scanner(System.in);
        while(existe) {
            existe = false;
            System.out.println("Donner le nom de la boisson");
            nom = s.nextLine();
            for (Boisson b : listeBoisson) {
                if (b.getNom().equals(nom)) {
                    existe = true;
                    System.out.println("Cette boisson existe déjà");
                    break;
                }
            }
        }
        int prix = 0;
        int test;
        System.out.println("Donner le prix de la boisson");
        if(s.hasNextInt()){
            test = s.nextInt();
            if(test < 0){
                prix = abs(test);
                System.out.println("Conversion de votre nombre négatif en positif");
            }
            else{
                prix = test;
            }

            int nbCafe=0;
            try {
                System.out.println("Donner le nombre de quantité de café présent dans cette boisson");
                test = s.nextInt();
                if(test < 0){
                    nbCafe=abs(test);
                    System.out.println("Conversion de votre nombre négatif en positif");
                }
                else{
                    nbCafe = test;
                }
                int nbLait=0;
                System.out.println("Donner le nombre de quantité de lait présent dans cette boisson");
                test = s.nextInt();
                if(test < 0){
                    nbLait=abs(test);
                    System.out.println("Conversion de votre nombre négatif en positif");
                }
                else{
                    nbLait = test;
                }
                int nbChocolat=0;
                System.out.println("Donner le nombre de quantité de chocolat présent dans cette boisson");
                test = s.nextInt();
                if(test < 0){
                    nbChocolat=abs(test);
                    System.out.println("Conversion de votre nombre négatif en positif");
                }
                else{
                    nbChocolat = test;
                }
                int nbSucre=0;
                System.out.println("Donner le nombre de quantité de sucre présent dans cette boisson");
                test = s.nextInt();
                if(test < 0){
                    nbSucre=abs(test);
                    System.out.println("Conversion de votre nombre négatif en positif");
                }
                else{
                    nbSucre = test;
                }

                if(nbSucre >= 0 & nbChocolat == 0 & nbLait == 0 & nbCafe == 0){
                    System.out.println("Impossible de créer cette boisson");
                }

                else{
                    Boisson b = new Boisson(nom, prix, nbCafe, nbLait, nbChocolat, nbSucre);
                    listeBoisson.add(b);
                    System.out.println("");
                    System.out.println("L'ajout de votre boisson " + nom + " s'est déroulé correctement");
                    System.out.println("-----------------------------------------------------------");
                }
            }
            catch(Exception e){
                System.out.println("Entrer une valeur numérique s'il vous plait. \n");
            }
        }
        else{
            System.out.println("Entrer une valeur numérique s'il vous plait \n");
        }

    }

    public void modifierCompositionBoisson(){
        Scanner s=new Scanner(System.in);
        afficherBoisson();
        int choix = 0;
        System.out.println("Choisissez la boisson que vous souhaitez modifier (donner le numéro)");
        try{
            choix=s.nextInt();
            Boisson boissonChoisi = listeBoisson.get(choix);
            System.out.println("-----------------------------------------------------------");
            afficherBoissonComplexe(boissonChoisi);
            System.out.println("-----------------------------------------------------------");
            System.out.println("-----------------------------------------------------------");
            int choisir;
            System.out.println("Vous avez sélectionné la boisson " + boissonChoisi.getNom());
            System.out.println("1. Modifier le nombre d'unités de café");
            System.out.println("2. Modifier le nombre d'unités de lait");
            System.out.println("3. Modifier le nombre d'unités de chocolat");
            System.out.println("4. Modifier le nombre d'unités de sucre");
            choisir = s.nextInt();
            switch (choisir) {
                case 1:
                    int nbCafé;
                    int nbCaféAncien = 0;
                    System.out.println("Quel est le nouveau nombre d'unités de café ?");
                    nbCafé = s.nextInt();
                    for(Ingredient e : boissonChoisi.getIngredients()){
                        if(e.getNom().equals(Ingredient.NomIngredient.cafe)){
                            nbCaféAncien = e.getStock();
                            e.setStock(nbCafé);
                        }
                    }
                    System.out.println("");
                    System.out.println("Le nombre d'unités de café à bien été modifier, c'est passer de  " + nbCaféAncien + " à " + nbCafé + " " );
                    break;
                case 2:
                    int nbLait;
                    int nbLaitAncien = 0;
                    System.out.println("Quel est le nouveau nombre d'unités de lait ?");
                    nbLait = s.nextInt();
                    for(Ingredient e : boissonChoisi.getIngredients()){
                        if(e.getNom().equals(Ingredient.NomIngredient.lait)){
                            nbLaitAncien = e.getStock();
                            e.setStock(nbLait);
                        }
                    }
                    System.out.println("");
                    System.out.println("Le nombre d'unités de café à bien été modifier, c'est passer de  " + nbLaitAncien + " à " + nbLait + " " );
                    break;
                case 3:
                    int nbChocolat;
                    int nbChocolatAncien = 0;
                    System.out.println("Quel est le nouveau nombre d'unités de chocolat ?");
                    nbChocolat = s.nextInt();
                    for(Ingredient e : boissonChoisi.getIngredients()){
                        if(e.getNom().equals(Ingredient.NomIngredient.chocolat)){
                            nbChocolatAncien = e.getStock();
                            e.setStock(nbChocolat);
                        }
                    }
                    System.out.println("");
                    System.out.println("Le nombre d'unités de café à bien été modifier, c'est passer de  " + nbChocolatAncien + " à " + nbChocolat + " " );
                    break;
                case 4:
                    int nbSucre;
                    int nbSucreAncien = 0;
                    System.out.println("Quel est le nouveau nombre d'unités de sucre ?");
                    nbSucre = s.nextInt();
                    for(Ingredient e : boissonChoisi.getIngredients()){
                        if(e.getNom().equals(Ingredient.NomIngredient.sucre)){
                            nbSucreAncien = e.getStock();
                            e.setStock(nbSucre);
                        }
                    }
                    System.out.println("");
                    System.out.println("Le nombre d'unités de café à bien été modifier, c'est passer de  " + nbSucreAncien + " à " + nbSucre + " " );
                    break;
            }

            System.out.println("-----------------------------------------------------------");
        }
        catch (Exception e){
            System.out.println("Il faut entrer un numéro s'il vous plait");
        }




    }

    public void supprimerBoisson(){
        Scanner s=new Scanner(System.in);
        afficherBoisson();
        int choix;
        System.out.println("Choisissez la boisson que vous voulez supprimer?(donner le numéro)");
        choix=Integer.parseInt(s.nextLine());
        String nom = listeBoisson.get(choix).getNom();
        listeBoisson.remove(choix);
        System.out.println("");
        System.out.println("La suppression de la boisson " + nom + " s'est déroulé correctement");
        System.out.println("-----------------------------------------------------------");
    }

    public void afficherStock(){
        int i = 0;
        for(Ingredient b : listeIngredientsTotal){
            System.out.println(i + "-" + b.getNom() +  " - Stock disponible : " + b.getStock());
            i++;
        }
    }

    public void ajouterQuantiteStock(){
        Scanner s=new Scanner(System.in);
        afficherStock();
        int choix;
        System.out.println("Choisissez l'ingrédient ou vous voulez ajouter du stock (donner le numéro)");
        choix=Integer.parseInt(s.nextLine());
        System.out.println(listeIngredientsTotal.get(choix).getNom() + " - Stock disponible : " + listeIngredientsTotal.get(choix).getStock());
        int stockPrecedent = listeIngredientsTotal.get(choix).getStock();
        int stock;
        System.out.println("Renseigner le stock pour cet ingrédient : ");
        stock=Integer.parseInt(s.nextLine());
        listeIngredientsTotal.get(choix).setStock(stock);
        System.out.println("");
        System.out.println("La mise à jour du stock de " + listeIngredientsTotal.get(choix).getNom() + " s'est déroulé correctement");
        System.out.println("Le stock est bien passé de " + stockPrecedent + " a " + stock);
        System.out.println("-----------------------------------------------------------");

    }

    public void verifierStock(){
        System.out.println("-----------------------------------------------------------");
        afficherStock();
        System.out.println("-----------------------------------------------------------");
    }

    public void afficherBoisson(){
        System.out.println("-----------------------------------------------------------");
        int i = 0;
        for(Boisson b : listeBoisson){
            System.out.println(i + " - " +b.getNom() + " Prix de la boisson : " + b.getPrix() + " €") ;
            i++;
        }
        System.out.println("-----------------------------------------------------------");

    }

    public void afficherBoissonComplexe(Boisson b){
        System.out.println("-----------------------------------------------------------");
        int nbCafe = 0;
        int nbLait = 0;
        int nbChocolat = 0;
        int nbSucre = 0;
        for(Ingredient e: b.getIngredients()){
            if(e.getNom().equals(Ingredient.NomIngredient.cafe)){
               nbCafe = e.getStock();
            }
            if(e.getNom().equals(Ingredient.NomIngredient.lait)){
                nbLait = e.getStock();
            }
            if(e.getNom().equals(Ingredient.NomIngredient.chocolat)){
                nbChocolat = e.getStock();
            }
            if(e.getNom().equals(Ingredient.NomIngredient.sucre)){
                nbSucre = e.getStock();
            }
        }
        System.out.println(b.getNom() + " Prix de la boisson : " + b.getPrix() + " €");
        System.out.println("Nb café " + nbCafe + " - Nb lait " + nbLait + " - Nb chocolat " + nbChocolat + " - Nb Sucre " + nbSucre);
        System.out.println("-----------------------------------------------------------");

    }

}
