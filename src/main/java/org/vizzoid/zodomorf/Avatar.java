public class Avatar {

    private double temperature = 100;
    private boolean warmingUp = false;
    private Tile tile;
  
    public boolean isOutside() {
      return this.tile.getBackground().isEmpty();
    }
  
    public boolean isTooCold() {
        return temperature < 10;
    }
    
    public boolean isWarmingUp() {
       return warmingUp;
    }
  
    public void tick(long ticks) {
        if (isOutside()) {
            newTemperature = tile.transitionTemperature(temperature, ticks);
            this.temperature = newTemperature
            this.warmingUp = newTemperature > temperature
        }
        if (isTooCold() && !isWarmingUp()) {
            this.health -= ticks;
        }
    }
  
}
