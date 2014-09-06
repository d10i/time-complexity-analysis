import java.util.ArrayList;
import java.util.List;

public class Logger {
    public Tree tree;

    public Logger() {
        this.tree = new Tree("example.Main.main");
    }

    public void log(long startTime, Thread thread) {
        startTime = System.nanoTime() - startTime;
        tree.add(startTime, filterStackTraceElements(thread.getStackTrace()));
    }

    private class Tree {
        private List<Tree> nodes;
        private String name;
        private long count;
        private double average;
        private double min;
        private double max;

        public Tree(String name) {
            this.nodes = new ArrayList<Tree>();
            this.name = name;
            this.count = 0;
            this.average = 0.0d;
            this.min = Double.POSITIVE_INFINITY;
            this.max = Double.NEGATIVE_INFINITY;
        }

        public boolean add(long elapsedTime, List<StackTraceElement> stackTraceElementsList) {
            StackTraceElement stackTraceElement = stackTraceElementsList.get(stackTraceElementsList.size() - 1);
            if (getStackTraceElementName(stackTraceElement).equals(name)) {
                if (stackTraceElementsList.size() == 1) {
                    this.average = ((this.count * this.average) + elapsedTime) / (this.count + 1.0d);
                    this.count++;
                    this.min = Math.min(this.min, elapsedTime);
                    this.max = Math.max(this.max, elapsedTime);
                } else {
                    boolean found = false;
                    List<StackTraceElement> subList = stackTraceElementsList.subList(0, stackTraceElementsList.size() - 1);
                    for (Tree node : nodes) {
                        if (node.add(elapsedTime, subList)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        Tree node = new Tree(getStackTraceElementName(subList.get(subList.size() - 1)));
                        node.add(elapsedTime, subList);
                        this.nodes.add(node);
                    }
                }

                return true;
            }

            return false;
        }

        private String getStackTraceElementName(StackTraceElement stackTraceElement) {
            return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
        }

        public String toString() {
            return toString(0);
        }

        public String toString(int n) {
            String tmp = "";

            for (int i = 0; i < n; i++) {
                tmp += "\t";
            }

            tmp += this.name + " (" + this.count + ", " + String.format("%.3f", this.average / 1000000.0f) + ", " + String.format("%.3f", this.min / 1000000.0f) + ", " + String.format("%.3f", this.max / 1000000.0f) + ")";

            for (Tree node : nodes) {
                tmp += "\r\n" + node.toString(n + 1);
            }

            return tmp;
        }
    }

    private static List<StackTraceElement> filterStackTraceElements(StackTraceElement[] stackTraceElements) {
        List<StackTraceElement> stackTraceElementsList = new ArrayList<StackTraceElement>(stackTraceElements.length);
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (stackTraceElement.getClassName().startsWith("example.")) {
                stackTraceElementsList.add(stackTraceElement);
            }
        }

        return stackTraceElementsList;
    }
}
