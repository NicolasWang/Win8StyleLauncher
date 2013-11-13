package com.jsmobile.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jsmobile.data.Element;
import com.jsmobile.data.ElementData;
import com.jsmobile.data.LauncherLayout;

import java.util.List;
import java.util.Map;

/**
 * Created by wangxin on 11/12/13.
 */
public class ListElement extends BaseElement {
    public ListElement(Context context, LauncherLayout.ElementLayoutInfo elementLayoutInfo, Element element) {
        super(context, elementLayoutInfo, element);
    }

    @Override
    protected void initElement() {
        mListView = new ListView(this.getContext());
        mListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        this.addView(mListView);
    }

    static class CustomAdapter extends BaseAdapter {
        private Context mContext;
        private List<ElementData> mDatas;

        public CustomAdapter(Context context, List<ElementData> elementDataList){
            mContext = context;
            mDatas = elementDataList;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            return null;
        }
    }

    private int getItemHeight(){
        return 0;
    }

    private ListView mListView;
}
