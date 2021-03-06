package main.java.cz2006project.mojojo.Entity.Utils;

/**
 * Created by srishti on 30/3/15.
 */

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;


public class CustomType {

    private static HashMap<String, Typeface> typefaces = new HashMap<String, Typeface>();
    public static Typeface getTypeface(Context context, String path){
        if (typefaces.containsKey(path)){
            return typefaces.get(path);
        }
        else{
            Typeface tf = Typeface.createFromAsset(context.getAssets(), path);
            typefaces.put(path, tf);
            return tf;
        }
    }
}