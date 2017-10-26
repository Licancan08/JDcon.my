package licancan.com.jdconmy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import licancan.com.jdconmy.adapter.SearchBigAdapter;
import licancan.com.jdconmy.adapter.SearchBigAdapter2;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.SearchBigBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

public class SearchBigActivity extends AppCompatActivity implements View.OnClickListener {

    boolean a=false;
    boolean type=false;
    private int num=0;
    private ImageView search_back;
    private List<SearchBigBean.DataBean> list;
    private List<SearchBigBean.DataBean> list2;
    private SearchBigAdapter adapter;
    private SearchBigAdapter2 adapter2;
    private RecyclerView search_recycler;
    private EditText search_name;
    private ImageView change_type;
    private Button but_search;
    private Button but_sum;
    private Button but_num;
    private Button but_price;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                    if(type==false)
                    {
                        adapter = new SearchBigAdapter(SearchBigActivity.this,list);
                    }else{
                        adapter = new SearchBigAdapter(SearchBigActivity.this,list2);
                    }
                LinearLayoutManager layoutManager=new LinearLayoutManager(SearchBigActivity.this);
                search_recycler.setLayoutManager(layoutManager);
                search_recycler.setAdapter(adapter);
                adapter.setOnItemClickLitener(new SearchBigAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(a==true&&list2!=null&&list2.size()>0)
                        {
                            Intent intent=new Intent(SearchBigActivity.this,DetailActivity.class);
                            intent.putExtra("pid",list2.get(position).getPid());
                            startActivity(intent);
                        }else{
                            Intent intent=new Intent(SearchBigActivity.this,DetailActivity.class);
                            intent.putExtra("pid",list.get(position).getPid());
                            startActivity(intent);
                        }

                    }
                });

            }
            else if(msg.what==2)
            {

                if(type==false)
                {
                    adapter2 = new SearchBigAdapter2(SearchBigActivity.this,list);
                }else{
                    adapter2 = new SearchBigAdapter2(SearchBigActivity.this,list2);
                }

                GridLayoutManager layoutManager2=new GridLayoutManager(SearchBigActivity.this,2);
                search_recycler.setLayoutManager(layoutManager2);
                search_recycler.setAdapter(adapter2);
                adapter2.setOnItemClickLitener(new SearchBigAdapter2.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(a==true&&list2!=null&&list2.size()>0)
                        {
                            Intent intent=new Intent(SearchBigActivity.this,DetailActivity.class);
                            intent.putExtra("pid",list2.get(position).getPid());
                            startActivity(intent);
                        }else{
                            Intent intent=new Intent(SearchBigActivity.this,DetailActivity.class);
                            intent.putExtra("pid",list.get(position).getPid());
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    };
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_big);
        initView();
        initData();
    }

    private void initData() {
        Intent in=getIntent();
        String pscid = in.getStringExtra("pscid");
        Map<String,String> map=new HashMap<>();
        map.put("pscid",pscid);
        OkHttp3Utils.doPost(Api.ZIFENLEI_API, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("result======="+result);
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
        SearchBigBean searchBigBean = gson.fromJson(result, SearchBigBean.class);
        list = searchBigBean.getData();
        if(list!=null)
        {
            handler.sendEmptyMessage(1);
        }
    }

    /**
     * 解析json
     * @param result
     */
    private void getJson2(String result) {
        Gson gson=new Gson();
        SearchBigBean searchBigBean = gson.fromJson(result, SearchBigBean.class);
        list2 = searchBigBean.getData();
        handler.sendEmptyMessage(1);
    }


    private void initView() {
        search_back = (ImageView) findViewById(R.id.search_back);
        search_back.setOnClickListener(this);

        search_recycler = (RecyclerView) findViewById(R.id.search_recycler);
        search_name = (EditText) findViewById(R.id.search_name);
        but_search = (Button) findViewById(R.id.but_search);
        but_search.setOnClickListener(this);
        change_type = (ImageView) findViewById(R.id.change_type);
        change_type.setOnClickListener(this);
        //排序  按照综合 销量  价格
        but_sum = (Button) findViewById(R.id.but_sum);
        but_num = (Button) findViewById(R.id.but_num);
        but_price = (Button) findViewById(R.id.but_price);
        but_sum.setOnClickListener(this);
        but_num.setOnClickListener(this);
        but_price.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        Toast.makeText(SearchBigActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.search_back:
                finish();
                break;
            //搜索按钮
            case R.id.but_search:
                a=true;
                String name = search_name.getText().toString();
                Map<String,String> map=new HashMap<>();
                map.put("keywords",name);
                OkHttp3Utils.doPost(Api.GUANJIAN_API, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        System.out.println("Findresult==========="+result);
                        type=true;
                        getJson2(result);
                    }
                });
                break;
            //切换布局
            case R.id.change_type:
                num++;
                if(num%2==0)
                {
                    change_type.setImageResource(R.drawable.grid);
                    LinearLayoutManager layoutManager1=new LinearLayoutManager(SearchBigActivity.this);
                    search_recycler.setLayoutManager(layoutManager1);
                    search_recycler.setAdapter(adapter);


                }else{
                    change_type.setImageResource(R.drawable.list);
                    handler.sendEmptyMessage(2);
                }
                break;
            case R.id.but_sum:
                break;
            case R.id.but_num:
                break;
            //按价格排序
            case R.id.but_price:
                break;
        }
    }
}
