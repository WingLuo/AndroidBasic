package tech.bootloader.androidbasic.glide;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import tech.bootloader.androidbasic.R;
import tech.bootloader.androidbasic.glide.svg.GlideApp;
import tech.bootloader.androidbasic.glide.svg.SvgSoftwareLayerSetter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GlideLoadUtils {


    /**
     * 将图片放在ImageView中
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void imageIntoImageView(Context context, String url, ImageView imageView) {

        if (context == null) {
            Log.e("Exception", "参数Context不允许为null！！！");
            return;
//			throw new IllegalArgumentException("参数Context不允许为null！！！");

        } else if (TextUtils.isEmpty(url)) {
            Log.e("Exception", "参数url不允许为null！！！");
            //			throw new IllegalArgumentException("参数url不允许为null！！！");
            return;
        } else if (imageView == null) {
            Log.e("Exception", "参数imageView不允许为null！！！");
            //			throw new IllegalArgumentException("参数imageView不允许为null！！！");
            return;
        }
        try {

            if (url.toLowerCase().endsWith("svg")) {
                GlideApp.with(context)
                        .as(PictureDrawable.class)
                        .placeholder(R.drawable.svg_image_loading)
                        .error(android.R.drawable.ic_menu_report_image)
                        .transition(withCrossFade())
                        .listener(new SvgSoftwareLayerSetter()).
                        load(url).into(imageView);
            } else {
                RequestOptions options = new RequestOptions()
//                    .centerCrop()
//				.override(100, 100)//指定加载图片的大小
                        //显示预加载图片,在某种状态下失败
               .placeholder(R.drawable.image_loading)
                        .error(android.R.drawable.ic_menu_report_image)
                        //设置下载优先级
                        .priority(Priority.HIGH)

//				.skipMemoryCache(true)//跳过内存缓存
//              .signature(new StringSignature(yourVersionMetadata))
                        .dontAnimate()//取消淡入淡出动画
//缓存策略策略解说：
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
//       all:缓存源资源和转换后的资源none:不作任何磁盘缓存source:缓存源资源result：缓存转换后的资源

                TransitionOptions transitionOptions = new DrawableTransitionOptions();
//.crossFade(5000) //淡入淡出 可以不写默认300
                Glide.with(context)
                        .load(new GlideUrl(url))
                        .apply(options)
                        .transition(transitionOptions)
                        .thumbnail(0.5f)
                        .into(imageView);
            }


        } catch (Exception e) {
            imageView.setImageBitmap(null);
            e.printStackTrace();
        }

    }


    private static class GlideUrl extends com.bumptech.glide.load.model.GlideUrl {
        private String url;

        public GlideUrl(String url) {
            super(url);
            this.url = url;
        }

        @Override
        public String getCacheKey() {
            return url.replace(findTokenParam(), "");
        }

        private String findTokenParam() {
            String tokenParam = "";
            int tokenKeyIndex = url.indexOf("?token=") >= 0 ? url.indexOf("?token=") : url.indexOf("&token=");
            if (tokenKeyIndex != -1) {
                int nextKeyIndex = url.indexOf("&", tokenKeyIndex + 1);
                if (nextKeyIndex != -1) {
                    tokenParam = url.substring(tokenKeyIndex + 1, nextKeyIndex + 1);
                } else {
                    tokenParam = url.substring(tokenKeyIndex);
                }
            }
            return tokenParam;
        }
    }
}