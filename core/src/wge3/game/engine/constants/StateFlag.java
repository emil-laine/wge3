package wge3.game.engine.constants;

public enum StateFlag {
    GOING_FORWARD  (1 << 0),
    GOING_BACKWARD (1 << 1),
    TURNING_LEFT   (1 << 2),
    TURNING_RIGHT  (1 << 3),
    
    IS_RUNNING     (1 << 4),
    IS_INVISIBLE   (1 << 5),
    IS_FLYING      (1 << 6),
    IS_GHOST       (1 << 7),
    
    PICKS_UP_ITEMS (1 << 8),
    SEES_EVERYTHING(1 << 9);
    
    private final int value;
    
    StateFlag(int value) {
        this.value = value;
    }
    
    public int value() {
        return value;
    }
}
