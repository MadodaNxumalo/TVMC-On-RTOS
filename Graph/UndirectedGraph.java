import java.util.Iterator;
import java.util.Stack;

public class UndirectedGraph extends Graph {
    // The value of h in this class can be obtained by Graph.getValue()
    private UndirectedVertex[] vertices;

    public UndirectedGraph() {
        Integer[] periodValues = getPeriodValues();           // Call to parent class containing period values
        vertices = new UndirectedVertex[periodValues.length];
        for (int i = 0; i < periodValues.length; i++) {
            vertices[i] = new UndirectedVertex(periodValues);
        }
    }

    // TODO: For each Graph G_h, use h colors.
    // TODO: Each color gets assigned: r % h (different colors have different remainders)
    // TOOD: Obtain for each period p_i a system of congruences t_i ≡ r_i_h (mod h)

    // TODO: Fix inheritance structure
    @Override
    public void initializeEdges() {
        //There exists an edge between p_i and p_j, iff (p_i, p_j) = h
        try {
            if (vertices != null) {
                Stack<Edge> edges = new Stack<>();
                for (UndirectedVertex v1: vertices) {
                    for (UndirectedVertex v2: vertices) {
                        // TODO: Complete this
                        // if ()
                    }    
                }        
            
                Iterator<Edge> it = edges.iterator();
                Edge[] newEdges = new Edge[edges.size()];
                int i = 0;
                while (it.hasNext()) {
                    newEdges[i] = it.next();
                    i += 1;
                }
                setEdges(newEdges);         // Call to set edges in parent

            } else {
                throw new Exception("Vertices not initialized");
            }
            
        } catch (Exception error) {
            error.printStackTrace();
            System.out.println("UndirectedGraph: initializeEdges(): Could not initialize Edges - Vertices not initialized");
        }
    }

    @Override
    public void initializeVertices() {
        Integer[] periodValues = getPeriodValues();
        this.vertices = new UndirectedVertex[periodValues.length];
        
        // TODO: Check this for efficiency
        for (int i = 0; i < periodValues.length; i++) {
            this.vertices[i] = new UndirectedVertex(periodValues);
        }
    }
}
