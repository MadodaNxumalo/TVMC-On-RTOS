public class DirectedVertex extends Vertex {
    int remainder;
    UndirectedGraph G_h;

    public DirectedVertex() {
    
    }

    public DirectedVertex(int val) {
        value = val;
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
