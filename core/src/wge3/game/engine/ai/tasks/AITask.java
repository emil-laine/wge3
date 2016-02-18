/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.game.engine.ai.tasks;

public abstract class AITask {
    
    public abstract void execute();
    public abstract boolean isFinished();
}
