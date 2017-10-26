package licancan.com.jdconmy.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.adapter.CartAdapter;
import licancan.com.jdconmy.app.TopBar;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.CarEntity;
import licancan.com.jdconmy.entity.ChildBean;
import licancan.com.jdconmy.entity.ParentBean;
import licancan.com.jdconmy.view.ICartView;
import licancan.com.jdconmy.zhifubao.PayDemoActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;
import xp.code.okhttp3.utils.callback.GsonObjCallback;

import static android.content.Context.MODE_PRIVATE;


public class GouwucheFragment extends Fragment implements View.OnClickListener,ICartView {
    private View view;
    private TopBar mTopbar;

    private ExpandableListView expandableListView;

    private CartAdapter adapter;

    private List<ParentBean> parentList;
    private List<List<ChildBean>> childList;

    private CheckBox gouwuche_footer_check;
    private TextView gouwuche_footer_jiesuan;
    private TextView gouwuche_footer_price;
    private TextView gouwuche_footer_heji;

    private TextView gouwuche_tv;

    private SharedPreferences sp;

    private int sum=0;//总价
    private String uid;
    private CarEntity.DataBean.ListBean listBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gouwuche, container, false);
        initView(view);
        initData();
        createEvent();
        return view;
    }

    private void initData() {
        sp = getActivity().getSharedPreferences("config",MODE_PRIVATE);
        if(sp.getBoolean("login",false))
        {
            gouwuche_tv.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            //调用P层获取数据
            uid = sp.getString("uid","");
            System.out.println("view ========== " + uid);
            HashMap<String,String> params=new HashMap<>();
            params.put("uid", uid);
            OkHttp3Utils.doPost(Api.GETCARTS_API, params, new GsonObjCallback<CarEntity>() {
                @Override
                public void onFail(Call call, IOException e) {
                }
                @Override
                public void onSucc(CarEntity carEntity) {
                    parentList = new ArrayList<>();
                    childList = new ArrayList<>();

                    //获取数据
                    List<CarEntity.DataBean> dataBeen = carEntity.getData();
                    for (int i=0;i<dataBeen.size();i++)
                    {
                        ParentBean parentBean=new ParentBean(dataBeen.get(i).getSellerName(),false,true);
                        parentList.add(parentBean);

                        List<ChildBean> childBeen=new ArrayList<ChildBean>();
                        List<CarEntity.DataBean.ListBean> listBeen = dataBeen.get(i).getList();
                        for(int j=0;j<listBeen.size();j++)
                        {
                            listBean = listBeen.get(j);
                            ChildBean bean=new ChildBean(sp.getString("uid",""), listBean.getPid()+"", listBean.getTitle(), listBean.getNum(),false,(int) listBean.getPrice(),true
                                    , listBean.getImages().split("\\|")[0]);
                            childBeen.add(bean);
                        }
                        childList.add(childBeen);
                    }

                    adapter = new CartAdapter(getContext(), parentList, childList,GouwucheFragment.this);
                    expandableListView.setAdapter(adapter);
                    //去掉二级列表的默认符号
                    expandableListView.setGroupIndicator(null);
                    //默认打开全部的子列表
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        expandableListView.expandGroup(i);
                    }
                }
            });
        }
        else
        {
            //显示购物车为空
            gouwuche_tv.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
        }
    }

    private void initView(View view) {
        mTopbar = (TopBar) view.findViewById(R.id.gouwuche_topbar);
        expandableListView = (ExpandableListView) view.findViewById(R.id.gouwuche_expanded);
        gouwuche_footer_check = (CheckBox) view.findViewById(R.id.gouwuche_footer_check);
        gouwuche_footer_jiesuan = (TextView) view.findViewById(R.id.gouwuche_footer_jiesuan);
        gouwuche_footer_jiesuan.setOnClickListener(this);
        gouwuche_footer_price = (TextView) view.findViewById(R.id.gouwuche_footer_price);
        gouwuche_footer_heji = (TextView) view.findViewById(R.id.gouwuche_footer_heji);
        gouwuche_tv= (TextView) view.findViewById(R.id.gouwuche_tv);
    }

    private void createEvent() {
        //设置选中监听去实现全选
        gouwuche_footer_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                {
                    adapter.allCheck(true);
                }
            }
        });
        //设置点击监听去实现反选
        gouwuche_footer_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取购物车的CheckBox的选中状态
                boolean isCheck=gouwuche_footer_check.isChecked();
                if(!isCheck)
                {
                    adapter.allCheck(false);
                }
            }
        });
    }
    /**
     * 支付结算
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gouwuche_footer_jiesuan:
                //创建订单
                Map<String,String> map=new HashMap<>();
                map.put("uid",uid);
                map.put("price",gouwuche_footer_price.getText().toString());
                OkHttp3Utils.doPost(Api.CREATE_API, map, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        System.out.println("创建订单=========="+result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "订单创建成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
        }
    }

    //修改全选按钮的状态
    @Override
    public void changeCheckBtn(boolean flag) {
        gouwuche_footer_check.setChecked(flag);
    }

    //计算总价的方法
    @Override
    public void addPrice() {
        //初始化总价
        sum=0;
        //遍历所有的子集合
        for(int i=0;i<adapter.getGroupCount();i++)
        {
            for (int j=0;j<adapter.getChildrenCount(i);j++)
            {
                ChildBean child=adapter.getChild(i,j);
                //如果该对象被选中,则加上这个对象中的价钱
                if(child.isCheck)
                {
                    sum+=child.price*child.saleNum;
                }
            }
        }
        //得到总价,更新UI控件
        gouwuche_footer_price.setText(sum+""+".00");
    }

    @Override
    public void delete(String uid,String pid) {
        //删除的接口回调
        HashMap<String,String> params=new HashMap<>();
        params.put("uid",uid);
        params.put("pid",pid);
        OkHttp3Utils.doPost(Api.DELETE_SHOPCAR, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json=response.body().string();
                parentList.clear();
                childList.clear();
                initData();
            }
        });
    }
}