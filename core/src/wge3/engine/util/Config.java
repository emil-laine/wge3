/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public final class Config {
    
    private JsonValue json;
    
    public Config(String filePath) {
        json = new JsonReader().parse(Gdx.files.internal(filePath));
    }
    
    public int getInt(String type, String key) {
        JsonValue value = get(type, key);
        if (value.isArray()) {
            return MathUtils.random(value.getInt(0), value.getInt(1));
        }
        return value.asInt();
    }
    
    public float getFloat(String type, String key) {
        JsonValue value = get(type, key);
        if (value.isArray()) {
            return MathUtils.random(value.getFloat(0), value.getFloat(1));
        }
        return value.asFloat();
    }
    
    public String getString(String type, String key) {
        return get(type, key).asString();
    }
    
    public int getIntX(String type, String key) {
        return json.get(type).get(key).get(0).asInt();
    }
    
    public int getIntY(String type, String key) {
        return json.get(type).get(key).get(1).asInt();
    }
    
    private JsonValue get(String type, String key) {
        JsonValue value = json.get(type).get(key);
        if (value == null) {
            value = json.get("default").get(key);
        }
        return value;
    }
}
