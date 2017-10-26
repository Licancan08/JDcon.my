package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.entity.ListBean1;

/**
 * Created by robot on 2017/10/10.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ViewHolder holder;
    Context context;
    List<ListBean1> listRv;


    public RecyclerViewAdapter(Context context,List<ListBean1> listRv) {
        this.context=context;
        this.listRv=listRv;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context,R.layout.recycler_item,null);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_Rname.setText(listRv.get(position).tv);
        Glide.with(context).load(listRv.get(position).getIv()).into(holder.iv_Rimg);
    }



    @Override
    public int getItemCount() {
        return listRv.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_Rname;
        public ImageView iv_Rimg;
        public ViewHolder(View view){
            super(view);
            iv_Rimg=view.findViewById(R.id.iv_Rimg);
            tv_Rname=view.findViewById(R.id.tv_Rname);
        }
    }
}
