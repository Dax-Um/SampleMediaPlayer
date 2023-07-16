package com.example.samplemediaplayer.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class ViewUtil {
    @BindingAdapter(value={"imgRes","drawableRes"}, requireAll=false)
    public static void loadImg(@NonNull ImageView imageView, Bitmap bitmap, int resId){
        if (bitmap != null){imageView.setImageBitmap(bitmap);}
        else {imageView.setImageResource(resId);}
    }
    @BindingAdapter({"customText"})
    public static void setCustomText(@NonNull TextView textView, String text){
        textView.setText(text);
        textView.setHorizontallyScrolling(true);
        textView.setSelected(true);
    }
}
