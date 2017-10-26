package licancan.com.jdconmy.utils;

import java.io.IOException;

import licancan.com.jdconmy.common.Api;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by robot on 2017/10/10.
 */

public class LoginUtils {
    public static void getResult(String mobile, String pwd, final IRegist iRegist)
    {
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("mobile",mobile);
        builder.add("password",pwd);
        FormBody body=builder.build();
        final Request request=new Request.Builder().post(body).url(Api.REGISTER_API).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                iRegist.registFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                iRegist.registSuccess(result);
                System.out.println("=======注册成功"+result);
            }
        });
    }
    public interface IRegist{
        void registSuccess(String result);
        void registFail();
    }
}
