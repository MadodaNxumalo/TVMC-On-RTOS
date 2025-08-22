public class DirectedVertex extends Vertex {
    int remainder;
    UndirectedGraph G_h;

    public DirectedVertex() {
    
    }

    public DirectedVertex(int val) {
        value = val;
        G_h = new UndirectedGraph();
        G_h.initializeEdges();
        G_h.initializeVertices();
    }

    public DirectedVertex(DirectedVertex v) {
        value = v.getValue();
    }

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int r) {
        remainder = r;
    }
}
