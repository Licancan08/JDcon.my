package licancan.com.jdconmy.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.adapter.ListAdapter;
import licancan.com.jdconmy.adapter.ShopRecylerAdapter;
import licancan.com.jdconmy.adapter.ViewPagerAdapter;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.GridBean;
import licancan.com.jdconmy.entity.HeadBean;
import licancan.com.jdconmy.entity.ListBean1;
import licancan.com.jdconmy.entity.ShopBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

/**
 * Created by robot on 2017/9/29.
 */

public class Fragment2 extends Fragment implements AdapterView.OnItemClickListener {

    public static int totalHight=0;
    public static int mPosition;
    private View view;
    private ListView lv_list;
    private XBanner small_xbanner;
    private FrameLayout small_fl_layout;
    private ListAdapter adapter;
    private List<String> imgesUrl;
    private Fragment_Fenlei fenlei;
    private List<GridBean.DataBean> list1;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                small_xbanner.setData(imgesUrl,null);
                small_xbanner.setPoinstPosition(XBanner.CENTER);
                small_xbanner.setmAdapter(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, View view, int position) {
                        Glide.with(getActivity()).load(imgesUrl.get(position)).into((ImageView) view);
                    }
                });
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment2,null);
        initView();
        initData();
        initHead();
        return view;
    }


    private void initView() {
        lv_list = view.findViewById(R.id.lv_list);
        lv_list.setOnItemClickListener(this);
        small_xbanner = view.findViewById(R.id.small_xbanner);
        //FrameLayout替换Fragment
        small_fl_layout = view.findViewById(R.id.small_fl_layout);


    }

    /**
     * List商品分类
     */
    private void initData() {
        OkHttp3Utils.doPost(Api.SHOPPING_API, null, new Callback() {
            private List<GridBean.DataBean> list;

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = response.body().string();
                Gson gson=new Gson();
                GridBean bean = gson.fromJson(result, GridBean.class);
                list1 = bean.getData();

                System.out.println("==========商品分类==============");
                for (GridBean.DataBean GridBean : list1) {
                    System.out.println(GridBean.getIcon() + " " + GridBean.getName() + " " + GridBean.getCid());
                    if(list1!=null)
                    {
                        if (adapter == null) {
                            adapter=new ListAdapter(getActivity(), list1);
                            lv_list.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });
    }

    /**
     * 请求图片数据
     */
    private void initHead() {
        OkHttp3Utils.doPost(Api.SHOUYE_API, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson=new Gson();
                HeadBean bean = gson.fromJson(result, HeadBean.class);
                List<HeadBean.DataBean> HeadList = bean.getData();
                imgesUrl = new ArrayList<>();
                if(HeadList!=null)
                {
                    for (HeadBean.DataBean HeadBean : HeadList) {
                        String icon = HeadBean.getIcon();
                        imgesUrl.add(icon);
                    }
                }
                //通知handler进行修改子线程
                handler.sendEmptyMessage(1);
            }
        });
    }

    /**
     * List条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mPosition=i;
        totalHight=lv_list.getMeasuredHeight()-120;
        lv_list.smoothScrollToPositionFromTop(i,totalHight/2,50);
        adapter.notifyDataSetChanged();

        fenlei = new Fragment_Fenlei();
        fenlei.getCid(i);
        Bundle bundle=new Bundle();
        bundle.putInt("cid",list1.get(i).getCid());
        fenlei.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.small_fl_layout, fenlei).commit();


    }
}
