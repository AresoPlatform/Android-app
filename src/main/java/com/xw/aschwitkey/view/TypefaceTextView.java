package com.xw.aschwitkey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;


import com.xw.aschwitkey.R;

import androidx.appcompat.widget.AppCompatTextView;

public class TypefaceTextView extends AppCompatTextView {
    public TypefaceTextView(Context context) {
        this(context, null);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypefaceTextView(context, attrs);
    }

    private void initTypefaceTextView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);
        String type = typedArray.getString(R.styleable.TypefaceTextView_typeface);
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

    public void setTypeFace(Context context, String type) {
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
        typeface = null;
    }
}
