package mar.huffman;

public class Node {
    public Node(boolean isChar, int frec, byte mChar) {
        this.frec = frec;
        this.isChar = isChar;
        this.mChar = mChar;
    }
    public Node(Node lChild, Node rChild) {
        this.lChild = lChild;
        this.rChild = rChild;
        this.frec = lChild.frec + rChild.frec;
        this.isChar = false;
    }
    public boolean isChar;
    public int frec;
    public byte mChar;
    Node lChild;
    Node rChild;
    @Override
    public String toString() {
        return "Node{" +
                "isChar=" + isChar +
                ", frec=" + frec +
                ", mChar=" + mChar +
                ", lChild=" + lChild +
                ", rChild=" + rChild +
                '}';
    }
}
