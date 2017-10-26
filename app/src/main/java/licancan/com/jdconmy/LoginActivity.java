package licancan.com.jdconmy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import licancan.com.jdconmy.presenter.LoginPresenter;
import licancan.com.jdconmy.view.LoginView;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView {

    private ImageView iv_back,qq_login;
    private EditText et_name,et_pass;
    private Button but_login;
    private TextView tv_regist,tv_forget;
    private LoginPresenter loginPresenter;
    private Bitmap bitmap;
    private String imagePath;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initData() {
        loginPresenter = new LoginPresenter(LoginActivity.this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_name = (EditText) findViewById(R.id.et_name);
        et_pass = (EditText) findViewById(R.id.et_pass);
        but_login = (Button) findViewById(R.id.but_login);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        qq_login= (ImageView) findViewById(R.id.qq_login);
        iv_back.setOnClickListener(this);
        but_login.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        qq_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            //关闭此页面
            case R.id.iv_back:
                finish();
                break;
            //登录
            case R.id.but_login:
                loginPresenter.login(et_name.getText().toString(),et_pass.getText().toString());
                finish();
                break;
            //手机注册
            case R.id.tv_regist:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginPresenter.regist(et_name.getText().toString(),et_pass.getText().toString());

                    }
                });
                break;
            //忘记密码
            case R.id.tv_forget:
                break;

            //第三方QQ登录
            case R.id.qq_login:
                Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void nameError(final String msg) {
    //用户名错误
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void passError(final String msg) {
        //密码错误
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loginSuccess(String code, final String msg) {
        //走登陆成功的逻辑
        System.out.println(msg+"======  登录成功");
        try {
            JSONObject obj = new JSONObject(msg);
            JSONObject data = obj.getJSONObject("data");
            String icon = data.getString("icon");
            String uid = data.getString("uid");
            System.out.println("uid========="+uid);
            String username = data.getString("username");
            System.out.println("username========="+username);
            //用sp保存数据
            sp = getSharedPreferences("config",MODE_PRIVATE);
            sp.edit().putBoolean("login",true).commit();
            sp.edit().putString("uid",uid).commit();//记得提交
            sp.edit().putString("username",username).commit();//记得提交
            URL url = new URL(icon);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.connect();
            InputStream in=uc.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                qq_login.setImageBitmap(bitmap);
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void loginFail(String code, String msg) {
        System.out.println("登录失败");
    }

    @Override
    public void failure(Call call, IOException e) {
        System.out.println("请求失败");
    }

    private void set(Bitmap bm) {
        File file=new File(getCacheDir()+"/1.jpg");
        try {
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG,100,bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode== Activity.RESULT_OK&&data!=null){
            Uri u =  data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(u, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imagePath = c.getString(columnIndex);

            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            set(bm);
            File file=new File(getCacheDir()+"/1.jpg");
            loginPresenter.Lo1(et_name.getText().toString(),file);
            ((ImageView)findViewById(R.id.qq_login)).setImageBitmap(bm);
            c.close();
        }
        System.out.println("imagePath = " + imagePath);



    }
}
