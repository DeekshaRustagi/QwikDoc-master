package main.java.cz2006project.mojojo.Entity.Utils;

/**
 * Created by srishti on 30/3/15.
 */

import android.content.Context;
import android.util.AttributeSet;


public class TypeFaceTextView extends android.widget.TextView {

    public TypeFaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            /**
             * Searching for the typeface attribute
             */
            if (attrs.getAttributeName(i).equals("typeface")) {
                String typeface = attrs.getAttributeValue(i);
                if (!isInEditMode()) {
                    try {
                        setTypeface(CustomType.getTypeface(context, typeface));
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}
