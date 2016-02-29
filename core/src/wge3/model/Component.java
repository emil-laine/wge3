/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

public abstract class Component {
    
    private MapObject owner;
    
    void setOwner(MapObject owner) {
        this.owner = owner;
    }
    
    protected final MapObject getOwner() {
        return owner;
    }
    
    /** Called in {@link Area#passTime(float)}. */
    public void update() {}
    
    /** Called when an Entity's bounds overlap the component owner's bounds. */
    public void entityTouches(Entity toucher) {}
    
    /** Called when a Creature "uses" this Entity. */
    public void use(Entity user) {}
}
