package licancan.com.jdconmy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
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

public class Detail_F2 extends Fragment {

    private View view;
    private int pid;
    private String url;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.detail_f2,null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        Intent in=getActivity().getIntent();
        pid = in.getIntExtra("pid", 0);
        webView = view.findViewById(R.id.webView);

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
        DetailBean.DataBean data = detailBean.getData();
        url = data.getDetailUrl();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
            }
        });

    }
}
