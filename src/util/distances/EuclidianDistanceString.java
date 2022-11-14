package util.distances;

/**
 *
 * @author GUSTAVOHENRIQUE2
 * @deprecated
 */
@Deprecated
public class EuclidianDistanceString {

//    @Override
    public double calc(String obj1, String obj2) throws Exception {
        double dist = 0;
        double sqrsum = 0;
        if (obj1.length() == obj2.length()) {
            for (int i = 0; i < obj1.length(); i++) {
                sqrsum = sqrsum + Math.pow((obj1.charAt(i) - obj2.charAt(i)), 2);
            }
            dist = Math.sqrt(sqrsum);
        }
        return dist;

    }

//    @Override
    public double calcAccordingVariation(String obj1, String obj2, double radius) throws Exception {
        return this.calc(obj1, obj2);
    }

//    @Override
    public double calcDistance(Object obj1, Object obj2) throws Exception {
        String str1 = (String) obj1;
        String str2 = (String) obj2;
        return this.calc(str1, str2);

    }
}
