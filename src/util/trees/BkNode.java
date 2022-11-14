package util.trees;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import util.distances.Distance;
import util.distances.DistanceUtil;

class BkNode<T> {

    private Key<T> point;
    private BkNode father;
    private Map<Double, BkNode> children = new HashMap<>();
    private Distance distanceCalculator;
    private DistanceUtil distanceUtil;

    public BkNode() {
        distanceUtil = new DistanceUtil();
    }

    public BkNode(T data, Distance distanceCalculator) {
        this();
        this.point = new Key<>(data);
        this.distanceCalculator = distanceCalculator;
    }

    public Key<T> getPoint() {
        return point;
    }

    
    public Map<Double, BkNode> getChildrenMap() {
        return children;
    }

    public List<BkNode> getChildren() {
        List<BkNode> matches = new LinkedList<>();
        Iterator<Map.Entry<Double, BkNode>> iterator = this.children.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pairs = (Map.Entry) iterator.next();
            BkNode currentNode = (BkNode) pairs.getValue();
            matches.add(currentNode);
        }
        return matches;
    }

    public void addPosition(int posSequence) {
        point.addOccurence(posSequence);
    }

    public int getQntOccurrence() {
        return point.getQntOccurence();
    }

    public List<Integer> getOccurrencePositions() {
        return point.getOccurences();
    }

    public void setQntOccurrence() {
        this.point.addOccurence(0);
    }

    public T getData() {
        return point.getPoint();
    }

    public BkNode getFather() {
        return father;
    }

    public BkNode childAtDistance(double dist) {
        double distInt = distanceUtil.downToNearest(dist);
        return children.get(distInt);
    }

    public void addChild(double dist, BkNode child) {
        double distInt = distanceUtil.downToNearest(dist);
//        System.out.println("---> " + child.getData() + " " + dist + " " + distInt);

        children.put(distInt, child);
    }

    public void addFather(BkNode father) {
        this.father = father;
    }

    public void removeChild(T s) throws Exception {
        double distance = distanceCalculator.calcDistance(this.point.getPoint(), s);
        this.children.remove(distance);
    }

    public List<BkNode> getDescendents() {
        List<BkNode> matches = new LinkedList<>();
        Iterator<Map.Entry<Double, BkNode>> iterator = this.children.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pairs = (Map.Entry) iterator.next();
            BkNode currentNode = (BkNode) pairs.getValue();
            matches.add(currentNode);
//            matches.addAll(this.getDescendents());
        }
        return matches;
    }

}
