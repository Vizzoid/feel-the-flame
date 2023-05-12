public class Tile {
    
    private Planet planet;
    private double temperature;
    private Material background = Material.EMPTY;
    private Material material = Material.EMPTY;
    
    public Material getBackground() {
        return this.background;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public void setBackground(Material material) {
        this.background = background;
    }
    
    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public double transitionTemperature(double temperature, long ticks) {
        double change = temperature > this.temperature ? -0.1 : 0.1;
        return temperature + change;
    }
    
    public void tick(long ticks) {
        if (background.isEmpty()) planet.transitionTemperature(this.temperature, ticks);
    }
    
}
