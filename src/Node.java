public class Node {
    String state;
    Node parent;
    String action;
    int g; 
    int h; 

    Node(String state, Node parent, String action, int g, int h) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.g = g;
        this.h = h;
    }

    int f() {
        return g + h;
    }
}