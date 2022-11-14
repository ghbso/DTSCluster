/*
 * Josenildo Silva (copy left) 2009
 */
package model.patterndiscovery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.distances.Distance;
import util.distances.ManhattanDistance;
import util.exceptions.WLargerThanTimeSeriesSizeException;
import util.exceptions.WNegativeException;
import util.timeseries.TimeSeriesOperation;

/**
 * TimeSeries class models the basic data type in a timeseries application. It
 * consists of a Vector of reals.
 *
 * @author Josenildo
 */
public class TimeSeries implements Iterable<Double> {

    List<Double> dataPoints;

    /**
     * Creates a TimeSeries object given a vector of reals.
     *
     * @param dataPoints is a set of double values
     * @throws Exception if occurs any exception
     */
    public TimeSeries(List<Double> dataPoints) throws Exception {
        this.dataPoints = dataPoints;
    }

    /**
     * getter da classe TimeSeries
     *
     * @return a Vector of reals.
     */
    public List<Double> getDataPoints() {
        return this.dataPoints;
    }

    /**
     * Used to assign a dataset to a TimeSeries object different from the
     * dataset used at the object creation.
     *
     * @param dataPoints is a set of double values
     */
    public void setDataPoints(List<Double> dataPoints) {
        this.dataPoints = dataPoints;
    }

    /*
     * Returns the size of the time series.
     */
    /**
     *
     * @return number of time series points
     */
    public int size() {
        return this.dataPoints.size();
    }

    /**
     * @see reduceDimension(int n, int w)
     */
    /*public TimeSeries2 reduceDimension(int w) throws IOException, Exception {
     // Get n, which is the size of the original time series
     int n = this.dataPoints.size();
     return reduceDimension(n, w);
     }*/
    /**
     * Transform this time series object in another one which has only fewer
     * points, preserving the overall shape. It is known as PAA transformation
     * and was proposed by E. Keogh and colleagues. The ideia is to generate w
     * values from each time series subsequence of size n. Each w is a mean
     * values of n/w values.
     *
     * @param n is the length of each subsequence in the original time series.
     * @param w is the length of a subsequence in the transformed time series.
     * @return a Vector of reals representing a reduced time series.
     */
    private TimeSeries reduceDimension(int n, int w) throws WNegativeException, WLargerThanTimeSeriesSizeException {
        // Check parameters
        if (n <= 0) {
            return null;
        }
        if (n > this.dataPoints.size()) {
            n = this.dataPoints.size();
        }

        if (w > n) {
            throw new WLargerThanTimeSeriesSizeException();
        }
        if (w < 0) {
            throw new WNegativeException();
        }

        int subSize = (int) Math.floor(n / w);
        int numOfMeans = (int) Math.floor(this.size() / subSize);
//        System.out.println(n + " " + w + " " + subSize + " " + numOfMeans);
        List<Double> meanValues = new ArrayList<Double>();
        //Compute each point of the w points of the reduced time series 
        for (int i = 0; i < numOfMeans; i++) {
            //For each of the w points ...

            // Get a subsequence of size given by subSize=(n/w).
            // Note that subList gets a sub list from i*(subSize) to (i+1)*subSize exclusive!
            List<Double> currSubSeq = (List<Double>) this.dataPoints.subList(i * (subSize), (i + 1) * subSize);
            //Compute the mean value
            double sum = 0;
            for (int j = 0; j < subSize; j++) {
                sum += currSubSeq.get(j);
            }// end for j
            //System.out.println(sum + " " + subSize);
            double mean = sum / subSize;

            //Add the new mean to Vector of means
            meanValues.add(new Double(mean));
        }// end for i
        //Return the reduced time series object.
        TimeSeries reducedTS = null;
        try {
            reducedTS = new TimeSeries(meanValues); // TODO use new TimeSeries(reducedDataPoints);
        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reducedTS;
    }

    public TimeSeries increaseDimension(int w) throws Exception {
        if (w < size()) {
            throw new Exception("W must be higher than time series dimension");
        }
        List<Double> points = new ArrayList<>();
        int qntNewPoints = w - size();
        int avgSize = (int) Math.ceil(size() / (double) qntNewPoints);
        int size = size();
//        System.out.println(avgSize);
        for (int i = 0; i < size; i++) {
            double sum = 0;
            int newI = i;

            if ((i + avgSize - 1) < size) {
                List<Double> pointToBeAdd = new ArrayList<>();
                for (int j = i; j < (i + avgSize); j++) {
//                System.out.println(j);
                    sum += dataPoints.get(j);
                    if (j != i || i == 0) {
                        pointToBeAdd.add(dataPoints.get(j));
                    }
                    newI = j;
                }
                i = newI - 1;
                double mean = sum / avgSize;
                int indexHalf = (avgSize / 2) - 1;
                if (i == 0) {
                    indexHalf += 1;
                }
                for (int j = 0; j < pointToBeAdd.size(); j++) {
                    if (j == indexHalf) {
                        points.add(mean);
                    }
                    points.add(pointToBeAdd.get(j));

                }
            }
        }
        System.out.println(points);
//        System.out.println(points.size());
        return new TimeSeries(points);

    }

    /*public TimeSeries2 discretize(double discrAmount) throws Exception {
     // between min and max or q1 and q3?
     List<Double> lst = new ArrayList<>();

     for (Double d : this.dataPoints) {
     System.out.println(d / discrAmount + " " + Math.floor(d / discrAmount));
     double i = Math.floor(d / discrAmount);
     lst.add(i);
     }

     return new TimeSeries2(lst);
     }*/
    public TimeSeries discretize(double discrAmount, boolean isToStandardize) throws Exception {
        // between min and max or q1 and q3?
        List<Double> lst = new ArrayList<>();

        int index = 0;
        for (Double d : this.dataPoints) {
            double i;
            if (isToStandardize) {
                /*System.out.println((d) + " * " + discrAmount + " == "
                 + (d * discrAmount) + " == Math.floor == "
                 + Math.floor(d * discrAmount));*/
                i = Math.floor(d * discrAmount);
            } else {
                double div = d / discrAmount;
//                System.out.println(div);
//                System.out.println(div + " " + d + " " + discrAmount);
                i = Math.floor(div);
//                System.out.println(div + " " + i);
            }

            index++;
            lst.add(i);
        }

        return new TimeSeries(lst);
    }

    public TimeSeries discretize(double discrAmount) throws Exception {
        // between min and max or q1 and q3?
        List<Double> lst = new ArrayList<>();

        for (Double d : this.dataPoints) {
            double i;
            double div = d / discrAmount;
            i = Math.floor(div);

            lst.add(i);
        }

        return new TimeSeries(lst);
    }

    public TimeSeries discretizeWithDecimalPlace(double discrAmount, boolean isToStandardize) throws Exception {
        // between min and max or q1 and q3?
        List<Double> lst = new ArrayList<>();

        for (Double d : this.dataPoints) {
            double i;
            if (isToStandardize) {
                /*System.out.println((d) + " * " + discrAmount + " == "
                 + (d * discrAmount) + " == Math.floor == "
                 + Math.floor(d * discrAmount));*/
                i = d * discrAmount;
                i = new Float(Math.round(i)).doubleValue();
//                System.out.println("oi");
            } else {

                double div = d / discrAmount;
                i = new Float(Math.round(div)).doubleValue();

            }

            lst.add(i);
        }


        return new TimeSeries(lst);
    }

    public TimeSeries discretize2(double discrAmount, boolean isToStandardize) throws Exception {
        // between min and max or q1 and q3?
        List<Double> lst = new ArrayList<>();

        int index = 0;
        int qntBreakPoints = 0;
        if (isToStandardize) {
            Double min = Math.floor(min() / discrAmount);
            min *= discrAmount;
            Double max = Math.ceil(max() / discrAmount);
            max *= discrAmount;

            System.out.println(min() + " " + max);
            double size = Math.floor(max()) - Math.floor(min());
            Double value = Math.ceil(size / discrAmount);
            qntBreakPoints = value.intValue();

            List<Double> breakPoints = new ArrayList<>();

            double mean = getMean();
            double stdDev = getStdDev();

//            System.out.println(mean + " " + stdDev);
            for (double i = min; i <= max; i += discrAmount) {
                double val = (i - mean) / stdDev;
                breakPoints.add(val);
            }

            TimeSeries tsNormalized = normalize(size());

//            System.out.println(tsNormalized.getDataPoints());
            for (Double d : tsNormalized.getDataPoints()) {
                double val = -1;
                if (d < breakPoints.get(0)) {
                    val = Math.floor(breakPoints.get(0));
                } else if (d > breakPoints.get(breakPoints.size() - 1)) {
                    val = Math.floor(breakPoints.size() - 1);

                } else {
                    for (int i = 1; i < breakPoints.size(); i++) {
//                        System.out.println( d + " " + breakPoints.get(i - 1)  + " " + breakPoints.get(i));
                        if ((d >= breakPoints.get(i - 1)) && (d < breakPoints.get(i))) {
//                            System.out.println(d + " " + breakPoints.get(i) + " " + breakPoints.get(i).intValue());
                            val = breakPoints.get(i).intValue();
//                            System.out.println(d + " " + val + " " + breakPoints.get(i));
//                            break;
                        }

                    }
//                    System.out.println(d + " " + val);
                }
//                System.out.println(d + " " + val);
                lst.add(val);
            }
//            System.out.println(lst);
        } else {
            for (Double d : this.dataPoints) {
                double i;
                if (isToStandardize) {
                    i = Math.floor(d * discrAmount);
                } else {
                    double div = d / discrAmount;
                    i = Math.floor(div);
                }

                index++;
                lst.add(i);
            }
        }
        return new TimeSeries(lst);
    }

    public TimeSeries discretize3(double discrAmount,
            Double minOriginal,
            Double maxOriginal,
            Double mean,
            Double stdDev) throws Exception {
        List<Double> lst = new ArrayList<>();

        Double min = Math.floor(minOriginal / discrAmount);
        min *= discrAmount;
        Double max = Math.ceil(maxOriginal / discrAmount);
        max *= discrAmount;

        System.out.println(min + " " + max);

        Double minNorm = (min - mean) / stdDev;
        Double maxNorm = (max - mean) / stdDev;
        Double discAmountNorm = discrAmount / stdDev;

        System.out.println(minNorm + " " + maxNorm);

        List<Double> breakPoints = new ArrayList<>();
        for (double i = minNorm; i <= maxNorm; i += discAmountNorm) {
            double val = i;
//            System.out.println(i + " " + val);
            breakPoints.add(val);
        }
        System.out.println(breakPoints);
        for (Double d : getDataPoints()) {
            double val = -1;
            if (d < breakPoints.get(0)) {
                val = breakPoints.get(0);
            } else if (d > breakPoints.get(breakPoints.size() - 1)) {
                val = breakPoints.get(breakPoints.size() - 1);

            } else {
                for (int i = 1; i < breakPoints.size(); i++) {
//                        System.out.println( d + " " + breakPoints.get(i - 1)  + " " + breakPoints.get(i));
                    if ((d >= breakPoints.get(i - 1)) && (d < breakPoints.get(i))) {
//                            System.out.println(d + " " + breakPoints.get(i) + " " + breakPoints.get(i).intValue());
                        val = breakPoints.get(i);
//                            System.out.println(d + " " + val + " " + breakPoints.get(i));
//                            break;
                    }

                }
//                    System.out.println(d + " " + val);
            }
//            System.out.println(d + " " + val);
            lst.add(val);
        }
//            System.out.println(lst);
        return new TimeSeries(lst);
    }

    public TimeSeries discretize4(int numberOfRegions, boolean isToStd) throws Exception {
        List<Double> points = new ArrayList<>();
        if (isToStd) {

            double min = 0;
            double max = 1;

            double step = (max - min) / numberOfRegions;
            List<Double> breakPoints = new ArrayList<>();
            for (double i = step; i < max; i += step) {
                breakPoints.add(i);
            }
            System.out.println(breakPoints);
            for (Double d : getDataPoints()) {
                double val = -1;
                if (d < breakPoints.get(0)) {
                    val = 0;
                } else if (d > breakPoints.get(breakPoints.size() - 1)) {
                    val = breakPoints.size() - 1;

                } else {
                    for (int i = 1; i < breakPoints.size(); i++) {
//                        System.out.println( d + " " + breakPoints.get(i - 1)  + " " + breakPoints.get(i));
                        if ((d >= breakPoints.get(i - 1)) && (d < breakPoints.get(i))) {
//                            System.out.println(d + " " + breakPoints.get(i) + " " + breakPoints.get(i).intValue());
                            val = i;
//                            System.out.println(d + " " + val + " " + breakPoints.get(i));
//                            break;
                        }

                    }
                }
                System.out.println(d + " " + val);
                points.add(val);
            }

        } else {

        }
        return new TimeSeries(points);
    }

    public TimeSeries discretize5(int numberOfRegions) throws Exception {
        List<Double> points = new ArrayList<>();

        List<Double> breakPoints = TimeSeriesOperation.calcBreakPoints(numberOfRegions);
        for (Double d : getDataPoints()) {
            double val = -1;
            if (d < breakPoints.get(0)) {
                val = 0;
            } else if (d > breakPoints.get(breakPoints.size() - 1)) {
                val = numberOfRegions - 1;
            } else {
                for (int i = 1; i < breakPoints.size(); i++) {
                    if ((d >= breakPoints.get(i - 1)) && (d < breakPoints.get(i))) {
                        val = i;
                    }

                }
            }
//            System.out.println(d + " " + val);
            points.add(val);
        }

        return new TimeSeries(points);
    }

    /**
     * Compute a symbolic version of a (reduced) time series for a given
     * alphabet. Normally used on reduced time serie, but not limited to.
     *
     * @param alphabet is the alphabet used to transform integer values into
     * characters
     * @return a String which is a simbolic version of a time series for a given
     * alphabet.
     * @throws Exception if the alphabet is less than 2
     */
    public StringBuffer discretize(String alphabet) throws Exception {
        //Init
        List<Double> breakPoints = new ArrayList<Double>();
        int alphaSize = alphabet.length();
        StringBuffer symbolic = new StringBuffer(); // holds a simbolic version of a time series.
        //Check parameter
        if (alphaSize < 2) {
            throw new Exception("Alphabet Error", new Throwable("Alphabet must have at least two symbols"));
        }//endif

        // Compute breakpints
        //breakpoints = [breakpoints _saxBreakPoints(alphasize)];
        breakPoints.addAll(TimeSeriesOperation.saxBreakPoints(alphaSize));
        // TODO drop assumption that break points are in the z-curve
        // or we will need to standardize data to use these breakpoints
        // TODO optimize this symbol substitution from O(n^2) to anything 
        // more time efficient.
        //   System.out.println(this.size());  
        for (int i = 0; i < this.size(); i++) {//for i=1:length(paa)
            //     System.out.println(this.getDataPoints().get(i));
            //        DecimalFormat df = new DecimalFormat("#");
            //        df.setMaximumFractionDigits(20);
            //        System.out.println(this.getDataPoints().get(i) + " " + df.format(this.getDataPoints().get(i)));

            if (this.getDataPoints().get(i) < -0.6744897501960818) {
                //          System.out.println(this.getDataPoints().get(i) + " " + breakPoints);
            }
            for (int j = 0; j < alphaSize; j++) {//  for =1:alphasize+1
                if ((breakPoints.get(j) <= (this.getDataPoints().get(i)))
                        && (this.getDataPoints().get(i) < breakPoints.get(j + 1))) {//     if ((breakpoints(j)<=paa(i)) && (paa(i)<breakpoints(j+1)))

                    //TODO substitute String by list of char
                    char[] data = {alphabet.charAt(j)};
                    String newdata = new String(data);

                    symbolic.append(newdata);//sax(i)=alphabet(j);
                    break;
                }//     endif;   
            }//  endfor;
            //         break;
        }//endfor;

        return symbolic;
    }

    /*public TimeSeries discretizeNumericAlphabet(int alphaSize) throws Exception {
     //Init
     List<Double> breakPoints = new ArrayList<Double>();

     List<Double> values = new ArrayList<>();

     //Check parameter
     if (alphaSize < 2) {
     throw new java.lang.Exception("Alphabet Error", new Throwable("Alphabet must have at least two symbols"));
     }//endif

     // Compute breakpints
     //breakpoints = [breakpoints _saxBreakPoints(alphasize)];
     breakPoints.addAll(this.saxBreakPoints(alphaSize));
     List<Double> numericAlphabet = getNumericAlphabet(alphaSize);
     alphaSize = numericAlphabet.size();
     // TODO drop assumption that break points are in the z-curve
     // or we will need to standardize data to use these breakpoints
     // TODO optimize this symbol substitution from O(n^2) to anything 
     // more time efficient.
     for (int i = 0; i < this.size(); i++) {//for i=1:length(paa)
     for (int j = 0; j < alphaSize; j++) {//  for =1:alphasize+1
     if ((breakPoints.get(j) < (this.getDataPoints().get(i)))
     && (this.getDataPoints().get(i) < breakPoints.get(j + 1))) {//     if ((breakpoints(j)<=paa(i)) && (paa(i)<breakpoints(j+1)))

     //TODO substitute String by list of char
     double d = numericAlphabet.get(j);

     values.add(d);//sax(i)=alphabet(j);

     break;
     }//     endif;   
     }//  endfor;
     }//endfor;
     return new TimeSeries(values);
     }*/
    /**
     * Compute break points used to discretize a time series according to HotSAX
     * algorithm proposed by Eamon Keogh et. al. Returns the points under the
     * Z-curve which gives equiprobable regions for a given number of regions
     * under a normal probability density function.
     *
     * @param number of regions under the z-curve
     * @return a set of reals reprenting break points of the Z-curve of a Normal
     * distribuition.
     */
    /**
     * Creates vectors of size n from the current time series
     *
     * @param n, a integer indicating the dimension of each data point (vector)
     * @return DataSet
     * @throws Exception if time series size is less than n value
     */
    public DataSet toDataSet(int n) throws Exception {
        // subquences should have dimension less than time series size
        int tsize = this.size();
        if (tsize < n) {
            throw new Exception("subquences have dimension" + n + " and time series has size" + tsize);
        }

        List<DataPoint> points = new ArrayList<DataPoint>();
        Distance df = new ManhattanDistance();

        try {
            for (int i = 0; i < tsize; i += n) {// creates a jumping window of size n
                List l = this.getDataPoints().subList(i, i + n);
                DataPoint dp;
                double[] values = new double[n];

                for (int j = 0; j < l.size(); j++) {
                    values[j] = ((Double) l.get(j)).doubleValue();
                }
                dp = new DataPoint(values, df);
                points.add(dp);
            }

            DataSet ds = new DataSet(points);
            return ds;

        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Normalizes the time series with a given mean and variation, such that the
     * new mean is 0 and variation is 1. This method is parametric to enable the
     * normalization with non-local datasets, as occurs in a distributed
     * environment.
     *
     * @param mean sample average of data points in the time series collection,
     * considering all non local datasets distributed though other peers.
     * @param stdDev sample standard deviation, considering all non local
     * datasets.
     * @return a normalized time series for given mean and variation.
     */
    private TimeSeries standardize(double mean, double stdDev) {
        TimeSeries tsn = null; // standardized time series
        List<Double> stdVals = new ArrayList<Double>();
        for (Iterator<Double> it = this.dataPoints.iterator(); it.hasNext();) {
            Double dp = it.next();
            if (stdDev == 0) {
                stdVals.add(0d);
            } else {
                double val = (dp.doubleValue() - mean) / stdDev;
                stdVals.add(val);
            }
        }
        try {
            tsn = new TimeSeries(stdVals);
            return tsn;
        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
            return tsn;
        }

    }

    /**
     *
     * @param mean is the mean
     * @param stdDev is the standard deviation
     * @return the time series non-standardized
     */
    public TimeSeries unStandardize(double mean, double stdDev) {
        TimeSeries tsn = null; // standardized time series
        List<Double> stdVals = new ArrayList<Double>();
        for (Iterator<Double> it = this.dataPoints.iterator(); it.hasNext();) {
            Double dp = it.next();
            if (stdDev == 0) {
                stdVals.add(0d);
            } else {
                double val = (stdDev * dp.doubleValue()) + mean;
                stdVals.add(val);
            }
        }
        try {
            tsn = new TimeSeries(stdVals);
            return tsn;
        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
            return tsn;
        }

    }

    /*public TimeSeries standardizeAndDiscretize(int n, double discrAmount, boolean isToStandardize) throws Exception {
     List<Double> points = new ArrayList<>();

     if (isToStandardize) {
     //System.out.println("Normalizando");
     for (int i = 0; i <= this.dataPoints.size() - n; i += n) {
     TimeSeries subSeq = this.subSequence(i, i + n);
     double mean = subSeq.getMean();
     double stdDev = subSeq.getStdDev();
     TimeSeries tsStd = subSeq.standardize(mean, stdDev);
     TimeSeries tsDisc = tsStd.discretize(discrAmount, isToStandardize);
     points.addAll(tsDisc.getDataPoints());
     }
     } else {
     points = this.discretize(discrAmount).getDataPoints();
     }
     //System.out.println(points);
     TimeSeries discret = new TimeSeries(points);
     return discret;

     }*/
    /**
     *
     * @param n is the size of a subsequence of the time series, used in the
     * reduction process
     * @param w is the size of the subsequences. The string is split in slices
     * of size w in the density estimation step.
     * @param alphabet is the alphabet used to discretize the time series
     * @param isToStandardize is the flag that indicates if it's necessary to
     * standardize the time series
     * @return the time series discretized
     */
    public StringBuffer reduceAndDiscretize(int n, int w, String alphabet, boolean isToStandardize) {
        try {
            TimeSeries tsRed = this.reduceTS(n, w, isToStandardize);
            StringBuffer discret = tsRed.discretize(alphabet);
            return discret;
        } catch (WNegativeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WLargerThanTimeSeriesSizeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Deprecated
    public TimeSeries reduceAndDiscretize(int n, int w, int s, double discAmount, boolean isToStandardize) {

        try {

            TimeSeries tsRed = this.reduceTS(n, w, s, isToStandardize);
            TimeSeries discret = tsRed.discretize(discAmount, isToStandardize);
            return discret;
        } catch (WNegativeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WLargerThanTimeSeriesSizeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     *
     * @param n is the size of a subsequence of the time series, used in the
     * reduction process
     * @param w is the size of the subsequences. The string is split in slices
     * of size w in the density estimation step
     * @param discAmount is the value used to discretize the time series using
     * integer alphabet
     * @param isToStandardize is the flag that indicates if it's necessary to
     * standardize the time series
     * @return the time series discretized
     */
    public TimeSeries reduceAndDiscretize(int n, int w, double discAmount, boolean isToStandardize) {

        try {

            TimeSeries tsRed = this.reduceTS(n, w, isToStandardize);
//            System.out.println(tsRed.subSequence(0, w).getDataPoints());
//            System.out.println(tsRed.subSequence(20, 20+w).getDataPoints());

            TimeSeries discret = tsRed.discretize(discAmount, isToStandardize);
//            System.out.println(discret.subSequence(0, w).getDataPoints());
//            System.out.println(discret.subSequence(20, 20+w).getDataPoints());
            return discret;
        } catch (WNegativeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WLargerThanTimeSeriesSizeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public TimeSeries reduceAndDiscretize2(int n, int w, double discAmount, boolean isToStandardize) throws Exception {

        if (isToStandardize) {
            List<Double> points = new ArrayList<>();
            List<Double> means = new ArrayList<>();
            List<Double> sdtDevs = new ArrayList<>();
            List<Double> mins = new ArrayList<>();
            List<Double> maxs = new ArrayList<>();
            for (int i = 0; i <= this.dataPoints.size() - n; i += n) {
                //System.out.println(i);

                TimeSeries subSeq = this.subSequence(i, i + n);
                //     System.out.println(subSeq.size());
                double mean = subSeq.getMean();
                double stdDev = subSeq.getStdDev();
                double min = subSeq.min();
                double max = subSeq.max();

                means.add(mean);
                sdtDevs.add(stdDev);
                mins.add(min);
                maxs.add(max);

                TimeSeries tsStd = subSeq.standardize(mean, stdDev);
                points.addAll(tsStd.getDataPoints());
            }

            TimeSeries tsStd = new TimeSeries(points);
            TimeSeries tsRed = tsStd.reduceDimension(n, w);

//            System.out.println(tsRed.subSequence(0, 7).getDataPoints());
            int index = 0;
            List<Double> pointsDisc = new ArrayList<>();

            for (int i = 0; i <= tsRed.dataPoints.size() - w; i += w) {
                TimeSeries subSeq = tsRed.subSequence(i, i + w);
                System.out.println(subSeq.getDataPoints());
                double mean = means.get(index);
                double stdDev = sdtDevs.get(index);
//                double min = subSeq.min();
                double min = mins.get(index);
//                double min = 0;
//                double max = subSeq.max();
                double max = maxs.get(index);
//                double max = 10000;

//                System.out.println(min + " " + max + " " + mean + " " + stdDev);
                System.out.println("Disc....");
                TimeSeries tsDisc = subSeq.discretize3(discAmount, min, max, mean, stdDev);
                pointsDisc.addAll(tsDisc.getDataPoints());
                break;
            }
            return new TimeSeries(pointsDisc);
        } else {
            TimeSeries tsRed = this.reduceDimension(n, w);
            TimeSeries discret = tsRed.discretize(discAmount);
            return discret;

        }

    }

    public TimeSeries reduceAndDiscretize3(int n, int w, int numberOfRegions, boolean isToStandardize) throws Exception {

        if (isToStandardize) {

            TimeSeries tsStd = normalize(n);
            TimeSeries tsRed = tsStd.reduceDimension(n, w);

//            System.out.println(tsRed.subSequence(0, 7).getDataPoints());
            int index = 0;
            List<Double> pointsDisc = new ArrayList<>();

            for (int i = 0; i <= tsRed.dataPoints.size() - w; i += w) {
                TimeSeries subSeq = tsRed.subSequence(i, i + w);
                System.out.println("Disc....");
                TimeSeries tsDisc = subSeq.discretize4(numberOfRegions, isToStandardize);
                pointsDisc.addAll(tsDisc.getDataPoints());
                break;
            }
            return new TimeSeries(pointsDisc);
        } else {
            TimeSeries tsRed = this.reduceDimension(n, w);
//            TimeSeries discret = tsRed.discretize(discAmount);
            return tsRed;

        }

    }

    public TimeSeries reduceAndDiscretize4(int n, int w, int alphaSize, boolean isToStandardize) {

        try {

            TimeSeries tsRed = this.reduceTS(n, w, isToStandardize);
            TimeSeries discret = tsRed.discretize5(alphaSize);
            return discret;
        } catch (WNegativeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WLargerThanTimeSeriesSizeException ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Deprecated
    public TimeSeries reduceTS(int n, int w, int s, boolean isToStandardize) throws Exception {
        TimeSeries tsStd = this;
        if (isToStandardize) {
            tsStd = this.normalize(n * s);
        }
        TimeSeries tsRed = tsStd.reduceDimension(n, w);

        return tsRed;
    }

    /**
     *
     * @param n is the size of a subsequence of the time series, used in the
     * reduction process
     * @param w is the size of the subsequences. The string is split in slices
     * of size w in the density estimation step
     * @param isToStandardize is the flag that indicates if it's necessary to
     * standardize the time series
     * @return the time series reduced
     * @throws Exception if occurs any exception
     */
    public TimeSeries reduceTS(int n, int w, boolean isToStandardize) throws Exception {
        TimeSeries tsStd = this;
        if (isToStandardize) {
            tsStd = this.normalize(n);
            //tsStd = this.normalize();
        }
        TimeSeries tsRed = tsStd.reduceDimension(n, w);
//        System.out.println(tsRed.subSequence(0, 4).getDataPoints());
        return tsRed;
    }

    /**
     *
     * @param n is the size of a subsequence of the time series, used in the
     * reduction process
     * @return the time series normalized
     * @throws Exception if occurs any exception
     */
    public TimeSeries normalize(int n) throws Exception {
        List<Double> points = new ArrayList<>();
        if (n > this.size()) {
            n = this.size();
        }
        for (int i = 0; i <= this.dataPoints.size() - n; i += n) {
            //System.out.println(i);

            TimeSeries subSeq = this.subSequence(i, i + n);
            //     System.out.println(subSeq.size());
            double mean = subSeq.getMean();
            double stdDev = subSeq.getStdDev();
            TimeSeries tsStd = subSeq.standardize(mean, stdDev);
            points.addAll(tsStd.getDataPoints());
        }
        return new TimeSeries(points);
    }

//    public TimeSeries unormalize(int n, int w) throws Exception {
//
//        List<Double> points = new ArrayList<>();
//        for (int i = 0; i <= this.dataPoints.size() - n; i += n) {
//            //System.out.println(i);
//
//            TimeSeries subSeq = this.subSequence(i, i + n);
//            //     System.out.println(subSeq.size());
//            double mean = subSeq.getMean();
//            double stdDev = subSeq.getStdDev();
//            TimeSeries tsStd = subSeq.unStandardize(mean, stdDev);
//            points.addAll(tsStd.getDataPoints());
//        }
//        return new TimeSeries(points);
//
//    }
    /*public StringBuffer standardizeAndDiscretize(int s, String alphabet, boolean isToStandardize) throws Exception {
     StringBuffer discret = new StringBuffer();

     if (isToStandardize) {
     for (int i = 0; i <= this.dataPoints.size() - s; i += s) {
     TimeSeries subSeq = this.subSequence(i, i + s);
     double mean = subSeq.getMean();
     double stdDev = subSeq.getStdDev();
     TimeSeries tsStd = subSeq.standardize(mean, stdDev);
     discret.append(tsStd.discretize(alphabet));

     }
     } else {
     discret = this.discretize(alphabet);

     }

     return discret;
     }*/
    /**
     *
     * @return the time series mean
     */
    public double getMean() {
        double mean = 0;
        for (Iterator<Double> it = this.dataPoints.iterator(); it.hasNext();) {
            Double d = it.next();
            //System.out.println(d);
            mean += d.doubleValue();
        }
        //System.out.println(mean + " " + dataPoints.size());
        mean = mean / (double) this.dataPoints.size();
        return mean;
    }

    /**
     *
     * @return the time series standard deviation
     */
    public double getStdDev() {
        double stdDev;
        double mean = this.getMean();
        double var = 0;
        for (Iterator<Double> it = this.dataPoints.iterator(); it.hasNext();) {
            Double d = it.next();
            var += Math.pow(d.doubleValue() - mean, (double) 2);// (x - mean)^2
        }
        var = var / (double) this.dataPoints.size();
        stdDev = Math.sqrt(var);
        return stdDev;

    }

    boolean concat(TimeSeries ts) {
        return this.dataPoints.addAll(ts.getDataPoints());
    }

    /**
     * Generates a list of subsequences of this time series
     *
     * @param size is the size of a given split. It need to be multiple of the
     * size of this time series. If not, the last split will not be returned in
     * the list.
     * @return a list of subsequences of this time series
     */
    public List<TimeSeries> split(int size) {
        List<TimeSeries> aList = new ArrayList<TimeSeries>();
        for (int i = 0; i <= this.size() - size; i += size) {
            try {
                aList.add(new TimeSeries(this.dataPoints.subList(i, i + size)));
            } catch (Exception ex) {
                Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
                aList = null;
            }
        }
        return aList;
    }

    /**
     *
     * @return time series in string format
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < this.size(); i++) {
            s.append((Double) this.dataPoints.get(i));
            s.append("\n");
        }
        return s.toString();
    }

    /**
     *
     * @param ini is the inital position
     * @param fim is the last position
     * @return the time series including the points between the specified
     * positions
     */
    public TimeSeries subSequence(int ini, int fim) {
        TimeSeries temp = null;
        try {
            temp = new TimeSeries(this.dataPoints.subList(ini, fim));
        } catch (Exception ex) {
            //Logger.getLogger(TimeSeries.class.getName()).log(Level.SEVERE, null, ex);
            temp = null;
        }
        return temp;
    }

    /**
     * Returns the min value of this time series.
     *
     * @return the minimum value of this time series
     */
    public Double min() {
        Double min = this.dataPoints.get(0);

        for (Double d : this.dataPoints) {
            if (min > d) {
                min = d;
            }
        }

        return min;
    }

    /**
     * Returns the max value of this time series.
     *
     * @return the maximum value of this time series
     */
    public Double max() {
        Double max = this.dataPoints.get(this.dataPoints.size() - 1);

        for (Double d : this.dataPoints) {
            if (max < d) {
                max = d;
            }
        }

        return max;
    }

    /**
     *
     * @return a values iterator
     */
    @Override
    public Iterator<Double> iterator() {
        return this.dataPoints.iterator();
    }

    /**
     *
     * @param i is the position
     * @return is the value point on specified position
     */
    public Double get(int i) {
        return this.dataPoints.get(i);
    }

    private TimeSeries normalize() throws Exception {
        double average = 0.0;
        for (int i = 0; i < this.size(); i++) {
            average += (Double) this.dataPoints.get(i);
        }
        average /= this.size();

        System.out.println(average + " " + this.varp(new ArrayList<Double>(this.dataPoints)));
        ArrayList<Double> tnova = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            //System.out.println(Math.sqrt(this.varp(this.dataPoints)));
            tnova.add((this.dataPoints.get(i) - average) / Math.sqrt(this.varp(new ArrayList<>(this.dataPoints))));
        }
        return new TimeSeries(tnova);

    }

    private double varp(ArrayList<Double> a) {
        if (a.isEmpty()) {
            return Double.NaN;
        }
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - avg) * (a.get(i) - avg);
        }
        return sum / a.size();
    }

    private double mean(ArrayList<Double> a) {
        if (a.isEmpty()) {
            return Double.NaN;
        }
        double sum = sum(a);
        return sum / a.size();
    }

    private double sum(ArrayList<Double> a) {
        double sum = 0.0;
        for (int i = 0; i < a.size(); i++) {
            sum += a.get(i);
        }
        return sum;
    }

    /**
     *
     * @param n is the size of a subsequence of the time series, used in the
     * reduction process
     * @param w is the size of the subsequences. The string is split in slices
     * of size w in the density estimation step
     * @return the equivalente time series according n value
     * @throws Exception if occurs any exception
     */
    public TimeSeries equivalentTimeSeries(int n, int w) throws Exception {
        List<Double> equivalentPoints = new ArrayList<>();
        int window = TimeSeriesOperation.getEquivalentPositionOnOriginalTS(1, n, w); // number of real values between position 0 and 1 from discretized time series
        for (int j = 0; j < size(); j++) {
            Double point = dataPoints.get(j);
            for (int i = 0; i < window; i++) {
                equivalentPoints.add(point);
            }
        }
        TimeSeries equivalentTimeSeries = new TimeSeries(equivalentPoints);
        return equivalentTimeSeries;
    }

    public double dist(TimeSeries tsB, Distance d) throws Exception {
        DataPoint a = new DataPoint(this.dataPoints.stream().mapToDouble(Double::doubleValue).toArray());
        DataPoint b = new DataPoint(tsB.getDataPoints().stream().mapToDouble(Double::doubleValue).toArray());
        return d.calcDistance(a, b);
    }

}
