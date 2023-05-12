public class Tile {
    
    private double temperature;
    
    public double transitionTemperature(double temperature, long ticks) {
        double change = temperature > this.temperature ? -0.1 : 0.1;
        return temperature + change;
    }
    
}
