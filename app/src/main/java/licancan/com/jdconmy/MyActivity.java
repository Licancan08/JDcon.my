package licancan.com.jdconmy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import licancan.com.jdconmy.common.Api;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

import static android.R.attr.name;

public class MyActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView get_head;
    private TextView my_mobile;
    private RelativeLayout layout_name;
    private TextView tv_name;
    private SharedPreferences sp;
    private String uid;
    private String nick;
    private Button back;

    private static final int CHOOSE_PICTURE=0;
    private static final int TAKE_PICTURE=1;
    private static final int CROP_SMALL_PICTURE=2;
    private static Uri tempUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initView();
        initData();
    }

    private void initView() {
        get_head = (ImageView) findViewById(R.id.get_head);
        get_head.setOnClickListener(this);
        my_mobile = (TextView) findViewById(R.id.my_mobile);
        layout_name = (RelativeLayout) findViewById(R.id.layout_name);
        tv_name = (TextView) findViewById(R.id.tv_name);
        layout_name.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
    }


    private void initData() {
        sp = getSharedPreferences("config",MODE_PRIVATE);
        String username = sp.getString("username","");
        uid = sp.getString("uid", "0");
        my_mobile.setText(username);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                String uid1 = sp.getString("uid", null);
                break;
            case R.id.layout_name:
                View v=View.inflate(this,R.layout.change_name,null);
                final EditText et_name=v.findViewById(R.id.et_name);
                AlertDialog.Builder b=new AlertDialog.Builder(this);
                b.setMessage("确定要修改昵称吗？");
                b.setView(v);
                b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    private String nick;

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nick = et_name.getText().toString();
                        sp.edit().putString("nickname",nick).commit();
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("uid",uid);
                        map.put("nickname", nick);
                        OkHttp3Utils.doPost(Api.NickName_API, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                System.out.println("result"+result);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_name.setText(nick);
                                        Toast.makeText(MyActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });
                b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MyActivity.this, "继续使用原昵称", Toast.LENGTH_SHORT).show();
                    }
                });

                b.create().show();

                break;
            case R.id.get_head:
                AlertDialog.Builder builder=new AlertDialog.Builder(MyActivity.this);
                builder.setTitle("设置头像");
                String[] items={"选择本地照片","拍照"};
                builder.setNegativeButton("取消",null);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case CHOOSE_PICTURE://选择本地图片
                                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);;
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                                startActivityForResult(intent,CHOOSE_PICTURE);
                                break;
                            case TAKE_PICTURE://拍照
                                Intent intent1=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                tempUri= Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                                intent1.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
                                startActivityForResult(intent1,TAKE_PICTURE);
                                break;
                        }
                    }
                });
                builder.create().show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String nickname = sp.getString("nickname", "Nancy");
        tv_name.setText(nickname);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PICTURE:
                startPhotoZoom(tempUri);
                break;
            case CHOOSE_PICTURE:
                startPhotoZoom(data.getData());
                break;
            case CROP_SMALL_PICTURE:
                if(data!=null){
                    setImageToView(data);
                }
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        if(uri==null){
            Log.i("tag","The uri is not exist");
        }
        tempUri=uri;
        Intent in=new Intent("com.android.camera.action.CROP");
        in.setDataAndType(uri,"image/*");
        //设置裁剪
        in.putExtra("crop","true");
        in.putExtra("aspectX",1);
        in.putExtra("aspectY",1);
        //宽高
        in.putExtra("outputX",150);
        in.putExtra("outputY",150);
        in.putExtra("return-data",true);
        startActivityForResult(in,CROP_SMALL_PICTURE);
    }
    private void setImageToView(Intent data) {
        Bundle extras=data.getExtras();
        if(extras!=null){
            Bitmap photo=extras.getParcelable("data");
            get_head.setImageBitmap(photo);
            saveImage(photo);
            File file=new File(MyActivity.this.getCacheDir()+"/aa.jpg");
            OkHttpClient ck=new OkHttpClient();
            MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("file",file.getName(), RequestBody.create(MediaType.parse("image/*"),file));
            Request request=new Request.Builder().url(Api.PHOTO_API).post(builder.build()).build();
            ck.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(MyActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject json=new JSONObject(response.body().string());
                        final String msg = json.optString("msg");
                        MyActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyActivity.this, "上传头像成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void saveImage(Bitmap photo) {
        File file=new File(MyActivity.this.getCacheDir()+"/aa.jpg");
        try {
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.JPEG,100,bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
