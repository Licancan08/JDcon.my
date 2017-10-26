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

public class Search2Activity extends AppCompatActivity implements View.OnClickListener {

    private int type=0;
    private EditText search_name;
    private String name;
    private RecyclerView search_recycler;
    private SearchBigAdapter adapter;
    private List<SearchBigBean.DataBean> list;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                adapter = new SearchBigAdapter(Search2Activity.this,list);
                LinearLayoutManager layoutManager=new LinearLayoutManager(Search2Activity.this);
                search_recycler.setLayoutManager(layoutManager);
                search_recycler.setAdapter(adapter);
                adapter.setOnItemClickLitener(new SearchBigAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(Search2Activity.this,DetailActivity.class);
                        int pid = list.get(position).getPid();
                        intent.putExtra("pid",pid);
                        System.out.println("pid======="+pid);
                        startActivity(intent);
                    }
                });
            }
        }
    };
    private ImageView change_type;
    private SearchBigAdapter2 adapter2;
    private ImageView search_back;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        Intent intent=getIntent();
        name = intent.getStringExtra("name");
        initView();
    }

    private void initView() {
        search_recycler = (RecyclerView) findViewById(R.id.search_recycler);
        search_name = (EditText) findViewById(R.id.search_name);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getData(name);
                        Toast.makeText(Search2Activity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        search_name.setText(name);
        getData(name);

        change_type = (ImageView) findViewById(R.id.change_type);
        change_type.setOnClickListener(this);

        search_back = (ImageView) findViewById(R.id.search_back);
        search_back.setOnClickListener(this);
    }

    /**
     * 请求数据
     * @param keywords
     */
    public void getData(String keywords) {
        Map<String,String> map=new HashMap<>();
        map.put("keywords",keywords);
        OkHttp3Utils.doPost(Api.GUANJIAN_API, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("Search2result==========="+result);
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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.change_type:
                type++;
                if(type%2==0)
                {
                    change_type.setImageResource(R.drawable.grid);
                    adapter = new SearchBigAdapter(Search2Activity.this,list);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(Search2Activity.this);
                    search_recycler.setLayoutManager(layoutManager);
                    search_recycler.setAdapter(adapter);
                    adapter.setOnItemClickLitener(new SearchBigAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(Search2Activity.this,DetailActivity.class);
                            int pid = list.get(position).getPid();
                            intent.putExtra("pid",pid);
                            System.out.println("pid======="+pid);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    change_type.setImageResource(R.drawable.list);
                    adapter2 = new SearchBigAdapter2(this,list);
                    GridLayoutManager layoutManager2=new GridLayoutManager(this,2);
                    search_recycler.setLayoutManager(layoutManager2);
                    search_recycler.setAdapter(adapter2);
                    adapter2.setOnItemClickLitener(new SearchBigAdapter2.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(Search2Activity.this,DetailActivity.class);
                            intent.putExtra("pid",list.get(position).getPid());
                            startActivity(intent);
                        }
                    });
                }
                break;
            case R.id.search_back:
                finish();
                break;
            //按价格排序
            case R.id.but_price2:
                break;

        }
    }
}
