package util.timeseries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import model.patterndiscovery.DataPoint;
import model.patterndiscovery.TimeSeries;
import util.StatUtil;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class TimeSeriesOperation {

//    public static TimeSeries reduceTS(TimeSeries ts,
//            int n,
//            int w,
//            int s,
//            boolean isToStandardize,
//            Object privacyConstant) throws WNegativeException, WLargerThanTimeSeriesSizeException, Exception {
//        if (!privacyConstant.getClass().equals(String.class)) {
//            return ts.reduceTS(n, w, s, isToStandardize);
//        }else{
//            return ts.reduceTS(n, w, isToStandardize);
//        }
//    }

    /*public static TimeSeries2 reduceTS(TimeSeries2 ts, int n, int w, int s,boolean isToStandardize) throws WNegativeException, WLargerThanTimeSeriesSizeException, timeseries.exception.WNegativeException, timeseries.exception.WLargerThanTimeSeriesSizeException, Exception {
     return ts.reduceTS(n, w, s, isToStandardize);
     }*/
    public static int calcEndPosition(int starterPosition, int n, int w, int s) {
        int tamSubSeq = (n / w);
        int endPosition = starterPosition + (tamSubSeq * s);
        return endPosition;
    }

    public static int getEquivalentPositionOnReducedTS(int pos, int n, int w) {
        return pos / (n / w);
    }

    public static int getEquivalentPositionOnOriginalTS(int pos, int n, int w) {
        return pos * (n / w);
    }

//    public static Object discretizeTS(TimeSeries tsRed, Object privacyConstant, boolean isToStd) throws Exception {
//        if (!privacyConstant.getClass().equals(String.class)) {
//            double discAmount = (double) privacyConstant;
//            TimeSeries discret = tsRed.discretize(discAmount, isToStd);
//            return discret;
//        } else {
//            String alphabet = (String) privacyConstant;
//            StringBuffer discret = tsRed.discretize(alphabet);
//            return discret.toString();
//
//        }
//    }
//
//    public static double getStdDev(TimeSeries ts) {
//        return ts.getStdDev();
//    }
    public static List<Double> saxBreakPoints(int numOfRegions) throws Exception {
        List<Double> breakPoints = new ArrayList<>();

        if (numOfRegions < 1) {
            throw new Exception("HotSAX Error", new Throwable("Number of regions less than 2."));
        }//endif
        double prob = 1.0 / (double) numOfRegions;
        breakPoints.add(Double.NEGATIVE_INFINITY);
        for (int i = 1; i < numOfRegions; i++) {// already included -INF
            double z = StatUtil.getInvCDF(prob * i, true); //z = normal_inv(prob*i);
            breakPoints.add(new Double(z));//   bp(i)= z
        }//endfor
        breakPoints.add(Double.POSITIVE_INFINITY);
        return breakPoints;
    }

    public static List<Double> calcBreakPoints(int alphaSize) {

        int qntBreakPoints = alphaSize - 1;
        if (qntBreakPoints == 0) {
            qntBreakPoints = 1;
        }

        if (qntBreakPoints % 2 != 0) {
            qntBreakPoints--;
        }
        int qntBreakNeg = qntBreakPoints / 2;
        int qntBreakPos = qntBreakPoints - qntBreakNeg;

        double stepNeg = (1d / qntBreakNeg);
        double stepPos = (1d / qntBreakPos);

        List<Double> breakPoints = new ArrayList<>();
        double step = stepNeg / 2;
        for (int i = 0; i < qntBreakNeg; i++) {
            breakPoints.add(step);
            step += stepNeg;
        }

        if (alphaSize % 2 == 0) {
            breakPoints.add(0d);
        }

        step = stepPos / 2;
        for (int i = 0; i < qntBreakPos; i++) {
            breakPoints.add(step);
            step += stepPos;
        }

        return breakPoints;
    }

    public static TimeSeries discretize2TimeSeries(Object sequence,
            Object disc,
            boolean isStd) throws Exception {

        if (disc.getClass().equals(String.class)) {
            return discretizeStr2TimeSeries((String) sequence, (String) disc);
        } else {
            return discretizeDataPoint2TimeSeries((DataPoint) sequence, (Double) disc, isStd);
        }
    }

    public static TimeSeries discretizeStr2TimeSeries(String sequence, String alphabet) throws Exception {
        List<Double> points = new ArrayList();
        List<Double> numericAlphabet = getNumericAlphabet(alphabet.length());

        for (int i = 0; i < sequence.length(); i++) {
            String substring = sequence.substring(i, (i + 1));
            int indexOf = alphabet.indexOf(substring);
            Double value = numericAlphabet.get(indexOf);
            points.add(value);
        }
        return new TimeSeries(points);
    }

    public static TimeSeries discretizeDataPoint2TimeSeries(DataPoint sequence,
            Double discrAmount,
            Boolean isStd) throws Exception {
        List<Double> points = new ArrayList();

        for (int i = 0; i < sequence.getDimension(); i++) {
            Double value = sequence.getValue(i);
            Double newValue;
            if (isStd) {
                newValue = Math.floor(value / discrAmount);
            } else {
                newValue = Math.floor(value * discrAmount);
            }

            points.add(newValue);
        }
        return new TimeSeries(points);
    }

    private static List<Double> getNumericAlphabet(int alphaSize) throws Exception {

        if (alphaSize == 2) {
            return Arrays.asList(-1d, 1d);
        } else {
            List<Double> breakPoints = new ArrayList<Double>();
            List<Double> formatedBreakPoints = new ArrayList<Double>();

            breakPoints.addAll(TimeSeriesOperation.saxBreakPoints(alphaSize));
            List<Double> numericAlphabet = new ArrayList<>();

            for (int i = 1; (i + 1) < breakPoints.size() - 1; i++) {
                double a = Double.valueOf(String.format(Locale.US, "%.2f", breakPoints.get(i)));
                double b = Double.valueOf(String.format(Locale.US, "%.2f", breakPoints.get(i + 1)));

                formatedBreakPoints.add(a);
                formatedBreakPoints.add(b);

                Double value = a + b;
                value /= 2;
                numericAlphabet.add(Double.valueOf(String.format(Locale.US, "%.2f", value)));

            }
//        System.out.println(formatedBreakPoints);
            double interval = formatedBreakPoints.get(0) - formatedBreakPoints.get(1);
            interval /= 2;

            Double valueA = formatedBreakPoints.get(0) + interval;
            Double valueB = formatedBreakPoints.get(formatedBreakPoints.size() - 1) + (-1 * interval);

            numericAlphabet.add(Double.valueOf(String.format(Locale.US, "%.2f", valueA)));
            numericAlphabet.add(Double.valueOf(String.format(Locale.US, "%.2f", valueB)));

            Collections.sort(numericAlphabet);
            return numericAlphabet;
        }
    }

//    public static TimeSeries discrete2int(StringBuffer discretizedTS, String alphabet) throws Exception {
//        List<Double> saxBreakPoints = TimeSeriesOperation.saxBreakPoints(alphabet.length());
//        List<Double> points = new ArrayList<>();
//
//        for (int i = 0; i < discretizedTS.length(); i++) {
//            int posLetter = alphabet.indexOf(discretizedTS.substring(i, i + 1));
//            double value;
//            if (posLetter == 0) {
//                value = -3;
//            } else if (posLetter == (alphabet.length() - 1)) {
//                value = 3;
//            } else {
//                value = (saxBreakPoints.get(posLetter) + saxBreakPoints.get(posLetter + 1)) / 2;
//            }
//            // System.out.println(posLetter + " " + value);
//            points.add(value);
//        }
//
//        return new TimeSeries(points);
//    }
}
