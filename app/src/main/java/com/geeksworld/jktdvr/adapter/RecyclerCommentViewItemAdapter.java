package com.geeksworld.jktdvr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geeksworld.jktdvr.R;
import com.geeksworld.jktdvr.model.CommentItemModel;
import com.geeksworld.jktdvr.model.HomeItemModel;
import com.geeksworld.jktdvr.tools.ShareKey;

import java.util.List;

/**
 * Created by xhs on 2018/3/30.
 */



public class RecyclerCommentViewItemAdapter extends RecyclerView.Adapter {

    public SharedPreferences share;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    private OnItemClickListener mItemClickListener;
    public void setItemClickListener(OnItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }



    List<CommentItemModel> list;//存放数据
    Context context;

    private final DisplayMetrics metrics;

    public RecyclerCommentViewItemAdapter(List<CommentItemModel> list, Context context) {
        this.list = list;
        this.context = context;

        metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        share = ShareKey.getShare(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder  holder = new MyFrag0ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
        return holder;
    }


    @Override
    public int getItemViewType(int position) {
        CommentItemModel model = list.get(position);

        return 0;
    }

    //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
// 在这里对获取对象进行操作
    //holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
    //position是点击位置

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //子项的点击事件监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "点击子项"+ position, Toast.LENGTH_SHORT).show();
                if (mItemClickListener!=null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });

        CommentItemModel model = list.get(position);

        if(holder instanceof MyFrag0ViewHolder){
            final MyFrag0ViewHolder frag0ViewHolder = (MyFrag0ViewHolder) holder;

            frag0ViewHolder.nameTextView.setText(model.getUserName());
            frag0ViewHolder.contentTextView.setText(model.getContent());
            frag0ViewHolder.timeTextView.setText(model.getCreateTime());


            String imgUrl = list.get(position).getHeadImg();
            Glide.with(context)
                    .load(imgUrl)
                    .error(R.mipmap.image_default_16_9)
                    .fallback(R.mipmap.image_default_16_9)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(metrics.widthPixels, metrics.widthPixels * 9 / 16)
                    .crossFade()
                    .thumbnail(0.6f)
                    .into(frag0ViewHolder.showImageView);

            frag0ViewHolder.position = position;


        }

    }



    //要显示的子项数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作

    public class MyFrag0ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView contentTextView;
        ImageView showImageView;
        TextView timeTextView;

        int position;
        public MyFrag0ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            showImageView = itemView.findViewById(R.id.showImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            showImageView = itemView.findViewById(R.id.showImageView);
        }
    }



    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public CommentItemModel getItem(int position){
        return list.get(position);
    }

    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, CommentItemModel model) {
        if (position < list.size() && position >= 0) {
            list.add(position, model);
            notifyItemInserted(position);
            return true;
        }
        return false;
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < list.size() && position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    //清空显示数据
    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }
}
