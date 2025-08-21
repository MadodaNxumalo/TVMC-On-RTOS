public class UndirectedGraph extends Graph{
    private UndirectedVertex[] vertices;

    public UndirectedGraph() {

    }

    public UndirectedGraph(Integer[] periodSet) {
        vertices = new UndirectedVertex[periodSet.length];
        for (int i = 0; i < periodSet.length; i++) {
            vertices[i] = new UndirectedVertex(periodSet[i]);
        }
    }
}
