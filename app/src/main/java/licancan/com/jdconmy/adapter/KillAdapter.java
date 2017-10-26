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
import licancan.com.jdconmy.entity.HeadBean;

/**
 * Created by robot on 2017/10/11.
 */

public class KillAdapter extends RecyclerView.Adapter<KillAdapter.ViewHolder>{

    private OnItemClickLitener mOnItemClickLitener;
    Context context;
    List<HeadBean.MiaoshaBean.ListBeanX> list;

    public KillAdapter(Context context, List<HeadBean.MiaoshaBean.ListBeanX> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kill_recyler_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String imgUrl = list.get(position).getImages().split("\\|")[0];
        Glide.with(context).load(imgUrl).into(holder.kill_img);
        holder.kill_price.setText("¥"+list.get(position).getPrice()+"");
        holder.kill_price.setTextColor(Color.RED);

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

        public ImageView kill_img;
        public TextView kill_price;
        public ViewHolder(View view) {
                super(view);
            kill_img = view.findViewById(R.id.kill_img);
            kill_price = view.findViewById(R.id.kill_price);
        }
    }

    //自定义接口  recyclerView的条目点击事件
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickLitener(KillAdapter.OnItemClickLitener itemClickLitener)
    {
        this.mOnItemClickLitener = itemClickLitener;
    }
}
