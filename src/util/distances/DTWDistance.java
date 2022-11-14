package util.distances;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class DTWDistance {

    private double windowLength;

    public DTWDistance(double size) throws Exception {
        if (size < 0) {
            throw new Exception("Window Length Error", new Throwable("Window Length must be greater than zero(0)"));
        }
        this.windowLength = size;
    }

    private double min(double vet[]) {
        return Math.min((Math.min(vet[0], vet[1])), vet[2]);
    }

    private String posMin(double vet[]) {
        if (vet[0] <= vet[1] && vet[0] < vet[2]) {
            return "left";
        } else {
            if (vet[1] < vet[2]) {
                return "up";
            }
        }
        return "diagonal";
    }

//    @Override
    public double distance(double[] val1, double[] val2) {
        int tam = val1.length;
        Double[] d1 = new Double[tam];
        Double[] d2 = new Double[tam];

        for (int i = 0; i < tam; i++) {
            d1[i] = val1[i];
            d2[i] = val2[i];
        }

        return distance(d1, d2);
    }

//    @Override
    public double distance(Double[] a, Double[] b) {

        int min, max;
        double[] vet = new double[3];

        // a = coluna
        // b = linhas
        if (a.length < b.length) {
            min = a.length;
            max = b.length;
        } else {
            min = b.length;
            max = a.length;
        }

        // Fills the matrix (Inverted matrix)
        double mat[][] = new double[min][max];
        for (int j = 0; j < max; j++) { // Fills the first row;
            if (j == 0) {
                mat[0][j] = Math.abs((double) b[j] - (double) a[j]);
            } else {
                mat[0][j] = Math.abs((double) b[0] - (double) a[j]) + (mat[0][j - 1]);
            }
        }

        for (int j = 1; j < min; j++) { // Fills the first column;
            mat[j][0] = Math.abs((double) a[0] - (double) b[j]) + (mat[j - 1][0]);
        }

        for (int i = 1; i < min; i++) { // Fills the rest of matrix;
            for (int j = 1; j < max; j++) {
                vet[0] = mat[i][j - 1];
                vet[1] = mat[i - 1][j];
                vet[2] = mat[i - 1][j - 1];
                mat[i][j] = Math.abs((double) b[i] - (double) a[j]) + this.min(vet); // Calculate distce of two values using Euclidian Distace;
            }
        }

        // Choose the best way
        //  |i - (n/ (m/j))| < R -----------> The corners of the matrix are pruned from consideration;
        //  D(i,j) + min{   (i-1,j-1) ; (i-1,j-2);   (i-2,j-1)   } ---------> move one diagonal step;
        //  D(i,j) + min{   (i-1,j-1) ; (i,j-1);   (i-1,j)   } --------->  move one to diagonal  step, to left or to down;
        int column = (max - 1);

        double sum = mat[min - 1][max - 1];
        for (int i = (min - 1); i >= 0; i--) {

            // Check if the position is the last;
            if (i == 0 && column == 0) {
                break;
            }

            // Check if position can be accessed;
            /* if(Math.abs(i - (column-1)) > this.windowLength){ 
             mat[i][column-1] = Double.POSITIVE_INFINITY;
             }
            
             if(Math.abs((i-1)-column) > this.windowLength){
             mat[i-1][column] = Double.POSITIVE_INFINITY; 
             //mat[i-1][column] = Double.POSITIVE_INFINITY;    
             }
             if(Math.abs((i-1) - (column-1)) > this.windowLength){
             mat[i-1][column-1] = Double.POSITIVE_INFINITY;
             }*/
            // Check if position don't exist and if can be accessed ;
            if (((column - 1) < 0) || (Math.abs(i - (column - 1)) > this.windowLength)) {
                vet[0] = Double.POSITIVE_INFINITY;
            } else {
                vet[0] = mat[i][column - 1];
            }

            if (((i - 1) < 0) || (column < 0) || (Math.abs((i - 1) - column)) > this.windowLength) {
                vet[1] = Double.POSITIVE_INFINITY;
            } else {
                vet[1] = mat[i - 1][column];
            }

            if (((i - 1) < 0) || ((column - 1) < 0) || (Math.abs((i - 1) - (column - 1)) > this.windowLength)) {
                vet[2] = Double.POSITIVE_INFINITY;
            } else {
                vet[2] = mat[i - 1][column - 1];
            }

            sum += this.min(vet);
            String aux = this.posMin(vet);
            if (aux.equals("left")) {
                i += 1;
                column -= 1;
            } else {
                if (aux.equals("diagonal")) {
                    column -= 1;
                } else {
                    if (i == 0) {
                        i += 1;
                    }
                }
            }

        }
        return (sum / (double) a.length); // Divide sum for reference time series;*/
    }

//    @Override
    public double distanceAccordingVariation(Double[] val1, Double[] val2, double radius) {
        return distance(val1, val2);
    }

//    @Override
    public double distanceAccordingVariation(double[] val1, double[] val2, double radius) {
        return distance(val1, val2);
    }

//    @Override
    public double calcDistance(Object obj1, Object obj2) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
