package com.baixiaohu.imagecompress.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.baixiaohu.imagecompress.utils.GlideUtils;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class PreviewAdapter extends PagerAdapter {
    private List<String> mFilePathData;
    private int mChildCount;

    public PreviewAdapter(List<String> filePathData) {
        this.mFilePathData = filePathData;
    }

    @Override
    public int getCount() {
        return mFilePathData == null ? 0 : mFilePathData.size();
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public PhotoView instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        container.addView(photoView);
        ViewGroup.LayoutParams layoutParams = photoView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        photoView.setLayoutParams(layoutParams);
        GlideUtils.showImage(container.getContext(), mFilePathData.get(position), photoView);
        return photoView;
    }

    public static class PreviewPageTransformer implements ViewPager.PageTransformer {
        private ViewPager viewPager;
        private int mTranslateOffsetX = getMeasureValue(128);

        @Override
        public void transformPage(@NonNull View view, float position) {
            if (viewPager == null) {
                viewPager = (ViewPager) view.getParent();
            }

            int leftInScreen = view.getLeft() - viewPager.getScrollX();
            float offsetRate = (float) leftInScreen * 0.08f / viewPager.getMeasuredWidth();
            float scaleFactor = 1 - Math.abs(offsetRate);
            if (scaleFactor > 0) {
                view.setScaleX(scaleFactor);
                //view.setScaleY(scaleFactor);
                view.setTranslationX(-mTranslateOffsetX * offsetRate);
            }
        }

    }
    private static int getMeasureValue(int px) {
        return getScreenW() * px / 1080;
    }

    private static int getScreenW() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return dm.widthPixels;
    }
}
