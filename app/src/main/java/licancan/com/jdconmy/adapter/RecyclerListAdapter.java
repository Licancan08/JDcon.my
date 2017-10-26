package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.entity.ListShopBean;

/**
 * Created by robot on 2017/10/10.
 */

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>{

    private OnItemClickLitener mOnItemClickLitener;
    Context context;
    List<ListShopBean.TuijianBean.ListBean> list;

    public RecyclerListAdapter(Context context, List<ListShopBean.TuijianBean.ListBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String imgUrl = list.get(position).getImages().split("\\|")[0];
        Glide.with(context).load(imgUrl).into(holder.iv_list_img);
        holder.tv_list_des.setText(list.get(position).getTitle());
        holder.tv_list_price.setText("¥"+list.get(position).getPrice()+"");
        holder.tv_list_price.setTextColor(Color.RED);

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

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_list_des,tv_list_price;
        public ImageView iv_list_img;
        public ViewHolder(View view){
            super(view);
            iv_list_img=view.findViewById(R.id.iv_list_img);
            tv_list_des=view.findViewById(R.id.tv_list_des);
            tv_list_price=view.findViewById(R.id.tv_list_price);
        }
    }

    //自定义接口  recyclerView的条目点击事件
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickLitener(OnItemClickLitener itemClickLitener)
    {
        this.mOnItemClickLitener = itemClickLitener;
    }
}
