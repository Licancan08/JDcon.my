package licancan.com.jdconmy;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.DetailBean;
import licancan.com.jdconmy.fragment.Detail_F1;
import licancan.com.jdconmy.fragment.Detail_F2;
import licancan.com.jdconmy.fragment.Detail_F3;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager vp;
    private List<String> tabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();

    }
    private void initView() {
        ImageView back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        vp = (ViewPager) findViewById(R.id.vp);

        tabList = new ArrayList<>();
        tabList.add("商品");
        tabList.add("详情");
        tabList.add("评价");

        vp.setAdapter(new DtailAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(vp);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }

    class DtailAdapter extends FragmentPagerAdapter{

        public DtailAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position)
            {
                case 0:
                    fragment=new Detail_F1();
                    break;
                case 1:
                    fragment=new Detail_F2();
                    break;
                case 2:
                    fragment=new Detail_F3();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }
    }
}
