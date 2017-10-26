package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.entity.GridBean;
import licancan.com.jdconmy.entity.HeadBean;
import licancan.com.jdconmy.fragment.Fragment2;
import okhttp3.Callback;

/**
 * Created by robot on 2017/10/10.
 */

public class ListAdapter extends BaseAdapter {
    public static int mPosition;
    Context context;
    List<GridBean.DataBean> list;
    public ListAdapter(Context context, List<GridBean.DataBean> list) {
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1=View.inflate(context,R.layout.list_item,null);
        mPosition=i;
        TextView tv_type=view1.findViewById(R.id.tv_type);
        tv_type.setText(list.get(i).getName());
        if(i== Fragment2.mPosition)
        {
            tv_type.setTextColor(Color.RED);
            view1.setBackgroundColor(Color.parseColor("#f3f5f7"));
        }else{
            tv_type.setTextColor(Color.parseColor("#000000"));
            view1.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return view1;
    }
}
