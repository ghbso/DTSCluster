
package model.clusteringtimeseries;

public class HillClimbingPoint<T> {

    private boolean visited;
    private Double density;
    private boolean navagated;
    
    private HillClimbingPoint<T> lastPointVisited;
    
    private T maximaLocal;

    public void setMaximaLocal(T maximaLocal) {
        this.maximaLocal = maximaLocal;
    }

    public T getMaximaLocal() {
        return maximaLocal;
    }

    public void setLastPointVisited(HillClimbingPoint<T> lastPointVisited) {
        this.lastPointVisited = lastPointVisited;
    }

    
    public void setVisited(boolean isChecked) {
        this.visited = isChecked;
    }

    public void setNavagated(boolean navagated) {
        this.navagated = navagated;
    }

    
    public void setDensity(Double density) {
        this.density = density;
    }

    public boolean isLabeled() {
        return visited;
    }

    public boolean isVisited() {
        return navagated;
    }

    
    public Double getDensity() {
        return density;
    }

    public HillClimbingPoint<T> getLastPointVisited() {
        return lastPointVisited;
    }
    
    

    public void clearNavigationPath() {
//        System.out.println(lastPointVisited.equals(this));
        if(lastPointVisited!=null){
            lastPointVisited.setNavagated(false);
            lastPointVisited.clearNavigationPath();
            lastPointVisited = null;
        }
    }
    
    
    
}
