package util.kernels;

import util.kernels.Kernel;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class GaussKernel extends Kernel
{

    /**
     *
     * @param x is the value
     * @return the gaussian kernel
     */
    public double calculate(double x)
  {
    return (1.0/Math.sqrt(2*Math.PI))*Math.exp((-1.0/2.0)*Math.pow(x,2));
  }

    /**
     *
     * @param y is the value
     * @return the inverse
     */
    public double inverse(double y)
  {
    return  Math.sqrt(-2*Math.log(Math.sqrt(2*Math.PI)*y));
    
    
  }

}