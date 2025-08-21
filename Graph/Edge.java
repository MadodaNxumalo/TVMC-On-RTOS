public class Edge {
    Vertex v1;
    Vertex v2;

    public Edge() {

    }

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Edge(Edge e) {
        Vertex[] v = e.getVertices();
        v1 = v[0];
        v2 = v[1];
    }

    public Vertex[] getVertices() {
        Vertex[] v = new Vertex[2];
        v[0] = new Vertex(v1);
        v[1] = new Vertex(v2);

        return v;
    }
}
