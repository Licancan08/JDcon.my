package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import licancan.com.jdconmy.DetailActivity;
import licancan.com.jdconmy.R;
import licancan.com.jdconmy.entity.SearchBigBean;

/**
 * Created by robot on 2017/10/16.
 */

public class SearchBigAdapter extends RecyclerView.Adapter<SearchBigAdapter.ViewHolder>{



    Context context;
    List<SearchBigBean.DataBean> list;
    public SearchBigAdapter(Context context, List<SearchBigBean.DataBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.search_item1,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.search_big_title.setText(list.get(position).getTitle());
        holder.search_big_price.setText("¥"+list.get(position).getSalenum()+"");
        String url = list.get(position).getImages().split("\\|")[0];
        holder.search_big_price.setTextColor(Color.RED);
        Glide.with(context).load(url).into(holder.search_big_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickLitener!=null){
                    mOnItemClickLitener.onItemClick(view,position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView search_big_title,search_big_price;
        private final ImageView search_big_img;

        public ViewHolder(View itemView) {
            super(itemView);
            search_big_img = itemView.findViewById(R.id.search_big_img);
            search_big_title=itemView.findViewById(R.id.search_big_title);
            search_big_price = itemView.findViewById(R.id.search_big_price);
        }
    }
    private OnItemClickLitener mOnItemClickLitener;
    //自定义接口  recyclerView的条目点击事件
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickLitener(OnItemClickLitener itemClickLitener)
    {
        mOnItemClickLitener = itemClickLitener;
    }


}
