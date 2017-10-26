package licancan.com.jdconmy;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import licancan.com.jdconmy.adapter.OrderAdapter;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.OrderBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView order_recycler;
    private List<OrderBean.DataBean> list;
    private OrderAdapter adapter;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                adapter = new OrderAdapter(OrderActivity.this, list);
                LinearLayoutManager layoutManager=new LinearLayoutManager(OrderActivity.this);
                order_recycler.setLayoutManager(layoutManager);
                order_recycler.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initData();
    }

    private void initView() {
        order_recycler = (RecyclerView) findViewById(R.id.order_recycler);
    }

    /**
     * 请求订单数据
     */
    private void initData() {
        SharedPreferences sp=getSharedPreferences("config",MODE_PRIVATE);
        String uid = sp.getString("uid", "148");
        Map<String,String> map=new HashMap<>();
        map.put("uid",uid);
        OkHttp3Utils.doPost(Api.GETORDER_PAI, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("查询订单======="+result);
                getJson(result);
            }
        });
    }

    /**
     * 解析json
     * @param result
     */
    private void getJson(String result) {
        Gson gson=new Gson();
        OrderBean orderBean = gson.fromJson(result, OrderBean.class);
        list = orderBean.getData();
        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }
}
