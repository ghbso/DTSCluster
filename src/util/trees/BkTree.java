package util.trees;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.distances.Distance;

public class BkTree<T> implements Tree<T> {

    private BkNode<T> root;
    private Distance distanceCalculator;
    private int size;

    public BkTree(Distance distanceCalculator) {
        root = null;
        this.distanceCalculator = distanceCalculator;
        this.size = 0;
    }

    private void reinsert(BkNode node) throws Exception {
        if (root == null) {
            root = node;
        } else {
            insertInteralNode(root, node);
        }
    }

    private void insertInteralNode(BkNode currentNode, BkNode newNode) throws Exception {
        double distance = distanceCalculator.calcDistance(currentNode.getData(), newNode.getData());

        // if the word aldready exists at the tree, only update the number of occurence.
//        if (distance == 0) {
        if (currentNode.getData().equals(newNode.getData())) {
            currentNode.addPosition((int) newNode.getOccurrencePositions().get(0));
            return;
        }

        BkNode bkNode = currentNode.childAtDistance(distance);
        //return the node with distance "DISTANCE", if exists.

        if (bkNode == null) {
            // if don't exist, insert a new leaf node
//            System.out.println(newNode.getData() + " " + currentNode.getData() + " " + distance);
            currentNode.addChild(distance, newNode);
            newNode.addFather(currentNode);

        } else {
            //else, seacrh for a node without children at current node children's.
//            System.out.println("L: " + bkNode.getData() );
            insertInteralNode(bkNode, newNode);
        }
    }

    @Override
    public void remove(T s) {
        try {
            BkNode<T> nodeToDelete = searchInternalNode(root, s, 0, true).get(0);
            double distance = distanceCalculator.calcDistance(nodeToDelete.getData(), nodeToDelete.getFather().getData());

            Iterator entries = nodeToDelete.getDescendents().iterator();
            while (entries.hasNext()) {
                this.reinsert((BkNode) entries.next());
            }
        } catch (Exception ex) {
            Logger.getLogger(BkTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public T search(T s) {
        try {
            // As we want the exactly word, the radious value is zero.
//            List<BkNodeStr> result = searchInternalNode(root, s, 0, 0, true);
            List<BkNode> result = searchInternalNode(root, s, 0, true);
            return (T) (result.isEmpty() ? null : result.iterator().next().getData());
        } catch (Exception ex) {
//            Logger.getLogger(BkTreeStr2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private List<BkNode> searchInternalNode(BkNode node,
            T s,
            double radious,
            boolean isSingleOccurence) throws Exception {

        double distance = distanceCalculator.calcDistance(node.getData(), s);

        List<BkNode> matches = new LinkedList<>();

        if (distance <= radious) {
            matches.add(node);
            if (!isSingleOccurence) {
                for (int i = 1; i < node.getQntOccurrence(); i++) {
                    matches.add(node);
                }
            }
        }

        if (node.getChildren().isEmpty()) {
            return matches;
        }
        List<Map.Entry<Double, BkNode>> children = new ArrayList(node.getChildrenMap().entrySet());
    
        double maxDistance = Double.MAX_VALUE;
        if (node.getFather() != null) {
            BkNode grandFather = node.getFather().getFather();
            if (grandFather != null) {
                
                double distBetweenCurrentNodeAndFather = distanceCalculator.calcDistance(
                        node.getData(),
                        grandFather.getData());
                 
                double v = radious + distBetweenCurrentNodeAndFather;
//                maxDistance = v;
            }
        }

//        maxDistance = radious+distance;
        for (int i = 0; i < children.size(); i++) {
//            if(children.get(i).getValue().getData().equals("aaaceee")){
//                System.out.println(distance + " " +children.get(i).getKey() + " " + maxDistance);
//            }
            if (children.get(i).getKey() <= maxDistance) {
                matches.addAll(this.searchInternalNode(children.get(i).getValue(),
                        s,
                        radious,
                        isSingleOccurence));
            }
        }

        return matches;
    }

    @Override
    public List<T> neighborsSingleOccurrences(T s, double radious) {
        try {
            List<T> neighborhood = new ArrayList<>();
            Iterator i = searchInternalNode(root, s, radious, true).iterator();
            while (i.hasNext()) {
                BkNode<T> node = (BkNode<T>) i.next();
                neighborhood.add(node.getData());
            }
            return neighborhood;
        } catch (Exception ex) {
            Logger.getLogger(BkTree.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<Key<T>> neighborsKeysSingleOccurrences(T s, double radious) {
        try {
            List<Key<T>> neighborhood = new ArrayList<>();
            Iterator i = searchInternalNode(root, s, radious, true).iterator();
            while (i.hasNext()) {
                BkNode<T> node = (BkNode<T>) i.next();
                neighborhood.add(node.getPoint());
            }
            return neighborhood;
        } catch (Exception ex) {
            Logger.getLogger(BkTree.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<T> neighbors(T s, double radious) {
        try {
            List<T> neighborhood = new ArrayList<>();
            Iterator i = searchInternalNode(root, s, radious, false).iterator();
            while (i.hasNext()) {
                BkNode<T> node = (BkNode<T>) i.next();
                neighborhood.add(node.getData());
            }
            return neighborhood;
        } catch (Exception ex) {
            Logger.getLogger(BkTree.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    @Override
    public int size() {
        return this.size;
    }

    
    @Override
    public List<Integer> getOccurencesPositions(T s) {
        try {
            List<BkNode> result = searchInternalNode(root, s, 0, true);
            return result.get(0).getOccurrencePositions();
        } catch (Exception ex) {
            Logger.getLogger(BkTree.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }

    }

    @Override
    public int getQntOcurrencesNodo(T d) {
        try {
            List<BkNode> result = searchInternalNode(root, d, 0, false);
            return result.get(0).getQntOccurrence();
        } catch (Exception ex) {
            Logger.getLogger(BkTree.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void insert(T s, int posSequence) {

        this.size++;
        BkNode<T> newNode = new BkNode(s, this.distanceCalculator);
        newNode.addPosition(posSequence);

        if (root == null) {
            root = newNode;
//            System.out.println("R: " + newNode.getData());
        } else {
            try {
                insertInteralNode(root, newNode);
            } catch (Exception ex) {
                Logger.getLogger(BkTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public BkNode getRoot() {
        return root;
    }

}
