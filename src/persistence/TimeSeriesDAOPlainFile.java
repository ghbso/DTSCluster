package persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import model.patterndiscovery.TimeSeries;

/**
 * Loads one dimensional data to a TimeSeries object. Each point is a real.
 */
public class TimeSeriesDAOPlainFile implements TimeSeriesDAO {

    /**
     * Loads timeseries data for a given file name and returns a TimeSeries
     * object. TimeSeries is based on a Vector of Double.
     *
     * @param fileName is the file path
     * @return a TimeSeries
     * @throws IOException if the file path is invalid
     */
    public TimeSeries load(String fileName) throws Exception {
        List<Double> points = new ArrayList<Double>();
        TimeSeries ts = null;

        BufferedReader r = new BufferedReader(new FileReader(fileName));

        StreamTokenizer st = new StreamTokenizer(r);
        st.eolIsSignificant(true);
        st.resetSyntax();
        st.wordChars('a', 'z');
        st.wordChars('A', 'Z');
        st.wordChars('0', '9');
        st.wordChars('+', '+');
        st.wordChars('-', '-');
        st.wordChars('.', '.');
        st.whitespaceChars('\u0000', '\u0020');
        st.commentChar('#');
        st.eolIsSignificant(false);
        st.lowerCaseMode(true);

        //System.out.println("\nLoading file "+fileName+" ...\n");
        int index = 0;
        while (st.ttype != StreamTokenizer.TT_EOF) {
            Double value = null;
//            index++;
//            if (st.ttype == StreamTokenizer.TT_NUMBER) {
//                value = (new Double(st.nval)).doubleValue();
//                if (value == 9.8) {
//                    System.out.println("hey" + index + " " + st.sval);
//                }
//                points.add(value);
//            }

            if (st.sval != null) {
                try {
                    value = Double.parseDouble(st.sval);
                    points.add(value);
                    index++;
                } catch (Exception e) {
                }
            }

//if
            st.nextToken();
        }//while 

        ts = new TimeSeries(points);
        r.close();
        return ts;

    } // method load

    /**
     *
     * @param ts is the time series to be saved.
     * @param fileName is the file name where the time series must be saved
     */
    public void save(TimeSeries ts, String fileName) {
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(fileName, false));
            List<Double> pts = ts.getDataPoints();
            for (Double pt : pts) {
                ps.println(pt);
            }
            ps.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TimeSeriesDAOPlainFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save(String ts, String fileName) {
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(fileName, false));
            for (int i = 0; i < ts.length(); i++) {
                ps.println(ts.subSequence(i, i + 1));
            }
            ps.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TimeSeriesDAOPlainFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<TimeSeries> load(String fileName, int size) throws Exception {

        List<TimeSeries> list = new ArrayList<>();

        BufferedReader r = new BufferedReader(new FileReader(fileName));

        StreamTokenizer st = new StreamTokenizer(r);
        st.eolIsSignificant(true);
        st.resetSyntax();
        st.wordChars('a', 'z');
        st.wordChars('A', 'Z');
        st.wordChars('0', '9');
        st.wordChars('+', '+');
        st.wordChars('-', '-');
        st.wordChars('.', '.');
        st.whitespaceChars('\u0000', '\u0020');
        st.commentChar('#');
        st.eolIsSignificant(false);
        st.lowerCaseMode(true);

        //System.out.println("\nLoading file "+fileName+" ...\n");
        int index = 0;

        List<Double> points = new ArrayList<Double>();

        while (st.ttype != StreamTokenizer.TT_EOF) {
            if (index % size == 0) {
                if (!points.isEmpty()) {
                    TimeSeries ts = new TimeSeries(points);
//                    System.out.println(ts.getDataPoints());
                    list.add(ts);
                    points = new ArrayList<>();
                }
            }
            Double value = null;

            if (st.sval != null) {
                value = Double.parseDouble(st.sval);
                points.add(value);
                index++;

            }

//if
            st.nextToken();
        }
        TimeSeries ts = new TimeSeries(points);
        list.add(ts);

        //while 
//        System.out.println(index + " " + list.size());
        r.close();
        return list;

    } // method load

    public List<TimeSeries> loadTimeSeriesList(String fileName) throws Exception {

        List<TimeSeries> list = new ArrayList<>();

        Stream<String> lines = Files.lines(Paths.get(fileName));
        lines.forEach(l -> {
            String[] splits = l.split(" ");
            List<Double> points = new ArrayList<Double>();
            Double value = null;

            for (String split : splits) {
                value = Double.parseDouble(split);
                points.add(value);

            }
//            System.out.println(points.size());
            if(!points.isEmpty()){
                try {
                    list.add(new TimeSeries(points));
                } catch (Exception ex) {
                    Logger.getLogger(TimeSeriesDAOPlainFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return list;

    } // method load

}// Class
