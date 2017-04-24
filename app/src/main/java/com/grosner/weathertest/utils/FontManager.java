package com.grosner.weathertest.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 */
public class FontManager {

    private Map<String, Typeface> typefaceMap;

    private final Context context;

    public FontManager(Context context) {
        this.context = context;
    }

    public Typeface getOrCreateTypeFace(AssetManager assetManager, String name) {
        Typeface typeface = getTypefaceMap().get(name);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(assetManager, "fonts/" + name + ".ttf");
            typefaceMap.put(name, typeface);
        }
        return typeface;
    }

    public Typeface getOrCreateTypeFace(String name) {
        return getOrCreateTypeFace(context.getResources().getAssets(), name);
    }

    private Map<String, Typeface> getTypefaceMap() {
        if (typefaceMap == null) {
            typefaceMap = new HashMap<>();
        }
        return typefaceMap;
    }
}
