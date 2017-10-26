package licancan.com.jdconmy.entity;

import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by XP on 2017/10/18.
 */
public class ChildBean {
    public String content;      //二级列表的内容
    public boolean isCheck;     //二级列表的CheckBox
    public boolean viewChange;  //二级列表是否显示编辑界面
    public int saleNum;         //二级列表的数量
    public int price;           //二级列表的价格
    public String imageUrl;     //二级列表的图片网址
    public String uid;
    public String pid;

    public ChildBean(String uid, String pid, String content, int saleNum, boolean isCheck, int price, boolean viewChange, String imageUrl)
    {
        this.uid=uid;
        this.pid=pid;
        this.imageUrl=imageUrl;
        this.saleNum=saleNum;
        this.viewChange=viewChange;
        this.content=content;
        this.isCheck=isCheck;
        this.price=price;

    }
}
