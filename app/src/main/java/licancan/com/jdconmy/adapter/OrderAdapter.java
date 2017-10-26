package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import licancan.com.jdconmy.OrderActivity;
import licancan.com.jdconmy.R;
import licancan.com.jdconmy.entity.OrderBean;
import licancan.com.jdconmy.zhifubao.PayDemoActivity;

/**
 * Created by robot on 2017/10/22.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    Context context;
    List<OrderBean.DataBean> list;
    public OrderAdapter(Context context, List<OrderBean.DataBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context,R.layout.order_layout,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String price = list.get(position).getPrice() + "";
        System.out.println("==============price"+price);
        holder.order_price.setText("¥"+list.get(position).getPrice()+"");
        holder.order_price.setTextColor(Color.RED);
        holder.order_date.setText(list.get(position).getCreatetime());
        holder.order_pid.setText("商品编号:"+list.get(position).getOrderid());
        int status = list.get(position).getStatus();

        if(status == 0){
            holder.but_order.setText("待支付");
        }else if(status == 1){
            holder.but_order.setText("已支付");
        }else{
            holder.but_order.setText("取消");
        }

        holder.but_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PayDemoActivity.class);
                intent.putExtra("status",list.get(position).getStatus());
                intent.putExtra("orderId",list.get(position).getOrderid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView order_price,order_date,order_pid;
        public Button but_order;

        public ViewHolder(View itemView) {
            super(itemView);
            order_price = itemView.findViewById(R.id.order_price);
            order_date = itemView.findViewById(R.id.order_date);
            order_pid = itemView.findViewById(R.id.order_pid);
            but_order = itemView.findViewById(R.id.but_order);
        }
    }
}
