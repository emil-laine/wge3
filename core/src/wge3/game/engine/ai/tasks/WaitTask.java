package wge3.game.engine.ai.tasks;

import static com.badlogic.gdx.utils.TimeUtils.millis;

public final class WaitTask extends AITask {
    
    private int duration;
    private long startTime;
    
    public WaitTask(int duration) {
        this.duration = duration;
        startTime = millis();
    }
    
    @Override
    public boolean isFinished() {
        return millis() - startTime > duration;
    }
    
    @Override
    public void execute() {
    }
}
