package licancan.com.jdconmy.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import licancan.com.jdconmy.R;
import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.entity.ChildBean;
import licancan.com.jdconmy.entity.ParentBean;
import licancan.com.jdconmy.view.ICartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

/**
 * Created by 小傻瓜 on 2017/10/20.
 */
public class CartAdapter extends BaseExpandableListAdapter {
    private Context context;

    private List<ParentBean> parentList;
    private List<List<ChildBean>> childList;

    private ICartView iCartView;

    public CartAdapter(Context context,List<ParentBean> parentList,List<List<ChildBean>> childList,ICartView iCartView)
    {
        this.context=context;
        this.parentList=parentList;
        this.childList=childList;
        this.iCartView=iCartView;
    }

    @Override
    public int getGroupCount() {
        return childList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public ParentBean getGroup(int i) {
        return parentList.get(i);
    }

    @Override
    public ChildBean getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i*998;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i*456+i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        List_1 list_1=null;
        //赋值
        final ParentBean parentBean=getGroup(i);
        if(view==null)
        {
            list_1=new List_1();
            view=View.inflate(context, R.layout.cart_expand_1,null);
            list_1.shop_name= (TextView) view.findViewById(R.id.cart_expand_1_shopname);
            list_1.checkBox= (CheckBox) view.findViewById(R.id.cart_expand_1_check);
            list_1.bianji= (TextView) view.findViewById(R.id.cart_expand_1_bianji);
            //绑定数据
            view.setTag(list_1);
            list_1.checkBox.setTag(parentBean);
        }
        else
        {
            list_1= (List_1) view.getTag();
            list_1.checkBox.setTag(parentBean);
        }
        final List_1 list_11=list_1;
        list_1.checkBox.setChecked(parentBean.isCheck);
        list_1.shop_name.setText(parentBean.title);
        //编辑按钮
        list_1.bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentBean parentBean1= (ParentBean) list_11.checkBox.getTag();
                if(parentBean1.ziCheck)
                {
                    parentBean1.ziCheck=false;
                    //去修改二级列表的boolean
                    ziChangeBianji(i,parentBean1.ziCheck);
                }
                else
                {
                    parentBean1.ziCheck=true;
                    //去修改二级列表的boolean
                    ziChangeBianji(i,parentBean1.ziCheck);
                }
                notifyDataSetChanged();
            }
        });
        //CheckBox添加选中监听器实现全选
        list_1.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ParentBean parentBean1= (ParentBean) list_11.checkBox.getTag();
                parentBean1.isCheck=b;

                parentCheck(i);
                //判断是否全选中,方法中含有接口回调
                allCheckChangeFromParent();
                //刷新适配器
                notifyDataSetChanged();
            }
        });
        //CheckBox添加点击监听器实现全选
        list_1.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck=((CheckBox)view).isChecked();
                if(!isCheck)
                {
                    shopCheck(false,childList.get(i));
                }
            }
        });
        return view;
    }
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        List_2 list_2=null;
        final ChildBean childBean=getChild(i,i1);
        if(view==null)
        {
            list_2=new List_2();
            view=View.inflate(context,R.layout.cart_expand_2,null);
            list_2.title= (TextView) view.findViewById(R.id.cart_expand_2_title);
            list_2.checkBox= (CheckBox) view.findViewById(R.id.cart_expand_2_check);
            list_2.price= (TextView) view.findViewById(R.id.cart_expand_2_price);
            list_2.image= (ImageView) view.findViewById(R.id.cart_expand_2_image);
            list_2.salenum= (TextView) view.findViewById(R.id.cart_expand_2_salenum);

            list_2.relative= (RelativeLayout) view.findViewById(R.id.cart_expand_2_relative);
            list_2.linear= (LinearLayout) view.findViewById(R.id.cart_expand_2_linear);
            list_2.num= (TextView) view.findViewById(R.id.cart_expand_2_num);
            list_2.add= (Button) view.findViewById(R.id.cart_expand_2_add);
            list_2.jian= (Button) view.findViewById(R.id.cart_expand_2_jian);
            list_2.del= (Button) view.findViewById(R.id.cart_expand_2_del_good);
            view.setTag(list_2);
            list_2.checkBox.setTag(childBean);
        }
        else
        {
            list_2= (List_2) view.getTag();
            list_2.checkBox.setTag(childBean);
        }
        final List_2 list_21=list_2;

        final ChildBean childBean1= (ChildBean) list_2.checkBox.getTag();

        if(childBean1.viewChange)
        {
            list_2.linear.setVisibility(View.GONE);
            list_2.relative.setVisibility(View.VISIBLE);
        }
        else
        {
            list_2.linear.setVisibility(View.VISIBLE);
            list_2.relative.setVisibility(View.GONE);
        }
        list_2.salenum.setText("x"+childBean1.saleNum);
        list_2.num.setText(childBean1.saleNum+"");
        list_2.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childBean1.saleNum++;
                iCartView.addPrice();
                notifyDataSetChanged();
            }
        });
        list_2.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //接口回调进行删除
                iCartView.delete(childBean.uid,childBean.pid);
            }
        });
        list_2.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childBean1.saleNum--;
                if(childBean1.saleNum>0)
                {
                    iCartView.addPrice();
                    notifyDataSetChanged();
                    return;
                }
                else if(childBean1.saleNum==0)
                {
                    Toast.makeText(context, "就剩一个了！", Toast.LENGTH_SHORT).show();
                    childBean.saleNum=1;
                }
            }
        });
        Glide.with(context).load(childBean1.imageUrl).into(list_2.image);
        list_2.checkBox.setChecked(childBean.isCheck);
        list_2.title.setText(childBean.content);
        list_2.price.setText("￥ "+childBean.price);
        //CheckBox添加监听器
        list_2.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ChildBean childBean1= (ChildBean) list_21.checkBox.getTag();
                childBean1.isCheck=b;
                childCheck(i);
                //接口回调    调用计算总价的方法
                iCartView.addPrice();
                //刷新适配器
                notifyDataSetChanged();
                SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
                String uid = sp.getString("uid", null);
                String s="0";
                if(b){
                    s = "1";
                }else{
                    s="0";
                }
               /* shuaxin(uid,childList.get(i).get(i1).sellerid,childList.get(i).get(i1).pid,childList.get(i).get(i1).saleNum+""
                ,s);*/
            }
        });
        return view;
    }
    class List_2
    {
        CheckBox checkBox;
        ImageView image;
        TextView title,price,num;

        RelativeLayout relative;
        LinearLayout linear;
        Button add,jian,del;
        TextView salenum;
    }
    class List_1
    {
        CheckBox checkBox;
        TextView shop_name,bianji;
    }
    //改变编辑状态的方法
    private void ziChangeBianji(int position,boolean flag)
    {
        //修改子布局
        List<ChildBean> childBeen=childList.get(position);
        for (int i=0;i<childBeen.size();i++)
        {
            ChildBean bean=childBeen.get(i);
            bean.viewChange=flag;
        }
    }
    //全选
    public void allCheck(boolean isCheck)
    {
        //通过遍历所有的集合,修改bean类来控制CheckBox的选中状态
        for(int i=0;i<getGroupCount();i++)
        {
            ParentBean p=parentList.get(i);
            p.isCheck=isCheck;

            for(int a=0;a<getChildrenCount(i);a++)
            {
                ChildBean c=childList.get(i).get(a);
                c.isCheck=isCheck;
            }
        }
        //刷新适配器
        notifyDataSetChanged();
    }

    //判断二级列表全选中时,一级列表选中
    private void childCheck(int parentPosition)
    {
        //获取一级列表的bean 来修改isCheck属性
        ParentBean parentBean=getGroup(parentPosition);

        for(int i=0;i<getChildrenCount(parentPosition);i++)
        {
            //当前的Childbean
            ChildBean bean=getChild(parentPosition,i);
            if(!bean.isCheck)
            {
                parentBean.isCheck=false;
                return;
            }
            parentBean.isCheck=true;
        }
    }

    //一级列表的全选    一级列表不需要反选
    private void parentCheck(int parentPosition)
    {
        //获取一级列表的bean 来修改isCheck属性
        ParentBean parentBean=getGroup(parentPosition);

        if(parentBean.isCheck)
        {
            shopCheck(true,childList.get(parentPosition));
            return;
        }
    }

    //修改二级列表Check的属性
    private void shopCheck(boolean flag,List<ChildBean> childBeans)
    {
        for(ChildBean c:childBeans)
        {
            c.isCheck=flag;
        }
    }

    //当一级列表全选时,全选按钮选中
    private void allCheckChangeFromParent()
    {
        //遍历一级列表数据
        for (ParentBean p:parentList)
        {
            //有一个未被选中,则全选按钮不会被选中
            if(!p.isCheck)
            {
                iCartView.changeCheckBtn(false);
                return;
            }
        }
        //如果都被选中,则全选按钮选中
        iCartView.changeCheckBtn(true);
    }
    /*public void shuaxin(String uid,String sellerid,String pid,String num,String selected){
        Map map1 = new HashMap();
        map1.put("uid",uid);
        map1.put("sellerid",sellerid);
        map1.put("pid",pid);
        map1.put("num",num);
        map1.put("selected",selected);
        OkHttp3Utils.doPost(Api.UPDATECARTS_API, map1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });*/
    //}
}