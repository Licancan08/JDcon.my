package licancan.com.jdconmy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import licancan.com.jdconmy.DetailActivity;
import licancan.com.jdconmy.R;
import licancan.com.jdconmy.SearchBigActivity;
import licancan.com.jdconmy.adapter.KillAdapter;
import licancan.com.jdconmy.adapter.RecyclerListAdapter;
import licancan.com.jdconmy.Search1Activity;
import licancan.com.jdconmy.adapter.SearchBigAdapter;
import licancan.com.jdconmy.adapter.ViewPagerAdapter;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.GridBean;
import licancan.com.jdconmy.entity.HeadBean;
import licancan.com.jdconmy.entity.ListBean1;
import licancan.com.jdconmy.entity.ListShopBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

/**
 * Created by robot on 2017/9/29.
 */

public class Fragment1 extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View view;
    private XBanner xbanner;
    private List<String> imgesUrl;
    private List<HeadBean.DataBean> Headlist;
    private List<GridBean.DataBean> Gridlist;
    private ViewPagerAdapter vp1;
    private KillAdapter killAdapter;
    private List<HeadBean.MiaoshaBean.ListBeanX> killlist;
    private List<ListShopBean.TuijianBean.ListBean> listBean;
    private RecyclerListAdapter listadapter;

    //秒杀倒计时
    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private long mHour = 02;
    private long mMin = 15;
    private long mSecond = 36;
    private boolean isRun = true;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                xbanner.setData(imgesUrl,null);
                xbanner.setPoinstPosition(XBanner.CENTER);
                xbanner.setmAdapter(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, View view, int position) {
                        Glide.with(getActivity()).load(imgesUrl.get(position)).into((ImageView) view);
                    }
                });
            }else if(msg.what==2){
                vp1 = new ViewPagerAdapter(getActivity(),list);
                vp.setAdapter(vp1);
            }else if(msg.what==3){
                killAdapter = new KillAdapter(getActivity(),killlist);
                kill_recyler.setAdapter(killAdapter);
                killAdapter.setOnItemClickLitener(new KillAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(getActivity(),DetailActivity.class);
                        intent.putExtra("pid",killlist.get(position).getPid());
                        startActivity(intent);
                    }
                });
            }
            else if(msg.what==4)
            {
                //关联适配器
                listadapter = new RecyclerListAdapter(getActivity(), listBean);
                recyler_list.setAdapter(listadapter);
                listadapter.setOnItemClickLitener(new RecyclerListAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent=new Intent(getActivity(),DetailActivity.class);
                        intent.putExtra("pid",listBean.get(position).getPid());
                        startActivity(intent);
                    }
                });

            }else if (msg.what==5) {
                computeTime();
                if (mHour<10){
                    tvHour.setText("0"+mHour+"");
                }else {
                    tvHour.setText("0"+mHour+"");
                }
                if (mMin<10){
                    tvMinute.setText("0"+mMin+"");
                }else {
                    tvMinute.setText(mMin+"");
                }
                if (mSecond<10){
                    tvSecond.setText("0"+mSecond+"");
                }else {
                    tvSecond.setText(mSecond+"");
                }
            }
        }
    };
    private ImageView head_sys;
    private RecyclerView recyler_list;
    private LinearLayoutManager layoutManager;
    private List<ListBean1> grid1;
    private List<ListBean1> grid;
    private ViewPager vp;
    private LinearLayout dot_ll;
    //定义存放原点图片的集合
    List<ImageView> dot_list=new ArrayList<ImageView>();
    private ViewPager viewPager;
    private RecyclerView kill_recyler;
    private LinearLayoutManager kill;
    private List<List<ListBean1>> list;
    private EditText et_sou;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view==null)
        {
            view =inflater.inflate(R.layout.fragment1,container,false);
        }
        
        vp=view.findViewById(R.id.viewPager);
        grid1 = new ArrayList<ListBean1>();
        grid = new ArrayList<ListBean1>();
        initView();
        initData();
        initGrid();
        initKill();
        initList();
        initDot();
        startRun();
        return view;
    }

    /**
     * 小圆点
     */
    private void initDot() {
        for (int i = 0; i <2 ; i++) {
            ImageView iv=new ImageView(getActivity());
            if(i==0)
            {
                iv.setImageResource(R.drawable.black);
            }
            else{
                iv.setImageResource(R.drawable.gray);
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(10,10);
            params.setMargins(10,5,10,5);
            dot_ll.addView(iv,params);
            dot_list.add(iv);

        }
    }

    private void initView() {
        dot_ll = view.findViewById(R.id.dot_ll);
        xbanner = view.findViewById(R.id.xbanner);
        head_sys = view.findViewById(R.id.head_sys);
        head_sys.setOnClickListener(this);
        //RecylerView控件
        recyler_list = view.findViewById(R.id.recyler_list);
//        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager=new GridLayoutManager(getActivity(),2);
        recyler_list.setLayoutManager(layoutManager);
        recyler_list.setHasFixedSize(true);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(this);

        kill_recyler = view.findViewById(R.id.kill_recyler);
        kill = new LinearLayoutManager(getActivity());
        kill.setOrientation(LinearLayoutManager.HORIZONTAL);
        kill_recyler.setLayoutManager(kill);

        tvHour=view.findViewById(R.id.tv_hour);
        tvMinute=view.findViewById(R.id.tv_minute);
        tvSecond=view.findViewById(R.id.tv_second);

        et_sou = view.findViewById(R.id.et_sou);
        et_sou.setOnClickListener(this);
    }

    /**
     * 请求九宫格内容
     */
    private void initGrid() {
        OkHttp3Utils.doPost(Api.SHOPPING_API, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson=new Gson();
                GridBean bean = gson.fromJson(result, GridBean.class);
                Gridlist = bean.getData();
                System.out.println("==========商品分类==============");
                for (GridBean.DataBean GridBean : Gridlist) {
                    System.out.println(GridBean.getIcon()+" "+GridBean.getName()+" "+GridBean.getCid());
                    int cid=GridBean.getCid();
                    if(cid<=11)
                    {
                        grid.add(new ListBean1(GridBean.getIcon(),GridBean.getName()));
                    } else
                    {
                        grid1.add(new ListBean1(GridBean.getIcon(),GridBean.getName()));
                    }
                }
                for (ListBean1 li : grid) {
                    System.out.println("li.getTv() = " + li.getTv());
                }
                list = new ArrayList<List<ListBean1>>();
                list.add(grid);
                list.add(grid1);
                handler.sendEmptyMessage(2);
            }
        });
    }

    /**
     * 请求图片数据
     */
    private void initData() {
        OkHttp3Utils.doPost(Api.SHOUYE_API, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson=new Gson();
                HeadBean bean = gson.fromJson(result, HeadBean.class);
                Headlist = bean.getData();
                imgesUrl = new ArrayList<>();
                if(Headlist!=null)
                {
                    for (HeadBean.DataBean HeadBean : Headlist) {
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
     * 秒杀数据
     */
    private void initKill() {
        OkHttp3Utils.doPost(Api.SHOUYE_API, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                HeadBean bean = gson.fromJson(result, HeadBean.class);
                killlist = bean.getMiaosha().getList();
                if(killlist!=null){
                    handler.sendEmptyMessage(3);
                }

            }
        });
    }


    /**
     * 请求List分类的数据
     */
    private void initList() {
        OkHttp3Utils.doPost(Api.SHOUYE_API, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result = response.body().string();
                Gson gson=new Gson();
                ListShopBean bean = gson.fromJson(result, ListShopBean.class);
                listBean = bean.getTuijian().getList();
                if(listBean!=null)
                {
                    handler.sendEmptyMessage(4);
                }


            }
        });
    }





    /**
     * 扫一扫
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.head_sys:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.et_sou:
                Intent intent1=new Intent(getActivity(), Search1Activity.class);
                startActivity(intent1);
                break;
        }

    }

    /**
     * 扫一扫
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i <dot_list.size() ; i++) {
            if(i==position)
            {
                dot_list.get(i).setImageResource(R.drawable.black);
            }
            else{
                dot_list.get(i).setImageResource(R.drawable.gray);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 秒杀开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 5;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
            }
        }
    }
}
