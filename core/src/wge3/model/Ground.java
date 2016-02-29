/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import wge3.engine.util.Config;

public class Ground extends TileLayer {
    
    private final int hpDrainAmount;
    
    private final static Config cfg = new Config("config/ground.toml");
    
    public Ground(String type) {
        super(cfg, type);
        hpDrainAmount = cfg.getInt(type, "hpDrainAmount");
    }
    
    public int getHPDrainAmount() {
        return hpDrainAmount;
    }
}
