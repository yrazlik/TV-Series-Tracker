package com.yrazlik.tvseriestracker.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.view.CircleTransform;

/**
 * Created by yrazlik on 29/10/17.
 */

public class PicassoImageLoader implements IImageLoader{

    private static PicassoImageLoader mInstance;
    private Context mContext;
    private int placeholderResId;
    private int errorResId;

    public static PicassoImageLoader getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new PicassoImageLoader();
            mInstance.setPlaceholder(R.drawable.placeholder);
            mInstance.setErrorResId(R.drawable.placeholder);
        }
        mInstance.setContext(context);
        return mInstance;
    }

    private void setContext(Context context) {
        mInstance.mContext = context;
    }

    private PicassoImageLoader() {}

    public PicassoImageLoader setPlaceholder(int placeholder) {
        mInstance.placeholderResId = placeholder;
        return mInstance;
    }

    public PicassoImageLoader setErrorResId(int errorResId) {
        mInstance.errorResId = errorResId;
        return mInstance;
    }

    @Override
    public void loadImage(String url, ImageView iv) {
        if(url == null || url.isEmpty()) {
            iv.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.with(mContext).load(url).placeholder(placeholderResId).error(errorResId).into(iv);
        }
    }

    @Override
    public void loadCircleImage(String url, ImageView iv) {
        if(url == null || url.isEmpty()) {
            Picasso.with(mContext).load(url).placeholder(placeholderResId).error(errorResId).transform(new CircleTransform()).into(iv);
        } else {
            Picasso.with(mContext).load(url).placeholder(placeholderResId).error(errorResId).into(iv);
        }
    }
}
