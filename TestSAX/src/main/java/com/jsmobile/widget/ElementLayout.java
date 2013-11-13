package com.jsmobile.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wangxin on 10/31/13.
 */
public class ElementLayout extends ViewGroup {
    private int mCellWidth = DEFAULT_CELL_WIDTH;
    private int mCellHeight = DEFAULT_CELL_HEIGHT;
    private int mCellMarginH = DEFAULT_ELEMENT_MARGIN;//cell水平空隙大小
    private int mCellMarginV = DEFAULT_ELEMENT_MARGIN;//cell垂直方向空隙大小

    public static final int DEFAULT_CELL_WIDTH = 200;
    public static final int DEFAULT_CELL_HEIGHT = 140;

    public static final int DEFAULT_PADDING_LEFT = 100;
    public static final int DEFAULT_PADDING_TOP = 147;

    public static final int DEFAULT_ELEMENT_MARGIN = 20;

    public ElementLayout(Context context) {
        super(context);
        init();
    }

    protected void init(){
        setPadding(DEFAULT_PADDING_LEFT - DEFAULT_ELEMENT_MARGIN, DEFAULT_PADDING_TOP - DEFAULT_ELEMENT_MARGIN, 0, 0);
    }

    public void setCellWidth(int cellWidth){
        mCellWidth = cellWidth;
    }

    public int getCellWidth(){
        return mCellWidth;
    }

    public void setCellHeight(int cellHeight){
        mCellHeight = cellHeight;
    }

    public int getCellHeight(){
        return mCellHeight;
    }

    public int getCellMarginV() {
        return mCellMarginV;
    }

    public void setCellMarginV(int mCellMarginV) {
        this.mCellMarginV = mCellMarginV;
    }

    public int getCellMarginH() {
        return mCellMarginH;
    }

    public void setCellMarginH(int mCellMarginH) {
        this.mCellMarginH = mCellMarginH;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            if(getChildAt(i).getVisibility() != View.GONE){
                final LayoutParams lp = (LayoutParams)getChildAt(i).getLayoutParams();
                int widthNum = lp.elementwidth;
                int heightNum = lp.elementheight;
                int widthMS = MeasureSpec.makeMeasureSpec(mCellWidth * widthNum + (widthNum-1)*mCellMarginH, MeasureSpec.EXACTLY);
                int heightMS = MeasureSpec.makeMeasureSpec(mCellHeight * heightNum + (heightNum-1)*mCellMarginV, MeasureSpec.EXACTLY);
                getChildAt(i).measure(widthMS, heightMS);
            }
        }

        setMeasuredDimension(resolveSize(this.getLayoutParams().width, widthMeasureSpec),
                resolveSize(this.getLayoutParams().height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            if(getChildAt(i).getVisibility() != View.GONE){
                final LayoutParams lp = (LayoutParams)getChildAt(i).getLayoutParams();
                int childLeft = getPaddingLeft() + (lp.elementleft - 1)*mCellWidth + (lp.elementleft - 1)*mCellMarginH;
                int childTop = getPaddingTop() + (lp.elementtop - 1)*mCellHeight + (lp.elementtop - 1)*mCellMarginV;
                int childRight = childLeft + lp.elementwidth * mCellWidth + (lp.elementwidth - 1)*mCellMarginH;
                int childBottom = childTop + lp.elementheight*mCellHeight + (lp.elementheight - 1)*mCellMarginV;

                getChildAt(i).layout(childLeft, childTop, childRight, childBottom);
            }
        }
    }

    public static class LayoutParams extends MarginLayoutParams{
        public int elementwidth;
        public int elementheight;
        public int elementleft;
        public int elementtop;


        public LayoutParams(int width, int height) {
            super(width, height);
            elementwidth = width;
            elementheight = height;
        }
    }

    public BaseElement getElementByPos(int left, int top){
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            BaseElement baseElement = (BaseElement) getChildAt(i);
            if(baseElement.getElementLeft() == left && baseElement.getElementTop() == top)
                return baseElement;
        }
        return null;
    }
}
