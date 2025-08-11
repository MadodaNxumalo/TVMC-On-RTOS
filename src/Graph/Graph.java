public class Graph {
    private boolean isDirected;
    private Vertex[] vertices;
    private Edge[] edges;

    public Graph() {
        this.isDirected = false;
        this.vertices = new Vertex[1];
    }
    
    public Graph(boolean isDirected, Vertex[] vertices, Edge[] edges) {
        this.isDirected = isDirected;

        int numVertices = vertices.length;
        this.vertices = new Vertex[numVertices];
        for (int i = 0; i < numVertices; i++) {
            this.vertices[i] = new Vertex(vertices[i]);
        }


        int numEdges = edges.length;
        this.edges = new Edge[numEdges];
        for (int i = 0; i < numEdges; i++) {
            this.edges[i] = new Edge(edges[i]);
        }
    }

    // Copy Constructor
    public Graph(Graph g) {
        // TODO
    }

    public Vertex[] getVertices() {
        return this.vertices;
    }

    public Edge[] getEdges() {
        return this.edges;
    }

    public void setEdges(Edge[] e) {
        this.edges = new Edge[e.length];
        for (int i = 0; i < e.length; i++) {
            this.edges[i] = new Edge(e[i]);
        }
    }

    public void setVertices(Vertex[] v) {
        this.vertices = new Vertex[v.length];
        for (int i = 0; i < v.length; i++) {
            this.vertices[i] = new Vertex(v[i]);
        }
    }
}
