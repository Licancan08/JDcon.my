package licancan.com.jdconmy.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.DetailBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

/**
 * Created by robot on 2017/10/18.
 */

public class Detail_F1 extends Fragment implements XBanner.XBannerAdapter, View.OnClickListener {

    private int pid;
    private TextView desc_content;
    private TextView desc_content2;
    private TextView desc_money;
    private TextView desc_oldmoney;
    private View view;
    private DetailBean.DataBean data;
    private XBanner xbanner;
    private List<String> imgesUrl;
    private String imgs_split;
    private Button add_goodcar;

    Handler handler=new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                desc_content.setText(data.getTitle());
                desc_content2.setText(data.getSubhead());
                desc_money.setText(data.getPrice()+"");//原价
                desc_oldmoney.setText(data.getBargainPrice()+"");//现价
                imgs_split = data.getImages();
                if(imgs_split!=null)
                {
                    String[] imgs=imgs_split.split("\\|");
                    System.out.println("img=========="+ imgs_split);
                    imgesUrl = new ArrayList<>();
                    for (String img : imgs) {
                        imgesUrl.add(img);
                    }
                    xbanner.setData(imgesUrl,null);
                    xbanner.setPoinstPosition(XBanner.CENTER);
                    xbanner.setmAdapter(new XBanner.XBannerAdapter() {
                        @Override
                        public void loadBanner(XBanner banner, View view, int position) {
                            Glide.with(getActivity()).load(imgesUrl.get(position)).into((ImageView) view);
                        }
                    });
                }

            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.detail_f1,null);
        initView();
        initData();
        return view;
    }

    @Override
    public void loadBanner(XBanner banner, View view, int position) {
        Glide.with(getActivity()).load(imgesUrl.get(position)).into((ImageView) view);
    }

    private void initView() {
        Intent in=getActivity().getIntent();
        pid = in.getIntExtra("pid", 0);

        xbanner = view.findViewById(R.id.xbanner);
        add_goodcar = view.findViewById(R.id.add_goodcar);
        add_goodcar.setOnClickListener(this);
        desc_content = (TextView) view.findViewById(R.id.desc_content);
        desc_content2 = (TextView) view.findViewById(R.id.desc_content2);
        desc_money = (TextView) view.findViewById(R.id.desc_money);
        desc_oldmoney = (TextView) view.findViewById(R.id.desc_oldmoney);
    }
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("pid",pid+"");
        OkHttp3Utils.doPost(Api.DETAL_API, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("result=========="+result);
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
        DetailBean detailBean = gson.fromJson(result, DetailBean.class);
        data = detailBean.getData();
        handler.sendEmptyMessage(1);
    }

    /**
     * 加入购物车
     * @param view
     */
    @Override
    public void onClick(View view) {
        int pid = data.getPid();
        SharedPreferences sp=getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        String uid = sp.getString("uid", 0 + "");
        Map<String,String> map=new HashMap<>();
        map.put("pid",pid+"");
        map.put("uid",uid+"");
        OkHttp3Utils.doPost(Api.ADDCART_API, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("ShoppingCar========="+response);
                        Toast.makeText(getActivity(), "加入购物车成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
