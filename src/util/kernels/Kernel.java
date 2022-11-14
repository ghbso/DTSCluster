package util.kernels;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public abstract class Kernel
{

    /**
     *
     * @param x is the value
     * @return the kernel value
     */
    public abstract double calculate(double x);

    /**
     *
     * @param y is the value
     * @return the inverse
     */
    public abstract double inverse(double y);
}