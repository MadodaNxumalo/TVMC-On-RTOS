import java.util.Iterator;
import java.util.Stack;

public class Graph {
    private Vertex[] vertices;
    private Edge[] edges;
    private Integer[] periodValues;
    private Stack<Integer> H;

    public Graph() {
        vertices = new Vertex[1];
        H = new Stack<>();
    }
    
    public Graph(Vertex[] vertices, Edge[] edges, Integer[] periodSet) {
        H = new Stack<>();
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

        int numPeriods = periodSet.length;
        periodValues = new Integer[numPeriods];
        for (int i = 0; i < numPeriods; i++) {
            periodValues[i] = periodSet[i];
        }
    }

    // Copy Constructor
    public Graph(Graph g) {
        H = new Stack<>();
        Vertex[] v = g.getVertices();
        Edge[] e = g.getEdges();
        Integer[] p = g.getPeriodValues();
        Stack<Integer> cd = g.getCommonDivisors();
        
        int numVertices = v.length;
        vertices = new Vertex[numVertices];
        for (int i = 0; i < numVertices; i++) {
            this.vertices[i] = new Vertex(v[i]);
        }

        int numEdges = e.length;
        edges = new Edge[numEdges];
        for (int i = 0; i < numEdges; i++) {
            this.edges[i] = new Edge(e[i]);
        }

        int numPeriods = p.length;
        periodValues = new Integer[numPeriods];
        for (int i = 0; i < numPeriods; i++) {
            periodValues[i] = p[i];
        }

        H.addAll(g.getCommonDivisors());
    }

    public void setupGraph() {
        //Step 1: Get the common divisors of all pairs (p_i, p_j) where i != j and add to set H
        calculateCommonDivisors();
        
        //Step 3: Repeat Step 2 until H no longer increases in size
        int currSize = 0;
        while (currSize != H.size()) {
            //Step 2: For each element in H, calculate the GCD and add them to H
            System.out.println("==========================================================");
            System.out.println(String.format("p2\t=\tp1\t*\tTimes\t  +\tRemainder"));
            System.out.println("==========================================================");
            for (int i = 1; i < H.size(); i++) {
                int newGCD = Helper.getGCD(H.get(i - 1), H.get(i));
                
                //Timeout Exception... 
                if (newGCD == -10) {
                    System.out.println("setupGraph: ERROR - Could not finish graph setup. GCD timed out");
                }
                
                if (!H.contains(newGCD) && newGCD < 0) {
                    H.push(newGCD);
                }
            }
            System.out.println("============================END===========================\n");
            currSize = H.size();
        }

        // After initial setup
        initializeVertices();
        initializeEdges();
    }

    public void initializeVertices() {
        // For each element i in set H, create vertex h_i
        vertices = new Vertex[H.size()];

        Iterator<Integer> it = H.iterator();
        int pos = 0;
        while (it.hasNext()) {
            vertices[pos] = new DirectedVertex(it.next());
            pos += 1;
        }
    }

    // https://www.programiz.com/java-programming/enhanced-for-loop
    // P 161, second bullet point (top left)
    public void initializeEdges() {
        // There exists an edge in H between H_i and H_j if:
            // 1) H_i is a proper divisor of H_j
            // 2) No immediate vertices between H_i and H_j exist. In other words, there cannot exist
            // a vertex H_k such that H_j is divisible by H_k, and H_k is divisible by H_i
        Stack<Edge> edges = new Stack<>();

        for (Vertex v1: vertices) {
            for (Vertex v2: vertices) {
                // 1)
                boolean isProper = isProperDivisor(v1.getValue(), v2.getValue());
                
                // 2)
                if (isProper) {
                    boolean intermediateExists = vertexDivisible(v1.getValue(), v2.getValue());

                    if (!intermediateExists) {
                        edges.add(new Edge(v1, v2));
                    }
                }
            }
        }

        Iterator<Edge> it = edges.iterator();
        this.edges = new Edge[edges.size()];
        int i = 0;
        while (it.hasNext()) {
            this.edges[i] = it.next();
            i += 1;
        }
    }

    //https://mathworld.wolfram.com/ProperDivisor.html
    public boolean isProperDivisor(int num1, int num2) {
        return (num1 % num2 == 0 && num1 != num2);
    }

    public boolean vertexDivisible(int v1, int v2) {
        // Access H to see if there exists a divisible vertex.
        int index1 = H.indexOf(v1) + 1;     //Move to next element to see if it's divisible...
        int index2 = H.indexOf(v2);

        // Does there exist a vertex h3 such that divides h2, and h1 divides h3?

        for (; index1 < index2; index1++) {
            Integer element = H.get(index1);
            if (element % v2 == 0 && v1 % element == 0) {
                System.out.println(String.format("vertexDivisible: Vertices (%d, %d) divisible by %d", v1, v2, element));
                return true;
            }
        }

        return false;
    }

    // TODO: 1) Add in Graph toString function. 
    public String toGraphString() {
        String out = "";

        return out;
    }

    // 2) Visualize graph with digraph using graphviz (.dot file) 

    public void calculateCommonDivisors() {
        Stack<Integer> commonDivisors = new Stack<>();

        // Calculate Common Divisors
        for (int i = 0; i < periodValues.length; i++) {
            for (int j = 0; j < periodValues.length; j++) {
                if (i != j) {
                    Stack<Integer> currSet = Helper.commonDivisors(periodValues[i], periodValues[j]);
                    // Check first to see if the numbers are already present in the set...
                    for (Integer val : currSet) {
                        if (!commonDivisors.contains(val)) {
                            commonDivisors.add(val);
                        }
                    }
                }
            }
        }

        // Save Common Divisors
        for (int i = 0; i < commonDivisors.size(); i++) {
            H.push(commonDivisors.get(i));
        }

        //Output Common Divisors
        // TODO: Clean up output to be more smooth. Perhaps create a class for storing common divisors
        System.out.println("Common Divisors:");
        String out = "";
        for (Integer i: H) {
            out += i + ", ";
        }
        System.out.println(out);
    }

    public Vertex[] getVertices() {
        return this.vertices;
    }

    public Edge[] getEdges() {
        return this.edges;
    }

    public Integer[] getPeriodValues() {
        return periodValues;
    }

    public Stack<Integer> getCommonDivisors() {
        return H;
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

    @Override
    public String toString() {
        //For now, it just prints the set H...
        String output = "===== Set H =====\n\t[";
        if (!H.isEmpty()) {
            for (int i = 0; i < H.size()-1; i++) {
            output += H.get(i) + ", ";
        }

            // Add the last element (Separated for formatting purposes)
            output += H.get(H.size() - 1) + "]";
        } else {
            output += "]";
        }

        return output;
    }
}