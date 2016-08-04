/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import java.util.List;
import me.grison.jtoml.impl.Toml;

public final class Config {
    
    private Toml toml;
    
    public Config(String filePath) {
        toml = Toml.parse(Gdx.files.internal(filePath).readString());
    }
    
    public int getInt(String type, String key) {
        Object value = get(type, key);
        if (value instanceof List) {
            List<Object> list = (List) value;
            return MathUtils.random(toInt(list.get(0)), toInt(list.get(1)));
        }
        return toInt(value);
    }
    
    public float getFloat(String type, String key) {
        Object value = get(type, key);
        if (value instanceof List) {
            List<Object> list = (List) value;
            return MathUtils.random(toFloat(list.get(0)), toFloat(list.get(1)));
        }
        return toFloat(value);
    }
    
    public boolean getBoolean(String type, String key) {
        return (boolean) get(type, key);
    }
    
    public String getString(String type, String key) {
        return (String) get(type, key);
    }
    
    public int getIntX(String type, String key) {
        return toInt(toml.getList(toPath(type, key)).get(0));
    }
    
    public int getIntY(String type, String key) {
        return toInt(toml.getList(toPath(type, key)).get(1));
    }
    
    public List<String> getStringList(String type, String key) {
        Object value = get(type, key);
        return (List<String>) value;
    }
    
    private Object get(String type, String key) {
        Object value = toml.get(toPath(type, key));
        if (value == null) {
            value = toml.get(toPath("default", key));
            if (value == null) {
                throw new IllegalArgumentException("No key \"" + key
                        + "\" found for type \"" + type + "\"");
            }
        }
        return value;
    }
    
    private static int toInt(Object value) {
        return (int) (long) value;
    }
    
    private static float toFloat(Object value) {
        if (value instanceof Long) {
            return (float) (long) value;
        } else if (value instanceof Double) {
            return (float) (double) value;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    private static String toPath(String type, String key) {
        return type + "." + key;
    }
}
