/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.distances;

/**
 *
 * @author Gustavo Henrique
 */
@Deprecated
public class PearsonCoefficient {

    public double calculatePearsonCoeficient(double pointsX[], double pointsY[]) {
        double xMean = this.calculateMean(pointsX);
        double yMean = this.calculateMean(pointsY);

        double sum = 0;

        int n = pointsX.length;
        double stdX = calculateStdDev(pointsX);
        double stdY = calculateStdDev(pointsY);

        for (int i = 0; i < pointsX.length; i++) {
    //        System.out.println(i);
            double x = pointsX[i];
            double y = pointsY[i];
            double result = (x - xMean) * (y - yMean);
            sum += result;
        }
    //    System.out.println(sum);

        sum /= (n);
    //    System.out.println(sum);
        //    System.out.println(stdX);
        //   System.out.println(stdY);

        //System.out.println(stdX * stdY);
        double pearsonCoefficient = sum / (stdX * stdY);
        //      System.out.println(pearsonCoefficient);
        return pearsonCoefficient;

    }

    private double calculateMean(double points[]) {
        double mean = 0;
        for (Double d : points) {
            mean += d.doubleValue();
        }
        mean = mean / (double) points.length;
        return mean;
    }

    private double calculateStdDev(double points[]) {
        double stdDev;
        double mean = this.calculateMean(points);
        double var = 0;
        for (Double d : points) {
            var += Math.pow(d.doubleValue() - mean, (double) 2);// (x - mean)^2
        }
        var = var / (double) points.length;
        stdDev = Math.sqrt(var);
        return stdDev;

    }
}
