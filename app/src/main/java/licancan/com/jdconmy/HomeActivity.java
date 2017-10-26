package licancan.com.jdconmy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import licancan.com.jdconmy.fragment.Fragment1;
import licancan.com.jdconmy.fragment.Fragment2;
import licancan.com.jdconmy.fragment.Fragment3;
import licancan.com.jdconmy.fragment.Fragment5;
import licancan.com.jdconmy.fragment.GouwucheFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton but_1;
    private RadioButton but_2;
    private RadioButton but_3;
    private RadioButton but_4;
    private RadioButton but_5;
    private Fragment1 f1;
    private Fragment2 f2;
    private Fragment3 f3;
    private Fragment5 f5;

    private GouwucheFragment gouwucheFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //RadioButton按钮
        but_1 = (RadioButton) findViewById(R.id.but_1);
        but_2 = (RadioButton) findViewById(R.id.but_2);
        but_3 = (RadioButton) findViewById(R.id.but_3);
        but_4 = (RadioButton) findViewById(R.id.but_4);
        but_5 = (RadioButton) findViewById(R.id.but_5);
        but_1.setOnClickListener(this);
        but_2.setOnClickListener(this);
        but_3.setOnClickListener(this);
        but_4.setOnClickListener(this);
        but_5.setOnClickListener(this);


    }


    /**
     * 初始化数据
     */
    private void initData() {
        f1 = new Fragment1();
        f2 = new Fragment2();
        f3 = new Fragment3();
        f5 = new Fragment5();
        gouwucheFragment=new GouwucheFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_layout, f1).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_layout, f2).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_layout, f3).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_layout, gouwucheFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_layout, f5).commit();

        //默认展示
        but_1.setChecked(true);
        but_1.setTextColor(Color.RED);
        getSupportFragmentManager().beginTransaction().show(f1).commit();
        getSupportFragmentManager().beginTransaction().hide(f2).commit();
        getSupportFragmentManager().beginTransaction().hide(f3).commit();
        getSupportFragmentManager().beginTransaction().hide(gouwucheFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(f5).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.but_1:
                but_1.setChecked(true);
                but_2.setChecked(false);
                but_3.setChecked(false);
                but_4.setChecked(false);
                but_5.setChecked(false);
                but_1.setTextColor(Color.RED);
                but_2.setTextColor(Color.GRAY);
                but_3.setTextColor(Color.GRAY);
                but_4.setTextColor(Color.GRAY);
                but_5.setTextColor(Color.GRAY);
                getSupportFragmentManager().beginTransaction().show(f1).commit();
                getSupportFragmentManager().beginTransaction().hide(f2).commit();
                getSupportFragmentManager().beginTransaction().hide(f3).commit();
                getSupportFragmentManager().beginTransaction().hide(gouwucheFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(f5).commit();
                break;
            case R.id.but_2:
                but_2.setChecked(true);
                but_1.setChecked(false);
                but_3.setChecked(false);
                but_4.setChecked(false);
                but_5.setChecked(false);
                but_2.setTextColor(Color.RED);
                but_1.setTextColor(Color.GRAY);
                but_3.setTextColor(Color.GRAY);
                but_4.setTextColor(Color.GRAY);
                but_5.setTextColor(Color.GRAY);
                getSupportFragmentManager().beginTransaction().show(f2).commit();
                getSupportFragmentManager().beginTransaction().hide(f1).commit();
                getSupportFragmentManager().beginTransaction().hide(f3).commit();
                getSupportFragmentManager().beginTransaction().hide(gouwucheFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(f5).commit();
                break;

            case R.id.but_3:
                but_3.setChecked(true);
                but_1.setChecked(false);
                but_2.setChecked(false);
                but_4.setChecked(false);
                but_5.setChecked(false);
                but_3.setTextColor(Color.RED);
                but_2.setTextColor(Color.GRAY);
                but_1.setTextColor(Color.GRAY);
                but_4.setTextColor(Color.GRAY);
                but_5.setTextColor(Color.GRAY);
                getSupportFragmentManager().beginTransaction().show(f3).commit();
                getSupportFragmentManager().beginTransaction().hide(f2).commit();
                getSupportFragmentManager().beginTransaction().hide(f1).commit();
                getSupportFragmentManager().beginTransaction().hide(gouwucheFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(f5).commit();
                break;
            case R.id.but_4:
                but_4.setChecked(true);
                but_1.setChecked(false);
                but_3.setChecked(false);
                but_2.setChecked(false);
                but_5.setChecked(false);
                but_4.setTextColor(Color.RED);
                but_2.setTextColor(Color.GRAY);
                but_3.setTextColor(Color.GRAY);
                but_1.setTextColor(Color.GRAY);
                but_5.setTextColor(Color.GRAY);
                getSupportFragmentManager().beginTransaction().show(gouwucheFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(f2).commit();
                getSupportFragmentManager().beginTransaction().hide(f3).commit();
                getSupportFragmentManager().beginTransaction().hide(f1).commit();
                getSupportFragmentManager().beginTransaction().hide(f5).commit();
                break;
            case R.id.but_5:
                but_5.setChecked(true);
                but_1.setChecked(false);
                but_3.setChecked(false);
                but_4.setChecked(false);
                but_2.setChecked(false);
                but_5.setTextColor(Color.RED);
                but_2.setTextColor(Color.GRAY);
                but_3.setTextColor(Color.GRAY);
                but_4.setTextColor(Color.GRAY);
                but_1.setTextColor(Color.GRAY);
                getSupportFragmentManager().beginTransaction().show(f5).commit();
                getSupportFragmentManager().beginTransaction().hide(f2).commit();
                getSupportFragmentManager().beginTransaction().hide(f3).commit();
                getSupportFragmentManager().beginTransaction().hide(gouwucheFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(f1).commit();
                break;
        }
    }
}
