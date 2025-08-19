public class Vertex {
    int remainder;

    public Vertex() {
        remainder = 0;
    }

    public Vertex(Vertex v) {
        remainder = v.getRemainder();
    }   

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int r) {
        remainder = r;
    }

}