public class UndirectedVertex extends Vertex {
    public UndirectedVertex() {
        
    }

    public UndirectedVertex(int val) {
        value = val;
    }

    public UndirectedVertex(UndirectedVertex v) {
        value = v.getValue();
    }   
}
