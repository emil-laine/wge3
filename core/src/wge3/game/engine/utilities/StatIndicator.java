package wge3.game.engine.utilities;

public final class StatIndicator {
    private int maximumValue;
    private int currentValue;

    public StatIndicator() {}
    
    public StatIndicator(int maximumValue) {
        setMax(maximumValue);
    }
    
    public void decrease(int amount) {
        currentValue -= amount;
        if (currentValue < 0)
            currentValue = 0;
    }
    
    public void decrease() {
        decrease(1);
    }
    
    public void increase(int amount) {
        currentValue += amount;
        if (currentValue > maximumValue)
            currentValue = maximumValue;
    }
    
    public void increase() {
        increase(1);
    }
    
    public boolean isFull() {
        return currentValue == maximumValue;
    }
    
    public boolean isEmpty() {
        return currentValue == 0;
    }
    
    public void setMax(int maximum) {
        maximumValue = maximum;
        currentValue = maximum;
    }

    public int getMax() {
        return maximumValue;
    }

    public void setCurrent(int newValue) {
        this.currentValue = newValue;
    }

    public int getCurrent() {
        return currentValue;
    }
    
    public float getFraction() {
        return (float)currentValue/maximumValue;
    }
}
