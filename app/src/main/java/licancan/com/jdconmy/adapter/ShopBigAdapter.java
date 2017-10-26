package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.entity.ShopBean;

/**
 * Created by robot on 2017/10/12.
 */

public class ShopBigAdapter extends RecyclerView.Adapter<ShopBigAdapter.ViewHolder>{

    Context context;
    List<ShopBean.DataBean> list;
    public ShopBigAdapter(Context context, List<ShopBean.DataBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.small_shop_item,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.shop_type.setText(list.get(position).getName());
        List<ShopBean.DataBean.ListBean> smallList = this.list.get(position).getList();
        ShopRecylerAdapter smallAdapter=new ShopRecylerAdapter(context,smallList);

        holder.small_recycler.setLayoutManager(new GridLayoutManager(context,3));
        holder.small_recycler.setAdapter(smallAdapter);

        smallAdapter.setmOnItemClickLitener(new ShopRecylerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(context, position+"click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView shop_type;
        public RecyclerView small_recycler;

        public ViewHolder(View itemView) {
            super(itemView);
            shop_type = itemView.findViewById(R.id.shop_type);
            small_recycler = itemView.findViewById(R.id.small_recycler);
        }
    }
}
