package wge3.game.engine;

public enum Command {
    FORWARD(0),
    BACKWARD(1),
    TURN_LEFT(2),
    TURN_RIGHT(3),
    USE_ITEM(4),
    CHANGE_ITEM(5),
    RUN(6),
    TOGGLE_FPS(7),
    EXIT(8),
    TOGGLE_FOV(9),
    TOGGLE_GHOST_MODE(10),
    TOGGLE_INVENTORY(11),
    SPAWN_WALL(12),
    DESTROY_OBJECT(13);
    
    public final int code;
    public static final int numberOfCommands = 14;
    
    private Command(int code) {
        this.code = code;
    }
}
