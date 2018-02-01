import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final static byte OPEN = 0x4;
    private final static byte TOPCNND = 0x2;
    private final static byte BOTCNND = 0x1;

    private boolean percolateState = false;
    private int tempCol;
    private int tempRow;
    private  final int size;
    private byte[][] stateMatrix;
    private final WeightedQuickUnionUF map;
    private int openSites;


    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException("n <= 0");

        openSites = 0;
        size = n;
        //CAN BE IMPROVED HERE TO [N][N]
        stateMatrix = new byte[n + 1][n + 1];
       
        map = new WeightedQuickUnionUF(n * n+1);

    }

    private void validate(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size) throw new IllegalArgumentException("row index i out of bounds");
    }

    private int xyTo1D(int i, int j) {

        int index = (i - 1) * size + j;
        return index;
    }
    private void getIndexes(int index){
        if ( (tempCol = index%size) == 0){
            tempRow = index/size;
        } else tempRow = (index/size)+1;
    }

    public void open(int row, int col) {

        validate(row, col);
        if (isOpen(row, col)) return;
        int index;
        byte status = 0x0;
       
        stateMatrix[row][col] |= OPEN;
        ++openSites;
       
        if (row == 1) {
            status |= TOPCNND;
        }
        if (row == size) {
            status |= BOTCNND;
        }

        if (row > 1 && (stateMatrix[row - 1][col] & OPEN) > 0) {
            index = map.find(xyTo1D(row-1, col));
            getIndexes(index);
            status |= stateMatrix[tempRow][tempCol];
            map.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        if (row < size && (stateMatrix[row + 1][col] & OPEN) > 0) {
            index = map.find(xyTo1D(row+1, col));
            getIndexes(index);
            status |= stateMatrix[tempRow][tempCol];
            map.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        if (col > 1 && (stateMatrix[row][col - 1] & OPEN) > 0 ){
            index = map.find(xyTo1D(row,col-1));
            getIndexes(index);
            status |= stateMatrix[tempRow][tempCol];
            map.union(xyTo1D(row, col), xyTo1D(row, col - 1));

        }
        if (col < size && (stateMatrix[row][col + 1] & OPEN) > 0) {
            index = map.find(xyTo1D(row,col+1));
            getIndexes(index);
            status |= stateMatrix[tempRow][tempCol];
            map.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
        index = map.find(xyTo1D(row, col));
        getIndexes(index);
        stateMatrix[tempRow][tempCol] |= status;
        if(stateMatrix[tempRow][tempCol] == 0x7){
            percolateState = true;
        }
    }

    public boolean isOpen(int row, int col) { 

        validate(row, col);
        return (stateMatrix[row][col] & OPEN) > 0;

    }

    public boolean isFull(int row, int col) {

        validate(row, col);
        int rootIndex = map.find(xyTo1D(row, col));
        getIndexes(rootIndex);
        return (stateMatrix[tempRow][tempCol] & TOPCNND) > 0;
    }

    public int numberOfOpenSites() {

        return openSites;
    }

    public boolean percolates() {        

        return percolateState;

    }

    public static void main(String[] args) {


        In in = new In("percolation/input10.txt");
        int n = in.readInt();
        Percolation test = new Percolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            System.out.println(i + " " + j);
            test.open(i, j);
            System.out.println("isOpen = " + test.isOpen(i, j));
            System.out.println("percolates = " + test.percolates());
            System.out.println("numOfSites = " + test.numberOfOpenSites());
            System.out.println("isFull = " + test.isFull(i, j));
        }
    }
}