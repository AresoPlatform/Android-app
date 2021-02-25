package com.xw.aschwitkey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.xw.aschwitkey.R;

import androidx.appcompat.widget.AppCompatEditText;

public class TypefaceEditText extends AppCompatEditText {
    public TypefaceEditText(Context context) {
        super(context);
    }

    public TypefaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TypefaceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceEditText);
        String type = typedArray.getString(R.styleable.TypefaceEditText_face);
        if (null == type) {
            return;
        }
        Typeface typeface = null;
        switch (type) {
            case "impact":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/impact.ttf");
                setTypeface(typeface);
                break;
            case "heavy":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/AlibabaSans-Heavy.otf");
                setTypeface(typeface);
                break;
            case "medium":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/AlibabaSans-Medium.otf");
                setTypeface(typeface);
                break;
            case "regular":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/AlibabaSans-Regular.otf");
                setTypeface(typeface);
                break;
            case "italic":
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/AlibabaSans-Italic.otf");
                setTypeface(typeface);
                break;
            case "systemDefault":
                setTypeface(Typeface.DEFAULT);
                break;
        }
        if (typedArray != null) {
            typedArray.recycle();
        }
        typeface = null;
    }

}
