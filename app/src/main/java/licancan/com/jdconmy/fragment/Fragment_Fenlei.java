package licancan.com.jdconmy.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.adapter.ShopBigAdapter;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.ShopBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

/**
 * Created by robot on 2017/10/11.
 */


public class Fragment_Fenlei extends Fragment {

    private RecyclerView shop_recy;
    private List<ShopBean.DataBean> list;
    private int cid;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                ShopBigAdapter bigAdapter=new ShopBigAdapter(getActivity(),list);
                System.out.println("xxx"+shop_recy);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                shop_recy.setLayoutManager(linearLayoutManager);
                shop_recy.setAdapter(bigAdapter);
            }
        }
    };
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.shop_recycler,null);
        initView();
        //请求大的Recycler  包括TextView和Recycler
        initBigShop();
        return view;

    }

    private void initView() {
        shop_recy = view.findViewById(R.id.shop_recy);
    }

    /**
     * 请求大的Recycler  包括TextView和Recycler
     */
    private void initBigShop() {
        OkHttp3Utils.doPost(Api.SHOP_API+cid, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = response.body().string();
                Gson gson=new Gson();
                ShopBean shopBean = gson.fromJson(result, ShopBean.class);
                list = shopBean.getData();
                for (ShopBean.DataBean dataBean : list) {
                    System.out.println("ss===="+dataBean.getName());
                }
                handler.sendEmptyMessage(1);

            }
        });
    }
    public void getCid(int position)
    {
        cid=position;
    }
}
