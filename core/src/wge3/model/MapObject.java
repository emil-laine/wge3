/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wge3.engine.util.Config;
import wge3.engine.util.StatIndicator;

public class MapObject extends TileLayer {
    
    private final String name;
    private final List<Component> components;
    
    protected StatIndicator hp;
    protected int hardness;
    protected float shadowDepth;
    
    private final static Config cfg = new Config("config/object.toml");
    
    public MapObject(String type, Object... params) {
        super(cfg, type);
        
        name = type.replaceAll("([a-z])([A-Z])", "$1 $2").toLowerCase();
        hp = new StatIndicator(cfg.getInt(type, "hp"));
        hardness = cfg.getInt(type, "hardness");
        shadowDepth = cfg.getFloat(type, "shadowDepth");
        
        initTilePropertyFlags(type);
        
        components = new ArrayList();
        initComponents(type);
    }
    
    private void initTilePropertyFlags(String type) {
        if (cfg.getBoolean(type, "blocksMovement"))
            propertyFlags.add(TilePropertyFlag.BLOCKS_MOVEMENT);
        if (cfg.getBoolean(type, "blocksVision"))
            propertyFlags.add(TilePropertyFlag.BLOCKS_VISION);
        if (cfg.getBoolean(type, "castsShadows"))
            propertyFlags.add(TilePropertyFlag.CASTS_SHADOWS);
        if (cfg.getBoolean(type, "hasDestroyedSprite"))
            propertyFlags.add(TilePropertyFlag.HAS_DESTROYED_SPRITE);
    }
    
    private void initComponents(String type) {
        for (String componentName : cfg.getStringList(type, "components")) {
            String className = "wge3.model.components." + componentName;
            Component component;
            
            try {
                component = (Component) Class.forName(className).newInstance();
            } catch (ClassNotFoundException
                    | InstantiationException
                    | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            
            component.setOwner(this);
            components.add(component);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }
    
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (component.getClass() == componentClass)
                return (T) component;
        }
        return null;
    }
    
    /** Deals damage to this object.
     *  @param amount how much damage to deal */
    public void dealDamage(int amount) {
        hp.decrease(amount);
    }
    
    /** Returns whether this object has taken enough damage to be destroyed. */
    public boolean isDestroyed() {
        return hp.isEmpty();
    }
    
    /** Returns whether this object type has a separate graphical representation
     *  to be shown when it's destroyed. */
    public boolean hasDestroyedSprite() {
        return propertyFlags.contains(TilePropertyFlag.HAS_DESTROYED_SPRITE);
    }
    
    /** Returns whether to draw shadows for this type of object. */
    public boolean castsShadows() {
        return propertyFlags.contains(TilePropertyFlag.CASTS_SHADOWS);
    }
    
    /** Returns the steepness of the lightness curve for the shadows cast by
     *  this object.
     *  @return in the range [0.0, 1.0]:
     *          1.0f - The shadow of this object is 100% the lightness on this
     *                 object, i.e. no visible shadows.
     *          0.0f - The shadow of this object is 0% the lightness on this
     *                 object, i.e. the shadow will be entirely black. */
    public float getShadowDepth() {
        return shadowDepth;
    }
}
