package tech.dario.dissertation.timerecorder.akka.tree;

import java.util.HashMap;
import java.util.Map;

abstract public class Node<T extends MergeableValue> {

    protected final Map<String, Node<T>> nodes = new HashMap<String, Node<T>>();
    //protected final List<Node<T>> nodes = new ArrayList<Node<T>>();

    public Node<T> add(T value, MeasuredStackTraceElements measuredStackTraceElements) {
        if (measuredStackTraceElements.size() > 0) {
            String stackTraceElementName = measuredStackTraceElements.getLastElement();
            Node node = nodes.get(stackTraceElementName);
            if (node != null) {
                return node.add(value, measuredStackTraceElements.withLastElementRemoved());
            }
            /*for (Node node : nodes) {
                if (node.getName().equals(stackTraceElementName)) {
                    return node.add(value, measuredStackTraceElements.withLastElementRemoved());
                }
            }*/

            TreeNode<T> newNode = new TreeNode<T>(stackTraceElementName);
            newNode.add(value, measuredStackTraceElements.withLastElementRemoved());
            nodes.put(stackTraceElementName, newNode);
            //nodes.add(newNode);
        }

        return null;
    }

    abstract String getName();

    public void clear() {
        for(Node node : this.nodes.values()) {
            node.clear();
        }
        this.nodes.clear();
    }
}
