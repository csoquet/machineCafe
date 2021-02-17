import java.io.*;
import java.security.spec.ECField;
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
        listeBoisson = new ArrayList<Boisson>(5);
        listeIngredientsTotal = new ArrayList<>();
        Ingredient cafe = new Ingredient(Ingredient.NomIngredient.cafe);
        Ingredient lait = new Ingredient(Ingredient.NomIngredient.lait);
        Ingredient sucre = new Ingredient(Ingredient.NomIngredient.sucre);
        Ingredient chocolat = new Ingredient(Ingredient.NomIngredient.chocolat);
        Ingredient the = new Ingredient(Ingredient.NomIngredient.the);
        listeIngredientsTotal.add(cafe);
        listeIngredientsTotal.add(lait);
        listeIngredientsTotal.add(sucre);
        listeIngredientsTotal.add(chocolat);
        listeIngredientsTotal.add(the);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("stocks.txt"));
            String line;
            final String SEPARATEUR = ",";
            String[] mots;
            while ((line = in.readLine()) != null)
            {
                mots = line.split(SEPARATEUR);
                for(Ingredient e : listeIngredientsTotal){
                    String nom = String.valueOf(e.getNom());
                    if(mots[0].equals(nom)){
                        e.setStock(Integer.parseInt(mots[1]));
                    }
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("Premier lancement de la machine, initialisation des stocks à 0");
        } catch (IOException e) {

        }

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
                        if (pasDeBoisson()) {
                            break;
                        }
                        acheterBoisson();
                        break;
                    case "2":
                        if(listeBoisson.size() == 5){
                            System.err.println("Impossible d'ajouter une nouvelle boisson");
                            break;
                        }
                        ajouterBoisson();
                        break;
                    case "3":
                        if (pasDeBoisson()) {
                            break;
                        }
                        modifierCompositionBoisson();
                        break;
                    case "4":
                        if (pasDeBoisson()) {
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
                        enregistrerStock();
                        s.close();
                        break;
                    case "7":
                        if (pasDeBoisson()) {
                            break;
                        }
                        afficherBoisson();
                        break;
                    default:
                        System.err.println("Ce numéro n'existe pas");
                        break;
                }
            }
            System.out.println("A bientot !");
    }

    public boolean pasDeBoisson(){
        if (listeBoisson.isEmpty()) {
            System.err.println("Il n'y a pas de boissons dans la machine");
            return true;
        }
        return false;
    }

    public void acheterBoisson(){
        Scanner s=new Scanner(System.in);
        afficherBoisson();
        int choix;
        System.out.println("Choisissez la boisson que vous voulez (donner le numéro)?");
        if(s.hasNextInt()){
            try{
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
                    String reponse;
                    System.out.println("Voulez-vous modifier la composition du sucre ? (O/N)");
                    reponse=s.nextLine();
                    while(!(reponse.equals("O") || reponse.equals("N"))){
                        System.out.println("Voulez-vous modifier la composition du sucre ? (O/N)");
                        reponse=s.nextLine();
                    }
                    int nbSucreAncien = 0;
                    if(reponse.equals("O")){
                       nbSucreAncien = modifSucre(choisi);
                    }
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

                    if(reponse.equals("O")){
                        for(Ingredient e : choisi.getIngredients()){
                            if(e.getNom().equals(Ingredient.NomIngredient.sucre)){
                                e.setStock(nbSucreAncien);
                            }
                        }
                    }
                }

            } catch(Exception e){
                System.err.println("Veuillez rentrer un numéro valide");
            }

        }
        else{
            System.err.println("Veuillez rentrer un numéro valide");
        }
    }

    public int modifSucre(Boisson b){
        int nbSucreAncien = 0;
        for(Ingredient e : b.getIngredients()){
            if(e.getNom().equals(Ingredient.NomIngredient.sucre)){
                nbSucreAncien = e.getStock();
            }
        }
        Scanner s=new Scanner(System.in);
        int nbSucre = 0;
        System.out.println("Votre boisson est à " + nbSucreAncien + " sucres. A combien voulez vous le modifier ?");
        while(!s.hasNextInt()){
            System.err.println("Veuillez rentrer une valeur valide !");
            s=new Scanner(System.in);
            System.out.println("Votre boisson est à " + nbSucreAncien + " sucres. A combien voulez vous le modifier ?");
        }
            nbSucre = Integer.parseInt(s.nextLine());

        for(Ingredient e : b.getIngredients()){
            if(e.getNom().equals(Ingredient.NomIngredient.sucre)){
                e.setStock(nbSucre);
            }
        }
        System.out.println("");
        System.out.println("Le nombre d'unités de sucre à bien été modifier, c'est passer de  " + nbSucreAncien + " à " + nbSucre + " " );
        System.out.println("");
        return nbSucreAncien;
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
                    System.err.println("Cette boisson existe déjà");
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
                int nbThe=0;
                System.out.println("Donner le nombre de quantité de thé présent dans cette boisson");
                test = s.nextInt();
                if(test < 0){
                    nbThe=abs(test);
                    System.out.println("Conversion de votre nombre négatif en positif");
                }
                else{
                    nbThe = test;
                }

                if(nbSucre >= 0 && nbChocolat == 0 && nbLait == 0 && nbCafe == 0 && nbThe == 0){
                    System.out.println("Impossible de créer cette boisson");
                }

                else{
                    Boisson b = new Boisson(nom, prix, nbCafe, nbLait, nbChocolat, nbSucre, nbThe);
                    listeBoisson.add(b);
                    System.out.println("");
                    System.out.println("L'ajout de votre boisson " + nom + " s'est déroulé correctement");
                    System.out.println("-----------------------------------------------------------");
                }
            }
            catch(Exception e){
                System.err.println("Veuillez entrer un nombre valide s'il vous plait ! \n");
            }
        }
        else{
            System.err.println("Veuillez entrer un nombre valide s'il vous plait ! \n");
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
            System.out.println("5. Modifier le nombre d'unités de the");
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
                    System.out.println("Le nombre d'unités de lait à bien été modifier, c'est passer de  " + nbLaitAncien + " à " + nbLait + " " );
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
                    System.out.println("Le nombre d'unités de chocolat à bien été modifier, c'est passer de  " + nbChocolatAncien + " à " + nbChocolat + " " );
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
                    System.out.println("Le nombre d'unités de sucre à bien été modifier, c'est passer de  " + nbSucreAncien + " à " + nbSucre + " " );
                    break;
                case 5:
                    int nbThe;
                    int nbTheAncien = 0;
                    System.out.println("Quel est le nouveau nombre d'unités de the ?");
                    nbThe = s.nextInt();
                    for(Ingredient e : boissonChoisi.getIngredients()){
                        if(e.getNom().equals(Ingredient.NomIngredient.the)){
                            nbTheAncien = e.getStock();
                            e.setStock(nbThe);
                        }
                    }
                    System.out.println("");
                    System.out.println("Le nombre d'unités de the à bien été modifier, c'est passer de  " + nbTheAncien + " à " + nbThe + " " );
                    break;
            }

            System.out.println("-----------------------------------------------------------");
        }
        catch (Exception e){
            System.err.println("Il faut entrer un numéro valide s'il vous plait");
        }




    }

    public void supprimerBoisson(){
        try {
            Scanner s = new Scanner(System.in);
            afficherBoisson();
            int choix;
            System.out.println("Choisissez la boisson que vous voulez supprimer?(donner le numéro)");
            choix = Integer.parseInt(s.nextLine());
            String nom = listeBoisson.get(choix).getNom();
            listeBoisson.remove(choix);
            System.out.println("");
            System.out.println("La suppression de la boisson " + nom + " s'est déroulé correctement");
            System.out.println("-----------------------------------------------------------");
        } catch (Exception e){
            System.err.println("Veuillez entrer un nombre valide s'il vous plait !");
        }
    }

    public void afficherStock(){
        int i = 0;
        for(Ingredient b : listeIngredientsTotal){
            System.out.println(i + "-" + b.getNom() +  " - Stock disponible : " + b.getStock());
            i++;
        }
    }

    public void ajouterQuantiteStock(){
        try {
            Scanner s = new Scanner(System.in);
            afficherStock();
            int choix;
            System.out.println("Choisissez l'ingrédient ou vous voulez ajouter du stock (donner le numéro)");
            choix = Integer.parseInt(s.nextLine());
            System.out.println(listeIngredientsTotal.get(choix).getNom() + " - Stock disponible : " + listeIngredientsTotal.get(choix).getStock());
            int stockPrecedent = listeIngredientsTotal.get(choix).getStock();
            int stock;
            System.out.println("Renseigner le stock pour cet ingrédient : ");
            stock = Integer.parseInt(s.nextLine());
            listeIngredientsTotal.get(choix).setStock(stock);
            System.out.println("");
            System.out.println("La mise à jour du stock de " + listeIngredientsTotal.get(choix).getNom() + " s'est déroulé correctement");
            System.out.println("Le stock est bien passé de " + stockPrecedent + " a " + stock);
            System.out.println("-----------------------------------------------------------");
        } catch(Exception e){
            System.err.println("Veuillez rentrer un nombre valide s'il vous plait !");
        }

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
        int nbThe = 0;
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
            if(e.getNom().equals(Ingredient.NomIngredient.the)){
                nbThe = e.getStock();
            }
        }
        System.out.println(b.getNom() + " Prix de la boisson : " + b.getPrix() + " €");
        System.out.println("Nb café " + nbCafe + " - Nb lait " + nbLait + " - Nb chocolat " + nbChocolat + " - Nb Sucre " + nbSucre + " - Nb The " + nbThe);
        System.out.println("-----------------------------------------------------------");

    }

    public void enregistrerStock(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("stocks.txt", "UTF-8");
            for(Ingredient e : listeIngredientsTotal){
                writer.println(e.getNom()+","+e.getStock());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
