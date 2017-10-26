package licancan.com.jdconmy.entity;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by robot on 2017/10/10.
 */

public class ListBean1 {
   public String iv;
    public String tv;

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    public ListBean1(String iv, String tv) {
        this.iv = iv;
        this.tv = tv;
    }

    public ListBean1() {
    }
}
