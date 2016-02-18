/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

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
    
    public float asFraction() {
        return (float)currentValue/maximumValue;
    }
}
