package tp1;
import java.util.ArrayList;

public class Dual_Simplex {
	double[][] A2; //matrice des coef des contraintes
    ArrayList<Double> C2 = new ArrayList<Double>();//list des coef du fonction objectif
    ArrayList<Double> B2 = new ArrayList<Double>();//list de valuers du B (partie doit des contraintes)
    int s = 1;
    String c;

    public Dual_Simplex(String maxMin, double[][] A, ArrayList<Double> C, ArrayList<Double> B, String[] comparaison, int contraintnmb, int nmbVarhorsbase) {

        A2 = new double[nmbVarhorsbase][contraintnmb];
        if (maxMin.equals("Max")) {
           
            maxMin = "Min";
            c = "<=";
           
            for (int i = 0; i < nmbVarhorsbase; i++) {

                B2.add(C.get(i));
            }

            for (int i = 0; i < contraintnmb; i++) {
               
                if (comparaison[i].equals(c)) {
                    s = 1;
                } else {
                    comparaison[i] = c;
                    s = -1;

                }
                C2.add(B.get(i) * s);

                for (int j = 0; j < nmbVarhorsbase; j++) {
                    A2[j][i] = A[i][j] * s;
                }
            }
            for(int i = 0; i < nmbVarhorsbase; i++){
                comparaison[i] = c;
            }
            
            int t = contraintnmb;
            contraintnmb = nmbVarhorsbase;
            nmbVarhorsbase = t;

            new Simplex("Min", A2, C2, B2, comparaison, contraintnmb, nmbVarhorsbase);
        } else {
            c = ">=";
            new Affiche("dans ce cas on a entrer dans la dexieme phase de dual simplex");
        }

    }
}
