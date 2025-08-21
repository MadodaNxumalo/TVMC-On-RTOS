public class Vertex {
    int value;

    public Vertex() {
    }

    public Vertex(int val) {
        value = val;
    }

    public Vertex(Vertex v) {
        value = v.getValue();
    }   

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        value = v;
    }
}