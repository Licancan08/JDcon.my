package xp.code.okhttp3.utils.callback;

import android.os.Handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import xp.code.okhttp3.utils.OkHttp3Utils;

/**
 * Created by XP on 2017/10/16.
 */
public abstract class GsonObjCallback<T> implements Callback {
    private Handler handler= OkHttp3Utils.getHandler();

    public abstract void onFail(Call call,IOException e);

    public abstract void onSucc(T t);
    @Override
    public void onFailure(final Call call, final IOException e) {
        //主线程调用
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFail(call,e);
            }
        });
    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //将返回的json串转换成对象
        String json=response.body().string();
        Class<T> cla=null;

        Class clz=this.getClass();
        ParameterizedType type= (ParameterizedType) clz.getGenericSuperclass();
        Type[] types=type.getActualTypeArguments();

        cla= (Class<T>) types[0];
        Gson gson=new Gson();
        final T t=gson.fromJson(json,cla);
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSucc(t);
            }
        });
    }
}
