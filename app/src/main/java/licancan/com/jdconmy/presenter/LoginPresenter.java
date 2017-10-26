package licancan.com.jdconmy.presenter;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

import licancan.com.jdconmy.model.LoginModel;
import licancan.com.jdconmy.utils.LoginUtils;
import licancan.com.jdconmy.view.LoginView;
import okhttp3.Call;

/**
 * Created by robot on 2017/10/10.
 */

public class LoginPresenter implements LoginModel.ILogin{
    private LoginView loginView;
    private LoginModel loginModel;
    private final LoginUtils loginUtils;

    public LoginPresenter(LoginView loginView)
    {
        this.loginView=loginView;
        loginModel=new LoginModel();
        loginModel.setiLogin(this);
        loginUtils = new LoginUtils();
    }

    /**
     * 登录
     * @param mobile
     * @param pass
     */
    public void login(String mobile,String pass)
    {

        if(TextUtils.isEmpty(mobile))
        {
            loginView.nameError("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            loginView.passError("密码不能为空");
            return;
        }
        loginModel.login(mobile,pass);
    }

    /**
     * 注册
     */
    public void regist(String mobile,String pass)
    {
        if(TextUtils.isEmpty(mobile))
        {
            loginView.nameError("号码不能为空");
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            loginView.passError("密码不能为空");
            return;
        }
        //注册具体操作的方法
        loginModel.regist(mobile,pass);
    }

    /**
     * 上传图像
     * @param name
     * @param img
     */
    public void Lo1(String name,File img){
        if(TextUtils.isEmpty(name)){
            loginView.nameError("用户名为空");
            return;
        }
        if(img==null){
            loginView.nameError("图片为空");
            return;
        }
        loginModel.p2(name,img);

    }


    @Override
    public void loginSuccess(String code, String msg) {
        loginView.loginSuccess(code,msg);
    }

    @Override
    public void loginFail(String code, String msg) {
        loginView.loginFail(code,msg);
    }

    @Override
    public void failure(Call call, IOException e) {
        loginView.failure(call,e);
    }
}
