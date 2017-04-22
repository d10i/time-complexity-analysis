package tech.dario.timecomplexityanalysis.timerecorder.tree;

public class NodesPair<T1, R1 extends AbstractNode<T1, R1>, T2, R2 extends AbstractNode<T2, R2>> {
  private final R1 node1;
  private final R2 node2;

  public NodesPair(R1 node1, R2 node2) {
    this.node1 = node1;
    this.node2 = node2;
  }

  public R1 getNode1() {
    return node1;
  }

  public R2 getNode2() {
    return node2;
  }
}
