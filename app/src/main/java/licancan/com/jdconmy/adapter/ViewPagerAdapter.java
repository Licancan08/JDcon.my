package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.entity.ListBean1;

/**
 * Created by robot on 2017/10/10.
 */

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    List<List<ListBean1>> listAd;

    public ViewPagerAdapter(Context context, List<List<ListBean1>> list) {
        this.context = context;
        this.listAd = list;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context, R.layout.recycler,null);
        RecyclerView re = view.findViewById(R.id.rv_recycler_grid);
        RecyclerViewAdapter rv = new RecyclerViewAdapter(context,listAd.get(position));
        GridLayoutManager gm = new GridLayoutManager(context,5);
        re.setLayoutManager(gm);
        re.setAdapter(rv);
        container.addView(view);

        return view;
    }

}
