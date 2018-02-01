import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private static final double CONST = 1.96;
    private double [] percolationThresholds;
    private final int trials;
    private final int n;
    private double mean;
    private double stddev;


    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        percolationThresholds = new double[trials];
        this.trials = trials;
        this.n = n;

        calculate();

    }

    private void calculate() {

        for (int i = 0; i < trials; ++i) {

            Percolation temp = new Percolation(n);
            while (!temp.percolates()) {

                int row = StdRandom.uniform(n)+1;
                int col = StdRandom.uniform(n)+1;


                temp.open(row, col);
            }
            percolationThresholds[i] = (double) temp.numberOfOpenSites()/(n*n);
        }
        mean = StdStats.mean(percolationThresholds);
        stddev = StdStats.stddev(percolationThresholds);
    }
   
    public double mean() {

        return mean;
    }
   
    public double stddev() {

        return  stddev;
    }
   
    public double confidenceLo() {

        return mean-(CONST*stddev)/Math.sqrt(trials);
    }
   
    public double confidenceHi() {
        return mean+((CONST*stddev)/Math.sqrt(trials));
    }
   
    public static void main(String[] args)  {

        In in = new In();

        int n = in.readInt();
        int t = in.readInt();

        PercolationStats test = new PercolationStats(n, t);

        System.out.printf("mean %20c %f\n", '=', test.mean());
        System.out.printf("stddev %18c %.16f\n", '=', test.stddev());
        System.out.printf("95%% confidence interval = [%.16f, %.16f]", test.confidenceLo(), test.confidenceHi());




    }
}