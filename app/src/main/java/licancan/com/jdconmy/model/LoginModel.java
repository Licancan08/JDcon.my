package licancan.com.jdconmy.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import licancan.com.jdconmy.common.Api;
import licancan.com.jdconmy.utils.LoginUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by robot on 2017/10/10.
 */

public class LoginModel {
    private ILogin iLogin;
    /**
     * 登录的操作
     * @param mobile
     * @param pwd
     */
    public void login(String mobile,String pwd)
    {
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("mobile",mobile);
        builder.add("password",pwd);
        FormBody body=builder.build();
        final Request request=new Request.Builder().post(body).url(Api.LOGIN_API).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iLogin.failure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null&&response.isSuccessful())
                {
                    String result=response.body().string();
                    System.out.println("result========="+result);
                    try {
                        JSONObject obj=new JSONObject(result);
                        String code = obj.getString("code");
                        if(code.equals("0"))
                        {
                            iLogin.loginSuccess(code,result);
                        }else{
                            iLogin.loginFail(code,"111");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 注册的操作
     * @param mobile
     * @param pwd
     */
    public void regist(final String mobile, final String pwd)
    {
        LoginUtils.getResult(mobile, pwd, new LoginUtils.IRegist() {
            @Override
            public void registSuccess(String result) {
                System.out.println("注册成功");
            }

            @Override
            public void registFail() {
                iLogin.loginFail(mobile,pwd);
            }
        });
    }

    /**
     * 上传头像
     * @param phone
     * @param img
     */
    public void p2(String phone,File img) {

        OkHttpClient mOkHttpClent = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", img.getName(), RequestBody.create(MediaType.parse("image/png"), img))
                .addFormDataPart("mobile", phone);

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(Api.PHOTO_API)
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iLogin.failure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s1 = response.body().string();
                try {
                    JSONObject json1 = new JSONObject(s1);
                    String code1 = json1.getString("code");
                    if (code1.equals(0)) {
                        iLogin.loginSuccess(code1, "成功");
                    } else {
                        iLogin.loginFail(code1, "失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setiLogin(ILogin iLogin)
    {
        this.iLogin=iLogin;
    }

    public interface ILogin{
        void loginSuccess(String code,String msg);
        void loginFail(String code,String msg);
        void failure(Call call, IOException e);
    }
}
