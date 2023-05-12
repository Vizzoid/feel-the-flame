public class Planet {
    
    private double temperature;
    
    public void tick(long ticks) {
        temperature = getSkyTemperature();
    }
    
    public double transitionTemperature(double target, double current, long ticks) {
        double change = current > target ? -0.1 : 0.1;
        return temperature + change;
    }
    
    public double transitionTemperature(double current, long ticks) {
        return transitionTemperature(temperature, current, ticks);
    }

}
