package org.vizzoid.zodomorf;

public class BarrenPlanet extends Planet {

    @Override
    public void newDay() {
        super.newDay();
        if (getDay() > 50) return;
        setMinTemperature(getMinTemperature() - 2);
        setMaxTemperature(getMaxTemperature() - 2);
    }
}
