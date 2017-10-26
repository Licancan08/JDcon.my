package licancan.com.jdconmy.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import licancan.com.jdconmy.R;

/**
 * Created by XP on 2017/10/18.
 */
public class TopBar extends RelativeLayout {
    private ImageView left_iv,right_iv;
    private TextView title_tv;

    private LayoutParams leftParams,rightParams,titleParams;

    private String title;       //标题
    private int titleColor;     //标题颜色
    private float titleSize;    //标题大小
    private Drawable leftImageSrc;   //左侧图片
    private Drawable rightImageSrc;  //左侧图片

    private TopBarClickListener clickListener;

    public TopBar(Context context) {
        this(context,null);
    }
    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        //取出对应的值
        title=ta.getString(R.styleable.TopBar_title);
        titleColor=ta.getColor(R.styleable.TopBar_titleColor,0x000);
        titleSize=ta.getDimension(R.styleable.TopBar_titleSize,16f);
        leftImageSrc=ta.getDrawable(R.styleable.TopBar_leftImageSrc);
        rightImageSrc=ta.getDrawable(R.styleable.TopBar_rightImageSrc);

        //调用recycle()  避免重复创建时有误
        ta.recycle();

        //初始化控件
        left_iv=new ImageView(context);
        right_iv=new ImageView(context);
        title_tv=new TextView(context);

        //给控件赋值

        title_tv.setText(title);
        title_tv.setTextColor(titleColor);
        title_tv.setTextSize(titleSize);
        title_tv.setGravity(Gravity.CENTER);

        if(leftImageSrc!=null)
        {
            left_iv.setImageDrawable(leftImageSrc);
        }

        if(rightImageSrc!=null)
        {
            right_iv.setImageDrawable(rightImageSrc);
        }

        //为控件设置对应的布局
        leftParams=new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL,TRUE);
        leftParams.setMargins(30,0,0,0);
        addView(left_iv,leftParams);


        rightParams=new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL,TRUE);
        rightParams.setMargins(0,0,30,0);
        addView(right_iv,rightParams);


        titleParams=new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(title_tv,titleParams);

    }

    public void setOnTopBarClickListener(TopBarClickListener listener)
    {
        this.clickListener=listener;
        left_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.leftClick();
            }
        });
        right_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.rightClick();
            }
        });
    }

    public interface TopBarClickListener{
        void leftClick();
        void rightClick();
    }
}
