package licancan.com.jdconmy.entity;

import java.util.List;

/**
 * Created by robot on 2017/10/22.
 */

public class OrderBean {

    /**
     * msg : 请求成功
     * code : 0
     * data : [{"createtime":"2017-10-22T16:16:03","orderid":932,"price":999,"status":0,"uid":148},{"createtime":"2017-10-22T16:16:57","orderid":933,"price":999,"status":0,"uid":148},{"createtime":"2017-10-22T16:17:40","orderid":935,"price":999,"status":0,"uid":148},{"createtime":"2017-10-22T16:17:46","orderid":936,"price":999,"status":0,"uid":148},{"createtime":"2017-10-22T16:18:18","orderid":937,"price":999,"status":0,"uid":148},{"createtime":"2017-10-22T16:18:53","orderid":939,"price":999,"status":0,"uid":148}]
     * page : 1
     */

    private String msg;
    private String code;
    private String page;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * createtime : 2017-10-22T16:16:03
         * orderid : 932
         * price : 999.0
         * status : 0
         * uid : 148
         */

        private String createtime;
        private int orderid;
        private double price;
        private int status;
        private int uid;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
