package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.SearchBigActivity;
import licancan.com.jdconmy.entity.ShopBean;

/**
 * Created by robot on 2017/10/11.
 */

public class ShopRecylerAdapter extends RecyclerView.Adapter<ShopRecylerAdapter.ViewHolder>{
    Context context;
    List<ShopBean.DataBean.ListBean> list;
    private OnItemClickLitener mOnItemClickLitener;
    public ShopRecylerAdapter(Context context, List<ShopBean.DataBean.ListBean> list) {
        this.context=context;
        this.list=list;
    }

    /**
     * 自定义接口
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view,int position);
        void onItemLongClick(View v,int position);
    }

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.small_shop,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getIcon()).into(holder.iv_img);
        holder.tv_name.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //得到商品的pscid
                String pscid = list.get(position).getPscid()+"";
                //跳转到搜索页面
                Intent intent=new Intent(context, SearchBigActivity.class);
                intent.putExtra("pscid",pscid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final ImageView iv_img;
        public final TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_shop);
            tv_name = itemView.findViewById(R.id.tv_shop);
        }
    }


}
