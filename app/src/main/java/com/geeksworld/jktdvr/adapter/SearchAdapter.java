package com.geeksworld.jktdvr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.model.HomeItemModel;

import java.util.List;

/**
 * Created by xhs on 2017/10/24.
 */

public class SearchAdapter<T> extends BaseAdapter {
    private final LayoutInflater inflater;
    private final RequestManager glide;
    private List<T> datas;
    private Context context;
    private int rId;

    public SearchAdapter(Context context, List<T> datas, int rId) {
        this.context = context;
        this.datas = datas;
        this.rId = rId;
        inflater = LayoutInflater.from(context);
        glide = Glide.with(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(rId, null);
            holder.searchTitleTextView = view.findViewById(R.id.searchTitleTextView);
            HomeItemModel homeItemModel = (HomeItemModel) datas.get(i);
            holder.searchTitleTextView.setText(homeItemModel.getTitle());

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//        if (rId == R.layout.item_search){
//            HomeItemModel data = (HomeItemModel) datas.get(i);
//            holder.name.setText(data.getTitle());
//            if (!Tool.isNull(data.getImgList().get(0)))
//                glide.load(data.getImgList().get(0)).into(holder.img);
//        }

        return view;
    }

    class ViewHolder {
        private TextView searchTitleTextView;

    }
}
