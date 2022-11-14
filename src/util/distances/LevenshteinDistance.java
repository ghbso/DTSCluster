package util.distances;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class LevenshteinDistance {

//    @Override
    public double calc(String src, String tgt) throws Exception {

        int d[][];
        if (src.length() == 0) {
            return tgt.length();
        }
        if (tgt.length() == 0) {
            return src.length();
        }
        d = new int[src.length() + 1][tgt.length() + 1];
        for (int i = 0; i <= src.length(); i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= tgt.length(); j++) {
            d[0][j] = j;
        }
        for (int i = 1; i <= src.length(); i++) {
            char sch = src.charAt(i - 1);
            for (int j = 1; j <= tgt.length(); j++) {
                char tch = tgt.charAt(j - 1);
                int cost = sch == tch ? 0 : 1;
                d[i][j] = minimum(d[i - 1][j] + 1, //deletion
                        d[i][j - 1] + 1, //insertion
                        d[i - 1][j - 1] + cost); //substitution
            }
        }
        return d[src.length()][tgt.length()];
    }

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);

    }

//    @Override
    public double calcAccordingVariation(String obj1, String obj2, double radius) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
    public double calcDistance(Object obj1, Object obj2) throws Exception {
        String str1 = (String) obj1;
        String str2 = (String) obj2;
        return this.calc(str1, str2);
    }
}
