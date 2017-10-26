package licancan.com.jdconmy.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import licancan.com.jdconmy.LoginActivity;
import licancan.com.jdconmy.MyActivity;
import licancan.com.jdconmy.OrderActivity;
import licancan.com.jdconmy.R;
import licancan.com.jdconmy.common.Api;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by robot on 2017/9/29.
 */

public class Fragment5 extends Fragment implements View.OnClickListener {

    private View view;
    private TextView login_regist;
    private ImageView image_image;

    private static final int CHOOSE_PICTURE=0;
    private static final int TAKE_PICTURE=1;
    private static final int CROP_SMALL_PICTURE=2;
    private static Uri tempUri;
    private SharedPreferences sp;
    private ImageView exit;
    private ImageView order;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment5,null);
        initView();
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        return view;
    }

    private void initView() {
        login_regist = view.findViewById(R.id.login_regist);
        login_regist.setOnClickListener(this);
        image_image = view.findViewById(R.id.iv_login);
        image_image.setOnClickListener(this);
        exit = view.findViewById(R.id.exit);
        exit.setOnClickListener(this);

        order = view.findViewById(R.id.order);
        order.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            //我的订单
            case R.id.order:
                Intent intent1=new Intent(getActivity(), OrderActivity.class);
                startActivity(intent1);
                break;
            //注册/登录
            case R.id.login_regist:
                String uid = sp.getString("uid", "0");
                if(uid==0+"")
                {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getActivity(), MyActivity.class);
                    startActivity(intent);
                }
                String username = sp.getString("username", "");
                login_regist.setText(username);
                break;
            //退出登录
            case R.id.exit:
                if(sp.getBoolean("login",false)){
                    login_regist.setText("登录/注册");
                    String uid1 = sp.getString("uid", null);
                    uid1=null;
                }

                break;
            case R.id.iv_login:
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
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
                                tempUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
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
            image_image.setImageBitmap(photo);
            saveImage(photo);
            File file=new File(getActivity().getCacheDir()+"/aa.jpg");
            OkHttpClient ck=new OkHttpClient();
            MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("file",file.getName(), RequestBody.create(MediaType.parse("image/*"),file));
            Request request=new Request.Builder().url(Api.PHOTO_API).post(builder.build()).build();
            ck.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(getActivity(), "上传头像失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject json=new JSONObject(response.body().string());
                        final String msg = json.optString("msg");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "上传头像成功", Toast.LENGTH_SHORT).show();
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
        File file=new File(getActivity().getCacheDir()+"/aa.jpg");
        try {
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
            photo.compress(Bitmap.CompressFormat.JPEG,100,bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(sp.getBoolean("login",false))
        {
            String username = sp.getString("username","name");
            login_regist.setText(username);
        }
        else{
            login_regist.setText("登录/注册");
        }
    }
}
