public class Main {
    public static void main(String[] args) {
        Vertex[] v = new Vertex[1];
        v[0] = new Vertex();

        Edge[] e = new Edge[1];
        e[0] = new Edge();

        Integer[] p = {4, 4, 6, 12};
        Graph testGraph = new Graph(v,e,p);
        testGraph.setupGraph();
        System.out.println(testGraph.toString());
    }
}
