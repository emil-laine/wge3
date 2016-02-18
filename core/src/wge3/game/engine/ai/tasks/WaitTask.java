/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

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
