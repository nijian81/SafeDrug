package cn.com.phhc.drugSafeApp.utils;

import java.io.Serializable;

//用于药品查询界面列表项展示 尼见 2015-03-09
public class DrugImageItem implements Serializable{

    String flag;

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {

        return flag;
    }

    public DrugImageItem(String flag) {

        this.flag = flag;
    }
}
