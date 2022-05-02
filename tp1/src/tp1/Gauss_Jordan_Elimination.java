package tp1;
import java.util.Scanner;
public class Gauss_Jordan_Elimination {
	 private static final double EPSILON = 1e-8;
	 
	    private final int N;      // N par N
	    private double[][] a;     // N par N+1 matrice augmentée
	 
	    // élimination de Gauss-Jordan avec pivotement partiel
	    public Gauss_Jordan_Elimination(double[][] A, double[] b) 
	    {
	        N = b.length;
	 
	        // construire une matrice augmentée
	        a = new double[N][N+N+1];
	        for (int i = 0; i < N; i++)
	            for (int j = 0; j < N; j++)
	                a[i][j] = A[i][j];
	 
	        // calculer l'inverse
	        for (int i = 0; i < N; i++)
	            a[i][N+i] = 1.0;
	 
	        for (int i = 0; i < N; i++) 
	            a[i][N+N] = b[i];
	 
	        solve();
	 
	        assert check(A, b);
	    }
	 
	    private void solve() 
	    {
	        // Gauss-Jordan elimination
	        for (int p = 0; p < N; p++) 
	        {
	            int max = p;
	            for (int i = p+1; i < N; i++) 
	            {
	                if (Math.abs(a[i][p]) > Math.abs(a[max][p])) 
	                {
	                    max = i;
	                }
	            }
	 
	            // échanger la ligne p avec la ligne max
	            swap(p, max);
	 
	            // singulier ou presque singulier
	            if (Math.abs(a[p][p]) <= EPSILON) 
	            {
	                continue;
	            }
	            pivot(p, p);
	        }
	    }
	 
	    // permuter row1 and row2
	    private void swap(int row1, int row2) 
	    {
	        double[] temp = a[row1];
	        a[row1] = a[row2];
	        a[row2] = temp;
	    }
	 
	    // pivot sur l'entrée (p, q) en utilisant l'élimination de Gauss-Jordan
	    private void pivot(int p, int q) 
	    {   
	        for (int i = 0; i < N; i++) {
	            double alpha = a[i][q] / a[p][q];
	            for (int j = 0; j <= N+N; j++) 
	            {
	                if (i != p && j != q) a[i][j] -= alpha * a[p][j];
	            }
	        }
	 
	        for (int i = 0; i < N; i++)
	            if (i != p) a[i][q] = 0.0;
	 
	        for (int j = 0; j <= N+N; j++)
	            if (j != q) a[p][j] /= a[p][q];
	        a[p][q] = 1.0;
	    }
	 
	    // extraire la solution pour Ax = b
	    public double[] primal() 
	    {
	        double[] x = new double[N];
	        for (int i = 0; i < N; i++) 
	        {
	            if (Math.abs(a[i][i]) > EPSILON)
	                x[i] = a[i][N+N] / a[i][i];
	            else if (Math.abs(a[i][N+N]) > EPSILON)
	                return null;
	        }
	        return x;
	    }
	 
	    // extraire la solution pour yA = 0, yb != 0
	    public double[] dual() 
	    {
	        double[] y = new double[N];
	        for (int i = 0; i < N; i++) 
	        {
	            if ( (Math.abs(a[i][i]) <= EPSILON) && (Math.abs(a[i][N+N]) > EPSILON) ) 
	            {
	                for (int j = 0; j < N; j++)
	                    y[j] = a[i][N+j];
	                return y;
	            }
	        }
	        return null;
	    }
	 
	    // le système a-t-il une solution ?
	    public boolean isFeasible() 
	    {
	        return primal() != null;
	    }
	 
	    // afficher le tableau
	    private void show() 
	    {
	        for (int i = 0; i < N; i++) 
	        {
	            for (int j = 0; j < N; j++) 
	            {
	                System.out.print(" "+a[i][j]);
	            }
	            System.out.print("| ");
	            for (int j = N; j < N+N; j++) 
	            {
	            	System.out.print(" "+a[i][j]);
	            }
	            System.out.print("| \n"+a[i][N+N]);
	        }
	        System.out.println();
	    }
	 
	 
	    // vérifier que Ax = b ou yA = 0, yb != 0
	    private boolean check(double[][] A, double[] b) 
	    {
	 
	        // vérifier que Ax = b
	        if (isFeasible()) 
	        {
	            double[] x = primal();
	            for (int i = 0; i < N; i++) 
	            {
	                double sum = 0.0;
	                for (int j = 0; j < N; j++) 
	                {
	                     sum += A[i][j] * x[j];
	                }
	                if (Math.abs(sum - b[i]) > EPSILON) 
	                {
	                	System.out.println("not feasible");
	                	System.out.println(i+" = "+b[i]+", sum = "+sum+"\n");
	                   return false;
	                }
	            }
	            return true;
	        }
	 
	        // ou yA = 0, yb != 0
	        else 
	        {
	            double[] y = dual();
	            for (int j = 0; j < N; j++) 
	            {
	                double sum = 0.0;
	                for (int i = 0; i < N; i++) 
	                {
	                     sum += A[i][j] * y[i];
	                }
	                if (Math.abs(sum) > EPSILON) 
	                {
	                    System.out.println("invalid certificate of infeasibility");
	                    System.out.println("sum = "+sum+"\n");
	                    return false;
	                }
	            }
	            double sum = 0.0;
	            for (int i = 0; i < N; i++) 
	            {
	                sum += y[i] * b[i];
	            }
	            if (Math.abs(sum) < EPSILON) 
	            {
	            	System.out.println("invalid certificate of infeasibility");
	            	System.out.println("yb  = "+sum+"\n");
	 
	                return false;
	            }
	            return true;
	        }
	    }
	 
	 
	    public static void test(double[][] A, double[] b) 
	    {
	        Gauss_Jordan_Elimination gaussian = new Gauss_Jordan_Elimination(A, b);
	        if (gaussian.isFeasible()) 
	        {
	        	System.out.println("Solution to Ax = b");
	            double[] x = gaussian.primal();
	            for (int i = 0; i < x.length; i++) 
	            {
	            	System.out.println(" "+x[i]+"\n");
	            }
	        }
	        else 
	        {
	        	System.out.println("Certificate of infeasibility");
	            double[] y = gaussian.dual();
	            for (int j = 0; j < y.length; j++) 
	            {
	            	System.out.println(" "+y[j]+"\n");
	            }
	        }
	        System.out.println();
	    }
	 
	    public static void main(String[] args) 
	    {
	 
	    	Scanner input = new Scanner(System.in);
	    	System.out.println("Entrez le nombre de variables dans les équations: ");
	    	int n = input.nextInt();
	        System.out.println("Entrez les coefficients de chaque variable pour chaque équation: ");
	        System.out.println("ax + by + cz + ... = d");
	        double [][]mat = new double[n][n];
	        double []constants = new double[n];
	        
	        for(int i=0; i<n; i++)
	        {
	            for(int j=0; j<n; j++)
	            {
	                mat[i][j] = input.nextDouble();
	            }
	            constants[i] = input.nextDouble();
	        }
	        test(mat, constants);          
	    }
	    
	 }

