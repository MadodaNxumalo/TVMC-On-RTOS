public class UndirectedVertex extends Vertex {
    private Integer[] periodSet;
    
    public UndirectedVertex() {
        
    }

    public UndirectedVertex(int val) {
        value = val;
    }

    public UndirectedVertex(UndirectedVertex v) {
        value = v.getValue();
    }

    public UndirectedVertex(Integer[] p) {
        periodSet = new Integer[p.length];
        System.arraycopy(p, 0, periodSet, 0, p.length);
    }
}
