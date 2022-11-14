/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.kernels;

/**
 * Factory to instantiate Kernel Functions.
 *
 * @author jcsilva
 */
public class KernelFunctionFactory {

    /**
     *
     */
    public static final int GAUSS = 0; 
  
    public static Kernel getKernel(String kernelDescr) {
        if (kernelDescr.equalsIgnoreCase("Gauss")){        
            return new GaussKernel();
        
        }
    return null;
    }
}
