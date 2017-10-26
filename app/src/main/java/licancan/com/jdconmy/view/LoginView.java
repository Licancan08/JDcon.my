package licancan.com.jdconmy.view;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by robot on 2017/10/10.
 */

public interface LoginView {
    void nameError(String msg);
    void passError(String msg);
    public void loginSuccess(String code, String msg);
    void loginFail(String code,String msg);
    void failure(Call call, IOException e);
}
