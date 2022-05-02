package tp1;
import java.util.ArrayList;
import java.util.Arrays;
public class Simplex {
	
	ArrayList<String> nomVariableV = new ArrayList<String>();//pour notations des var verticale dans le tableau 
    ArrayList<String> nomVariableH = new ArrayList<String>();//pour notations des var horizontale dans le tableau
    ArrayList<Double> ArrayZ = new ArrayList<Double>();//les coef du fonction obectif

    int nbrVarBase = 0;//nmb des var de base
    int nbrVarArtificial = 0;//nmb des var artificials
    int nbrVarBaseNeg = 0;//nmb des var de base negatif (dans le cas du superieur ou egale)
    int i = 0;
    int j = 0;
    String maxMin;//max ou min
    double AncienZ = 0;
    boolean existSolutionOptimal = false;//si il exist un solution optimal il devient true

    
    //cette fonction recoit 2 matrice et faire une comparaison si ils sont identique elle return true sinon return false
    public boolean comparer2matrix(int i,double[][] A1, double[][] A2, int nmbColomn, int nmbLigne) {
        for (i = 0; i < nmbLigne; i++) {
            for (int j = 0; j < nmbColomn; j++) {
                if (A1[i][j] != A2[i][j]) {
                    return false; //return false if the deffernt value
                }
            }
        }
        return true;//return true if equals value
    }
    
    public int nbrElementPositif(ArrayList<Double> B) {
        int elementPositif = 0;
        for (int i = 0; i < B.size(); i++) {
            if (B.get(i) > 0) {
                elementPositif++;
            }
        }
        return elementPositif;
    }
    //recherche si exist un v dans la liste globale (nomVariableV)
    //on a utilise cette fonction pour connais si ona terminer la premiere phase (les var artificials n'exist pas)
    public boolean siExisteV() {
        for (i = 0; i < nomVariableV.size(); i++) {
            if (nomVariableV.get(i).equals("V")) {
                return true;
            }
        }
        return false;
    }
//recoit array list et calculer et retourner le nmb des elements negatif
   
    static int nbrElementNegatif(ArrayList<Double> B,int l) {
        int elementNegatif = 0;
        for (int i = 0; i < l; i++) { //l c'es la taille du array list 
            if (B.get(i) < 0) {
                elementNegatif++;
            }
        }
        return elementNegatif;
    }
    //recoit matrice et return une cilumn specific
    //lenght : le nmb des column dans le matrice
    //index: l'indice du column qui on veut le reourner
    public ArrayList<Double> getColumn(double[][] array, int length, int index) {
        ArrayList<Double> column = new ArrayList<Double>();
        for (int i = 0; i < length; i++) {
            column.add(array[i][index]);
        }
        return column;//return column en tant que table 1 dimension 
    }
    //return le nmb de var decart
    
    public void nmbVarDecart(String[] comparaison) {

        for (int i = 0; i < comparaison.length; i++) {
            if (comparaison[i].equals("<=")){ //si la relation est <= alors on incremente le nmb des var de base
                nbrVarBase++;
            } else if (comparaison[i].equals(">=")) {
                //si la relation est <= alors on incremente le nmb des 
                //var de base negatif (cad coef = -1)
                //et incrementer le nmb des var artificials
                
                nbrVarBaseNeg++;                        
                nbrVarArtificial++;
            } else {
            	//il reste le cas de "=" , dans ce cas on incremente just le nmb des var artificials
                nbrVarArtificial++;
            }
        }
    }

    public Simplex(String maxMin, double[][] A, ArrayList<Double> C, ArrayList<Double> B, String[] comparaison, int contraintNbr, int nbrVariableHorsBase) {
        this.maxMin = maxMin;
        double Z = 0, temp = 0;
        int c, indiceX, indiceV, lenght1;
        double[][] AF;//le matrice des coef
        ArrayList<Integer> varV = new ArrayList<Integer>();//coef des var artificials
        nmbVarDecart(comparaison);//on a fait la description dans la methode en haut 
        c = 0;

        indiceX = nbrVariableHorsBase + 1;//on a inrementer pour l'affichage debut avec 1 et pas avec 0 par exemple en depart avec X1 et pas X0
        indiceV = 1;
        lenght1 = nbrVariableHorsBase + nbrVarBase + nbrVarBaseNeg + nbrVarArtificial;//le nombe des column dans le tableau
        //n3amrou tableau ta3 les noms ta3 les var (x)
        for (i = 0; i < nbrVariableHorsBase + nbrVarBase + nbrVarBaseNeg; i++) {
            nomVariableH.add("X");
        }
        // (v)artificiel
        for (i = nbrVariableHorsBase + nbrVarBase + nbrVarBaseNeg; i < lenght1; i++) {
            nomVariableH.add("V");
        }

        if (nbrVarArtificial > 0) {
            AF = new double[contraintNbr + 1][lenght1];

        } else {
            AF = new double[contraintNbr][lenght1];
        }
        //copier A to AF
        //car la taille du A est petit et on peut pas ajouter des var artificials ou de base
        //et pour cea on a grandir la taille du matrice pour ajouter des columns
        for (i = 0; i < contraintNbr; i++) {
            for (j = 0; j < nbrVariableHorsBase; j++) {
                AF[i][j] = A[i][j];
            }
        }
        //ajouter les variable de base au matrice 
        for (i = 0; i < contraintNbr; i++) {
            if (comparaison[i].equals("<=")) {
                AF[c][nbrVariableHorsBase + c] = 1;
                nomVariableV.add("X");
                varV.add(indiceX++);
                c++;
            } else if (comparaison[i].equals(">=")) {
                AF[c][nbrVariableHorsBase + c] = -1;
                c++;
            }
        }
        c = nbrVariableHorsBase + nbrVarBase + nbrVarBaseNeg;
        for (i = 0; i < contraintNbr; i++) {
            if (comparaison[i].equals(">=") || comparaison[i].equals("=")) {
                AF[i][c] = 1;
                nomVariableV.add("V");
                varV.add(indiceV++);
                c++;

            }
        }
        
        //si le nmb des var artificials superieur a 0 
        
        if (nbrVarArtificial > 0) {
            //remlir le reste du matrice avec des zero
            for (i = nbrVariableHorsBase; i < lenght1; i++) {
                C.add(0.0);
            }
            //axtrair la fonction objectif -V
            for (i = 0; i < nbrVariableHorsBase + nbrVarBase + nbrVarBaseNeg; i++) {
                temp = 0;
                for (j = 0; j < nbrVarBase + nbrVarArtificial; j++) {
                    //calcule la somme des ligne qui contient variable artificial
                    if (nomVariableV.get(j).equals("V")) {
                        temp = temp + AF[j][i];
                    }
                }
                AF[contraintNbr][i] = C.get(i) * (-1);
                C.set(i, temp * (-1));

            }
         // calculer -V
            temp = 0;
            
            for (j = 0; j < nbrVarBase + nbrVarArtificial; j++) {
                if (nomVariableV.get(j).equals("V")) {
                    temp = temp + B.get(j);
                }
            }
            B.add(0.0);
            Z = temp * -1;

        }

        //premiere phase
        if (nbrVarArtificial > 0) {
            //appeler la method de premiere phase
            premierePhase(Z, AF, C, B, contraintNbr, nbrVariableHorsBase, varV);
        } else {
            //complete fonction objectif avec des 0
            for (i = nbrVariableHorsBase; i < lenght1; i++) {
                C.add(0.0);
            }
            //appeler methode de simplex apres eliminer les var artificials
            simplexMethod(maxMin, Z, AF, C, B, contraintNbr, nbrVariableHorsBase, varV);
        }


    }

    private void premierePhase(double Z, double[][] AF, ArrayList<Double> C, ArrayList<Double> B, int contraintNbr, int nbrVariableHorsBase, ArrayList<Integer> varV) {

        double Z2;
        double nbr = 0, min = 999999999, minNegatif = 999999999, max = -999999999, pivot = 0;
        String resultAffichage = "vide";
        int pivotC = -1, pivotL = -1;
        int lenght1 = nbrVariableHorsBase + nbrVarBase + nbrVarBaseNeg + nbrVarArtificial;
        ArrayList<Double> B2 = new ArrayList<Double>();
        ArrayList<Double> C2 = new ArrayList<Double>();
        //double[] result = new double[nbrVariableHorsBase];
        double[] result = new double[nbrVariableHorsBase];
        double[][] AF2 = new double[contraintNbr + 1][lenght1];

        //Si tous les coefficients sont positif ou null alors la solution courante est optimale.
        if (!siExisteV()) {
            double nouveauZ = B.get(B.size() - 1) * -1;
            B.remove(B.size() - 1);
            ArrayList<Double> nouveauC = new ArrayList<Double>();
            for (i = 0; i < lenght1; i++) {
                nouveauC.add(AF[contraintNbr][i] * -1);
            }
            nouveauC.remove(C.size() - 1);
            System.out.println("Z=" + Z);
            System.out.println("nouveauZ=" + nouveauZ);
            System.out.println(C);
            System.out.println("nouveauC" + nouveauC);
            System.out.println(B);
            System.out.println(Arrays.deepToString(AF));
            System.out.println(nomVariableV);
            System.out.println(varV);

            simplexMethod(maxMin, nouveauZ, AF, nouveauC, B, contraintNbr, nbrVariableHorsBase, varV);
            return;
        }
        if (nbrElementNegatif(C,C.size()) <= 0) {
            //affiher result
            for (i = 0; i < nbrVarBase; i++) {
                if (varV.get(i) < nbrVariableHorsBase + 1) {
                    result[i] = B.get(i);
             }
            }
        }

        for (i = 0; i < lenght1; i++) {
            if (C.get(i) < 0 && C.get(i) < minNegatif) {
                minNegatif = C.get(i);
                pivotC = i;
            }
        }
        if(pivotC==-1){
            resultAffichage = "Cette problem n'a pas de solution optimale.";
            new Affiche(resultAffichage);
            return;
        }

        //detterminier le plus grand coiffecient positif (C)
        //s'il n'existe pas au moins un element positif stop le probleme est non borne
        if (nbrElementPositif(getColumn(AF, contraintNbr, pivotC)) == 0) {
            resultAffichage = "le probleme est non borne.";
            new Affiche(resultAffichage);
            return;
        } //determiner le pivot
        else {
            for (i = 0; i < contraintNbr; i++) {
                nbr = B.get(i) / AF[i][pivotC];
                if (nbr > 0 && nbr < min) {
                    min = nbr;
                    pivotL = i;
                }
            }
            pivot = AF[pivotL][pivotC];
        }
        if (B.get(pivotL) == 0) {
            resultAffichage = "Cette problem n'a pas de solution optimale.";
            new Affiche(resultAffichage);
            return;
        }
        //sortir de variable 
        varV.set(pivotL, pivotC + 1);
        nomVariableV.set(pivotL, nomVariableH.get(pivotC));
        //remplire 2eme tableau
        for (i = 0; i < lenght1; i++) {
            AF2[pivotL][i] = AF[pivotL][i] / pivot;
        }
        //remplire C2

        Z2 = Z - minNegatif * min;

        for (i = 0; i < C.size(); i++) {
            C2.add(C.get(i) - C.get(pivotC) * AF2[pivotL][i]);
        }
        //remplir reste du A
        for (i = 0; i < contraintNbr + 1; i++) {
            if (i != pivotL) {
                B2.add(B.get(i) - AF[i][pivotC] * B.get(pivotL) / pivot);
                for (j = 0; j < lenght1; j++) {
                    AF2[i][j] = AF[i][j] - AF[i][pivotC] * AF2[pivotL][j];
                }
            } else {
                B2.add(B.get(pivotL) / pivot);
            }
        }

        //next iteration
        premierePhase(Z2, AF2, C2, B2, contraintNbr, nbrVariableHorsBase, varV);
    }

    private void simplexMethod(String maxMin, double Z, double[][] AF, ArrayList<Double> CF, ArrayList<Double> B, int contraintNbr, int nbrVariableHorsBase, ArrayList<Integer> varV) {

        double Z2;
        double nbr = 0, min = 999999999, minNegatif = 999999999, max = -999999999, pivot = 0;
        String resultAffichage = "vide";
        int pivotC = -1, pivotL = -1;
        int lenght1 = nbrVariableHorsBase + nbrVarBase + nbrVarBaseNeg;
        ArrayList<Double> B2 = new ArrayList<Double>();
        ArrayList<Double> C2 = new ArrayList<Double>();
        double[] result = new double[nbrVariableHorsBase];
        double[][] AF2 = new double[contraintNbr][lenght1];

        if (maxMin.equals("Max")) {
            //Si tous les coefficients sont n�gatif ou null alors la solution courante est optimale.
            if (nbrElementPositif(CF) <= 0) {
                //affiher result

                for (i = 0; i < contraintNbr; i++) {
                    if (varV.get(i) < nbrVariableHorsBase + 1) {

                        result[varV.get(i) - 1] = B.get(i);
            }
                }
                resultAffichage = "la solution courante est optimale.";
                resultAffichage = resultAffichage + "\nresult : " + Arrays.toString(result) + "\nZ : " + Z * -1;

                new Affiche(resultAffichage);
                return;
            }
            for (i = 0; i < CF.size(); i++) {
                if (CF.get(i) > 0 && CF.get(i) > max) {
                    max = CF.get(i);
                    pivotC = i;
                }
            }
        } else {
            //Si tous les coefficients sont positif ou null alors la solution courante est optimale.
            if (nbrElementNegatif(CF,lenght1) <= 0) {
                //affiher result
                for (i = 0; i < nbrVarBase; i++) {
                    if (varV.get(i) < nbrVariableHorsBase + 1) {
                        result[i] = B.get(i);
                    }
                }
                resultAffichage = "la solution courante est optimale.";
                resultAffichage = resultAffichage + "\nresult : " + Arrays.toString(result) + "\nZ : " + Z * -1;

                new Affiche(resultAffichage);
                return;
            }

            for (i = 0; i < lenght1; i++) {
                if (CF.get(i) < 0 && CF.get(i) < minNegatif) {
                    minNegatif = CF.get(i);
                    pivotC = i;
                }
            }
        }
        //detterminier le plus grand coiffecient positif (C)
        //s'il n'existe pas au moins un element positif stop le probleme est non borne
        if (nbrElementPositif(getColumn(AF, contraintNbr, pivotC)) == 0) {
            resultAffichage = "le problème est non borné.";
            new Affiche(resultAffichage);
            return;
        } //determiner le pivot
        else {
            for (i = 0; i < contraintNbr; i++) {
                nbr = B.get(i) / AF[i][pivotC];
                if (nbr > 0 && nbr < min) {
                    min = nbr;
                    pivotL = i;
                }
            }
            pivot = AF[pivotL][pivotC];
        }
        if (B.get(pivotL) == 0) {
            resultAffichage = "Cette problem n'a pas de solution optimale.";
            new Affiche(resultAffichage);
            return;
        }
        //sortir de variable 
        varV.set(pivotL, pivotC + 1);
        //remplire 2eme tableau
        for (i = 0; i < lenght1; i++) {
            AF2[pivotL][i] = AF[pivotL][i] / pivot;
        }
        //remplire C2
        if (maxMin.equals("Max")) {
            Z2 = Z - max * min;
        } else {
            Z2 = Z - minNegatif * min;
        }
        for (i = 0; i < lenght1; i++) {
            C2.add(CF.get(i) - CF.get(pivotC) * AF2[pivotL][i]);
        }
        //remplir reste du A
        for (i = 0; i < contraintNbr; i++) {
            if (i != pivotL) {
                B2.add(B.get(i) - AF[i][pivotC] * B.get(pivotL) / pivot);
                for (j = 0; j < lenght1; j++) {
                    AF2[i][j] = AF[i][j] - AF[i][pivotC] * AF2[pivotL][j];
                }
            } else {
                B2.add(B.get(pivotL) / pivot);
            }
        }

        //next iteration
        simplexMethod(maxMin, Z2, AF2, C2, B2, contraintNbr, nbrVariableHorsBase, varV);
    }
}