/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  This code was originally part of the Apache Harmony project.
 *  The Apache Harmony project has been discontinued.
 *  That's why we imported the code into iText.
 */
/**
 * @author Alexey A. Petrenko
 */
package com.itextpdf.awt.geom.misc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * RenderingHints
 *
 */
public class RenderingHints implements Map<Object, Object>, Cloneable {
    public static final Key KEY_ALPHA_INTERPOLATION = new KeyImpl(1);
    public static final Object VALUE_ALPHA_INTERPOLATION_DEFAULT = new KeyValue(KEY_ALPHA_INTERPOLATION);
    public static final Object VALUE_ALPHA_INTERPOLATION_SPEED = new KeyValue(KEY_ALPHA_INTERPOLATION);
    public static final Object VALUE_ALPHA_INTERPOLATION_QUALITY = new KeyValue(KEY_ALPHA_INTERPOLATION);

    public static final Key KEY_ANTIALIASING = new KeyImpl(2);
    public static final Object VALUE_ANTIALIAS_DEFAULT = new KeyValue(KEY_ANTIALIASING);
    public static final Object VALUE_ANTIALIAS_ON = new KeyValue(KEY_ANTIALIASING);
    public static final Object VALUE_ANTIALIAS_OFF = new KeyValue(KEY_ANTIALIASING);

    public static final Key KEY_COLOR_RENDERING = new KeyImpl(3);
    public static final Object VALUE_COLOR_RENDER_DEFAULT = new KeyValue(KEY_COLOR_RENDERING);
    public static final Object VALUE_COLOR_RENDER_SPEED = new KeyValue(KEY_COLOR_RENDERING);
    public static final Object VALUE_COLOR_RENDER_QUALITY = new KeyValue(KEY_COLOR_RENDERING);

    public static final Key KEY_DITHERING = new KeyImpl(4);
    public static final Object VALUE_DITHER_DEFAULT = new KeyValue(KEY_DITHERING);
    public static final Object VALUE_DITHER_DISABLE = new KeyValue(KEY_DITHERING);
    public static final Object VALUE_DITHER_ENABLE = new KeyValue(KEY_DITHERING);

    public static final Key KEY_FRACTIONALMETRICS = new KeyImpl(5);
    public static final Object VALUE_FRACTIONALMETRICS_DEFAULT = new KeyValue(KEY_FRACTIONALMETRICS);
    public static final Object VALUE_FRACTIONALMETRICS_ON = new KeyValue(KEY_FRACTIONALMETRICS);
    public static final Object VALUE_FRACTIONALMETRICS_OFF = new KeyValue(KEY_FRACTIONALMETRICS);

    public static final Key KEY_INTERPOLATION = new KeyImpl(6);
    public static final Object VALUE_INTERPOLATION_BICUBIC = new KeyValue(KEY_INTERPOLATION);
    public static final Object VALUE_INTERPOLATION_BILINEAR = new KeyValue(KEY_INTERPOLATION);
    public static final Object VALUE_INTERPOLATION_NEAREST_NEIGHBOR = new KeyValue(KEY_INTERPOLATION);

    public static final Key KEY_RENDERING = new KeyImpl(7);
    public static final Object VALUE_RENDER_DEFAULT = new KeyValue(KEY_RENDERING);
    public static final Object VALUE_RENDER_SPEED = new KeyValue(KEY_RENDERING);
    public static final Object VALUE_RENDER_QUALITY = new KeyValue(KEY_RENDERING);

    public static final Key KEY_STROKE_CONTROL = new KeyImpl(8);
    public static final Object VALUE_STROKE_DEFAULT = new KeyValue(KEY_STROKE_CONTROL);
    public static final Object VALUE_STROKE_NORMALIZE = new KeyValue(KEY_STROKE_CONTROL);
    public static final Object VALUE_STROKE_PURE = new KeyValue(KEY_STROKE_CONTROL);

    public static final Key KEY_TEXT_ANTIALIASING = new KeyImpl(9);
    public static final Object VALUE_TEXT_ANTIALIAS_DEFAULT = new KeyValue(KEY_TEXT_ANTIALIASING);
    public static final Object VALUE_TEXT_ANTIALIAS_ON = new KeyValue(KEY_TEXT_ANTIALIASING);
    public static final Object VALUE_TEXT_ANTIALIAS_OFF = new KeyValue(KEY_TEXT_ANTIALIASING);

    private HashMap<Object, Object> map = new HashMap<Object, Object>();
    
    public RenderingHints(Map<Key, ?> map) {
        super();
        if (map != null) {
            putAll(map);
        }
    }

    public RenderingHints(Key key, Object value) {
        super();
        put(key, value);
    }

    public void add(RenderingHints hints) {
        map.putAll(hints.map);
    }

    public Object put(Object key, Object value) {
        if (!((Key)key).isCompatibleValue(value)) {
            throw new IllegalArgumentException();
        }

        return map.put(key, value);
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public Set<Object> keySet() {
        return map.keySet();
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    public void putAll(Map<?, ?> m) {
        if (m instanceof RenderingHints) {
            map.putAll(((RenderingHints) m).map);
        } else {
            Set<?> entries = m.entrySet();

            if (entries != null){
                Iterator<?> it = entries.iterator();
                while (it.hasNext()) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) it.next();
                    Key key = (Key) entry.getKey();
                    Object val = entry.getValue();
                    put(key, val);
                }
            }
        }
    }

    public Collection<Object> values() {
        return map.values();
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        return map.containsKey(key);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        map.clear();
    }

    public int size() {
        return map.size();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map<?, ?>)) {
            return false;
        }

        Map<?, ?> m = (Map<?, ?>)o;
        Set<?> keys = keySet();
        if (!keys.equals(m.keySet())) {
            return false;
        }

        Iterator<?> it = keys.iterator();
        while (it.hasNext()) {
            Key key = (Key)it.next();
            Object v1 = get(key);
            Object v2 = m.get(key);
            if (!(v1==null?v2==null:v1.equals(v2))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        RenderingHints clone = new RenderingHints(null);
        clone.map = (HashMap<Object, Object>)this.map.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "RenderingHints["+map.toString()+"]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Key
     */
    public abstract static class Key {
        private final int key;

        protected Key(int key) {
            this.key = key;
        }

        @Override
        public final boolean equals(Object o) {
            return this == o;
        }

        @Override
        public final int hashCode() {
            return System.identityHashCode(this);
        }

        protected final int intKey() {
            return key;
        }

        public abstract boolean isCompatibleValue(Object val);
    }

    /**
     * Private implementation of Key class
     */
    private static class KeyImpl extends Key {

        protected KeyImpl(int key) {
            super(key);
        }

        @Override
        public boolean isCompatibleValue(Object val) {
            if (!(val instanceof KeyValue)) {
                return false;
            }

            return ((KeyValue)val).key == this;
        }
    }

    /**
     * Private class KeyValue is used as value for Key class instance.
     */
    private static class KeyValue {
        private final Key key;

        protected KeyValue(Key key) {
            this.key = key;
        }
    }
}
