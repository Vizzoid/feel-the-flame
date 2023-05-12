public class Material {
    
    public static final Material EMPTY = new Material(true);

    private boolean empty;
    
    public Material(boolean empty) {
        this.empty = empty;
    }
    
    public boolean isEmpty() {
        return empty;
    }
    
}
