package com.jsmobile.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsmobile.data.Element;
import com.jsmobile.data.ElementData;
import com.jsmobile.data.LauncherLayout;
import com.jsmobile.app.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by wangxin on 10/31/13.
 */
public class ImageElement extends BaseElement {
    private RelativeLayout mRoot;
    private ImageView mImage;
    private TextView mText;

    public ImageElement(Context context, LauncherLayout.ElementLayoutInfo elementLayoutInfo, Element element) {
        super(context, elementLayoutInfo, element);
    }

    @Override
    protected void initElement() {
        mRoot = new RelativeLayout(this.getContext());
        FrameLayout.LayoutParams fparam = new LayoutParams(getElementWidth()*ElementLayout.DEFAULT_CELL_WIDTH + (getElementWidth()-1)*ElementLayout.DEFAULT_ELEMENT_MARGIN,
                getElementHeight()*(ElementLayout.DEFAULT_CELL_HEIGHT + ElementLayout.DEFAULT_ELEMENT_MARGIN) - ElementLayout.DEFAULT_ELEMENT_MARGIN);
        fparam.gravity = Gravity.CENTER;
        mRoot.setLayoutParams(fparam);

        mImage = new ImageView(getContext());
        mImage.setScaleType(ImageView.ScaleType.FIT_XY);//TODO
        mImage.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));//TODO

        mText = new TextView(getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.setMargins(10, 0, 0, 6);//TODO
        mText.setLayoutParams(lp);
        mText.setTextSize(13);//TODO

        //TODO
        ElementData data = this.getElementData(0);
        String url = data.getContentUrl();
        ImageLoader.getInstance().displayImage("http://192.168.8.97/" + url, mImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        mText.setText(data.getElementDesc());

        mRoot.addView(mImage);
        mRoot.addView(mText);
        mRoot.setFocusable(this.isCanFocus());
        mRoot.setBackgroundResource(R.drawable.imagefocus);

        this.addView(mRoot);
//        this.setPadding(20,20,20,20);
        this.setFocusable(false);
//        this.setBackgroundResource(R.drawable.imagefocus);
//        this.setFocusable();
    }

}
