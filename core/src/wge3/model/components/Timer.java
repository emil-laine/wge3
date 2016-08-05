/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model.components;

import wge3.model.Component;

@SuppressWarnings("unused") // used via reflection
public class Timer extends Component {
    
    protected final com.badlogic.gdx.utils.Timer timer;
    protected final com.badlogic.gdx.utils.Timer.Task task;
    protected int seconds;
    
    public Timer(Runnable effect, int seconds) {
        timer = new com.badlogic.gdx.utils.Timer();
        task = new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                effect.run();
            }
        };
        this.seconds = seconds;
    }
    
    public void start() {
        timer.scheduleTask(task, seconds);
    }
    
    public void cancel() {
        timer.clear();
    }
}
