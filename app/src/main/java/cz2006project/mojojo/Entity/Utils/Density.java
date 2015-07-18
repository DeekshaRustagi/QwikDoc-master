package main.java.cz2006project.mojojo.Entity.Utils;

/**
 * Created by srishti on 28/3/15.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;


class Density {
    public static int dp2px(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }
}
