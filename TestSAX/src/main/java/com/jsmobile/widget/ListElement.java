package com.jsmobile.widget;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jsmobile.app.R;
import com.jsmobile.data.Element;
import com.jsmobile.data.ElementData;
import com.jsmobile.data.LauncherLayout;

import java.util.List;
import java.util.Map;

/**
 * Created by wangxin on 11/12/13.
 */
public class ListElement extends BaseElement {
    private ListView mListView;
    private BaseElement mLinkElement;

    //假设link的元素只能在list元素的左边或右边
    static final int LINK_AT_LEFT = 1;
    static final int LINK_AT_RIGHT = 2;
    private int linkPos;

    public ListElement(Context context, LauncherLayout.ElementLayoutInfo elementLayoutInfo, Element element) {
        super(context, elementLayoutInfo, element);
    }

    @Override
    protected void initElement() {

        mListView = new ListView(this.getContext());
        ListView.LayoutParams param = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mListView.setLayoutParams(param);
        Log.d("wangx", "listview: " + mListView);
        mListView.setFocusable(false);

        this.addView(mListView);
        this.setFocusable(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup parent = (ViewGroup) this.getParent();
        Log.d("wangx", "parent tag: " + parent.getTag() + ", linkElementId: " + getLinkElementId());
        mLinkElement = (BaseElement) parent.findViewWithTag(getLinkElementId());
        if(mLinkElement.getElementLeft() > this.getElementLeft()){
            linkPos = LINK_AT_RIGHT;
            mListView.setPadding(ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN, 0, ElementLayout.DEFAULT_ELEMENT_MARGIN);
        } else if(mLinkElement.getElementLeft() < this.getElementLeft()){
            linkPos = LINK_AT_LEFT;
            mListView.setPadding(0, ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN, ElementLayout.DEFAULT_ELEMENT_MARGIN);
        }

        int itemHeight = ((this.getElementHeight()-1)*ElementLayout.DEFAULT_ELEMENT_MARGIN + this.getElementHeight()*ElementLayout.DEFAULT_CELL_HEIGHT) / this.getAllElementDatas().size();
        mListView.setAdapter(new CustomAdapter(this.getContext(), this.getAllElementDatas(), itemHeight, linkPos));
    }

    static class CustomAdapter extends BaseAdapter {
        private Context mContext;
        private List<ElementData> mDatas;
        private int mItemHeight;
        private int mLinkPos;

        public CustomAdapter(Context context, List<ElementData> elementDataList, int itemHeight, int linkPos){
            mContext = context;
            mDatas = elementDataList;
            mItemHeight = itemHeight;
            mLinkPos = linkPos;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int pos) {
            return mDatas.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup viewGroup) {
            if(convertView == null){
                final ViewHolder holder = new ViewHolder();
                LinearLayout ll = new LinearLayout(mContext);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setGravity(Gravity.CENTER);
                ListView.LayoutParams lparam = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.setLayoutParams(lparam);
                ll.setMinimumHeight(mItemHeight);

                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ElementLayout.DEFAULT_ELEMENT_MARGIN, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setBackgroundResource(android.R.color.holo_red_light);
                imageView.setVisibility(View.INVISIBLE);

                TextView textView = new TextView(mContext);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                param.weight = 1;
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(param);
                textView.setText(mDatas.get(pos).getElementDesc());

                if(mLinkPos == ListElement.LINK_AT_LEFT){
                    ll.addView(imageView);
                    ll.addView(textView);
                } else if(mLinkPos == ListElement.LINK_AT_RIGHT){
                    ll.addView(textView);
                    ll.addView(imageView);
                }

                holder.mImageView = imageView;
                holder.mTextView = textView;

                ll.setTag(holder);
                ll.setFocusable(false);
                textView.setFocusable(true);
//                textView.setBackgroundResource(R.drawable.imagefocus);
                textView.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        Log.d("wangx", "textView onFocusChange " + b);
                        if(b){
                            holder.mImageView.setVisibility(View.VISIBLE);
//                            holder.mTextView.setText("i am selected");
                        } else {
                            holder.mImageView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
//                Log.d("wangx", "convertView: " + convertView.getParent());

                return ll;
            } else {
                final ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.mImageView.setVisibility(View.INVISIBLE);
                holder.mTextView.setText(mDatas.get(pos).getElementDesc());
                holder.mTextView.setFocusable(true);
                holder.mTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(b)holder.mImageView.setVisibility(View.VISIBLE);
                    }
                });
                convertView.setFocusable(false);
                Log.d("wangx", "convertView: " + convertView.getParent());
                return convertView;
            }
        }

        static class ViewHolder{
            ImageView mImageView;
            TextView mTextView;
        }
    }

    public String getLinkElementId(){
        return mElement.getLinkElementId();
    }
}
