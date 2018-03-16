package com.zk.zk_online.weight;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.youth.banner.loader.ImageLoader;
import com.zk.zk_online.R;

/**
 * Created by ZYB on 2017/11/21 0021.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, final ImageView imageView) {
        Glide.with(context).load(path.toString()).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setImageDrawable(resource);
                        return false;
                    }
                }).
                into(imageView);
    }
}
