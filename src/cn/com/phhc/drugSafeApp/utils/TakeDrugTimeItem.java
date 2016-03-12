package cn.com.phhc.drugSafeApp.utils;

import java.io.Serializable;

//用于药品查询界面列表项展示 尼见 2015-03-09
public class TakeDrugTimeItem implements Serializable{

    String take_drug_time;
    int set_drug_num, take_drug_num;
    String isFirstAdd;

    String drug_name_setting_1;
    String drug_desc_setting_1;
    String begin_date_setting_1;
    String end_date_setting_1;

    String drug_name_setting_2;
    String drug_desc_setting_2;
    String begin_date_setting_2;
    String end_date_setting_2;

    String drug_name_setting_3;
    String drug_desc_setting_3;
    String begin_date_setting_3;
    String end_date_setting_3;

    String drug_name_setting_4;
    String drug_desc_setting_4;
    String begin_date_setting_4;
    String end_date_setting_4;

    String drug_name_setting_5;
    String drug_desc_setting_5;
    String begin_date_setting_5;
    String end_date_setting_5;

    String drug_name_1;
    String drug_desc_1;
    String begin_date_1;
    String end_date_1;

    String drug_name_2;
    String drug_desc_2;
    String begin_date_2;
    String end_date_2;

    String drug_name_3;
    String drug_desc_3;
    String begin_date_3;
    String end_date_3;

    String drug_name_4;
    String drug_desc_4;
    String begin_date_4;
    String end_date_4;

    String drug_name_5;
    String drug_desc_5;
    String begin_date_5;
    String end_date_5;

    public TakeDrugTimeItem() {
    }

    public TakeDrugTimeItem(String isFirstAdd, String take_drug_time, int set_drug_num, int take_drug_num, String drug_name_setting_1, String drug_desc_setting_1, String begin_date_setting_1, String end_date_setting_1, String drug_name_setting_2, String drug_desc_setting_2, String begin_date_setting_2, String end_date_setting_2, String drug_name_setting_3, String drug_desc_setting_3, String begin_date_setting_3, String end_date_setting_3, String drug_name_setting_4, String drug_desc_setting_4, String begin_date_setting_4, String end_date_setting_4, String drug_name_setting_5, String drug_desc_setting_5, String begin_date_setting_5, String end_date_setting_5, String drug_name_1, String drug_desc_1, String begin_date_1, String end_date_1, String drug_name_2, String drug_desc_2, String begin_date_2, String end_date_2, String drug_name_3, String drug_desc_3, String begin_date_3, String end_date_3, String drug_name_4, String drug_desc_4, String begin_date_4, String end_date_4, String drug_name_5, String drug_desc_5, String begin_date_5, String end_date_5) {
        this.isFirstAdd = isFirstAdd;
        this.take_drug_time = take_drug_time;
        this.set_drug_num = set_drug_num;
        this.take_drug_num = take_drug_num;
        this.drug_name_setting_1 = drug_name_setting_1;
        this.drug_desc_setting_1 = drug_desc_setting_1;
        this.begin_date_setting_1 = begin_date_setting_1;
        this.end_date_setting_1 = end_date_setting_1;
        this.drug_name_setting_2 = drug_name_setting_2;
        this.drug_desc_setting_2 = drug_desc_setting_2;
        this.begin_date_setting_2 = begin_date_setting_2;
        this.end_date_setting_2 = end_date_setting_2;
        this.drug_name_setting_3 = drug_name_setting_3;
        this.drug_desc_setting_3 = drug_desc_setting_3;
        this.begin_date_setting_3 = begin_date_setting_3;
        this.end_date_setting_3 = end_date_setting_3;
        this.drug_name_setting_4 = drug_name_setting_4;
        this.drug_desc_setting_4 = drug_desc_setting_4;
        this.begin_date_setting_4 = begin_date_setting_4;
        this.end_date_setting_4 = end_date_setting_4;
        this.drug_name_setting_5 = drug_name_setting_5;
        this.drug_desc_setting_5 = drug_desc_setting_5;
        this.begin_date_setting_5 = begin_date_setting_5;
        this.end_date_setting_5 = end_date_setting_5;
        this.drug_name_1 = drug_name_1;
        this.drug_desc_1 = drug_desc_1;
        this.begin_date_1 = begin_date_1;
        this.end_date_1 = end_date_1;
        this.drug_name_2 = drug_name_2;
        this.drug_desc_2 = drug_desc_2;
        this.begin_date_2 = begin_date_2;
        this.end_date_2 = end_date_2;
        this.drug_name_3 = drug_name_3;
        this.drug_desc_3 = drug_desc_3;
        this.begin_date_3 = begin_date_3;
        this.end_date_3 = end_date_3;
        this.drug_name_4 = drug_name_4;
        this.drug_desc_4 = drug_desc_4;
        this.begin_date_4 = begin_date_4;
        this.end_date_4 = end_date_4;
        this.drug_name_5 = drug_name_5;
        this.drug_desc_5 = drug_desc_5;
        this.begin_date_5 = begin_date_5;
        this.end_date_5 = end_date_5;
    }

    public String getIsFirstAdd() {
        return isFirstAdd;
    }

    public void setIsFirstAdd(String isFirstAdd) {
        this.isFirstAdd = isFirstAdd;
    }

    public String getTake_drug_time() {
        return take_drug_time;
    }

    public void setTake_drug_time(String take_drug_time) {
        this.take_drug_time = take_drug_time;
    }

    public int getSet_drug_num() {
        return set_drug_num;
    }

    public void setSet_drug_num(int set_drug_num) {
        this.set_drug_num = set_drug_num;
    }

    public int getTake_drug_num() {
        return take_drug_num;
    }

    public void setTake_drug_num(int take_drug_num) {
        this.take_drug_num = take_drug_num;
    }

    public String getDrug_name_setting_1() {
        return drug_name_setting_1;
    }

    public void setDrug_name_setting_1(String drug_name_setting_1) {
        this.drug_name_setting_1 = drug_name_setting_1;
    }

    public String getDrug_desc_setting_1() {
        return drug_desc_setting_1;
    }

    public void setDrug_desc_setting_1(String drug_desc_setting_1) {
        this.drug_desc_setting_1 = drug_desc_setting_1;
    }

    public String getBegin_date_setting_1() {
        return begin_date_setting_1;
    }

    public void setBegin_date_setting_1(String begin_date_setting_1) {
        this.begin_date_setting_1 = begin_date_setting_1;
    }

    public String getEnd_date_setting_1() {
        return end_date_setting_1;
    }

    public void setEnd_date_setting_1(String end_date_setting_1) {
        this.end_date_setting_1 = end_date_setting_1;
    }

    public String getDrug_name_setting_2() {
        return drug_name_setting_2;
    }

    public void setDrug_name_setting_2(String drug_name_setting_2) {
        this.drug_name_setting_2 = drug_name_setting_2;
    }

    public String getDrug_desc_setting_2() {
        return drug_desc_setting_2;
    }

    public void setDrug_desc_setting_2(String drug_desc_setting_2) {
        this.drug_desc_setting_2 = drug_desc_setting_2;
    }

    public String getBegin_date_setting_2() {
        return begin_date_setting_2;
    }

    public void setBegin_date_setting_2(String begin_date_setting_2) {
        this.begin_date_setting_2 = begin_date_setting_2;
    }

    public String getEnd_date_setting_2() {
        return end_date_setting_2;
    }

    public void setEnd_date_setting_2(String end_date_setting_2) {
        this.end_date_setting_2 = end_date_setting_2;
    }

    public String getDrug_name_setting_3() {
        return drug_name_setting_3;
    }

    public void setDrug_name_setting_3(String drug_name_setting_3) {
        this.drug_name_setting_3 = drug_name_setting_3;
    }

    public String getDrug_desc_setting_3() {
        return drug_desc_setting_3;
    }

    public void setDrug_desc_setting_3(String drug_desc_setting_3) {
        this.drug_desc_setting_3 = drug_desc_setting_3;
    }

    public String getBegin_date_setting_3() {
        return begin_date_setting_3;
    }

    public void setBegin_date_setting_3(String begin_date_setting_3) {
        this.begin_date_setting_3 = begin_date_setting_3;
    }

    public String getEnd_date_setting_3() {
        return end_date_setting_3;
    }

    public void setEnd_date_setting_3(String end_date_setting_3) {
        this.end_date_setting_3 = end_date_setting_3;
    }

    public String getDrug_name_setting_4() {
        return drug_name_setting_4;
    }

    public void setDrug_name_setting_4(String drug_name_setting_4) {
        this.drug_name_setting_4 = drug_name_setting_4;
    }

    public String getDrug_desc_setting_4() {
        return drug_desc_setting_4;
    }

    public void setDrug_desc_setting_4(String drug_desc_setting_4) {
        this.drug_desc_setting_4 = drug_desc_setting_4;
    }

    public String getBegin_date_setting_4() {
        return begin_date_setting_4;
    }

    public void setBegin_date_setting_4(String begin_date_setting_4) {
        this.begin_date_setting_4 = begin_date_setting_4;
    }

    public String getEnd_date_setting_4() {
        return end_date_setting_4;
    }

    public void setEnd_date_setting_4(String end_date_setting_4) {
        this.end_date_setting_4 = end_date_setting_4;
    }

    public String getDrug_name_setting_5() {
        return drug_name_setting_5;
    }

    public void setDrug_name_setting_5(String drug_name_setting_5) {
        this.drug_name_setting_5 = drug_name_setting_5;
    }

    public String getDrug_desc_setting_5() {
        return drug_desc_setting_5;
    }

    public void setDrug_desc_setting_5(String drug_desc_setting_5) {
        this.drug_desc_setting_5 = drug_desc_setting_5;
    }

    public String getBegin_date_setting_5() {
        return begin_date_setting_5;
    }

    public void setBegin_date_setting_5(String begin_date_setting_5) {
        this.begin_date_setting_5 = begin_date_setting_5;
    }

    public String getEnd_date_setting_5() {
        return end_date_setting_5;
    }

    public void setEnd_date_setting_5(String end_date_setting_5) {
        this.end_date_setting_5 = end_date_setting_5;
    }

    public String getDrug_name_1() {
        return drug_name_1;
    }

    public void setDrug_name_1(String drug_name_1) {
        this.drug_name_1 = drug_name_1;
    }

    public String getDrug_desc_1() {
        return drug_desc_1;
    }

    public void setDrug_desc_1(String drug_desc_1) {
        this.drug_desc_1 = drug_desc_1;
    }

    public String getBegin_date_1() {
        return begin_date_1;
    }

    public void setBegin_date_1(String begin_date_1) {
        this.begin_date_1 = begin_date_1;
    }

    public String getEnd_date_1() {
        return end_date_1;
    }

    public void setEnd_date_1(String end_date_1) {
        this.end_date_1 = end_date_1;
    }

    public String getDrug_name_2() {
        return drug_name_2;
    }

    public void setDrug_name_2(String drug_name_2) {
        this.drug_name_2 = drug_name_2;
    }

    public String getDrug_desc_2() {
        return drug_desc_2;
    }

    public void setDrug_desc_2(String drug_desc_2) {
        this.drug_desc_2 = drug_desc_2;
    }

    public String getBegin_date_2() {
        return begin_date_2;
    }

    public void setBegin_date_2(String begin_date_2) {
        this.begin_date_2 = begin_date_2;
    }

    public String getEnd_date_2() {
        return end_date_2;
    }

    public void setEnd_date_2(String end_date_2) {
        this.end_date_2 = end_date_2;
    }

    public String getDrug_name_3() {
        return drug_name_3;
    }

    public void setDrug_name_3(String drug_name_3) {
        this.drug_name_3 = drug_name_3;
    }

    public String getDrug_desc_3() {
        return drug_desc_3;
    }

    public void setDrug_desc_3(String drug_desc_3) {
        this.drug_desc_3 = drug_desc_3;
    }

    public String getBegin_date_3() {
        return begin_date_3;
    }

    public void setBegin_date_3(String begin_date_3) {
        this.begin_date_3 = begin_date_3;
    }

    public String getEnd_date_3() {
        return end_date_3;
    }

    public void setEnd_date_3(String end_date_3) {
        this.end_date_3 = end_date_3;
    }

    public String getDrug_name_4() {
        return drug_name_4;
    }

    public void setDrug_name_4(String drug_name_4) {
        this.drug_name_4 = drug_name_4;
    }

    public String getDrug_desc_4() {
        return drug_desc_4;
    }

    public void setDrug_desc_4(String drug_desc_4) {
        this.drug_desc_4 = drug_desc_4;
    }

    public String getBegin_date_4() {
        return begin_date_4;
    }

    public void setBegin_date_4(String begin_date_4) {
        this.begin_date_4 = begin_date_4;
    }

    public String getEnd_date_4() {
        return end_date_4;
    }

    public void setEnd_date_4(String end_date_4) {
        this.end_date_4 = end_date_4;
    }

    public String getDrug_name_5() {
        return drug_name_5;
    }

    public void setDrug_name_5(String drug_name_5) {
        this.drug_name_5 = drug_name_5;
    }

    public String getDrug_desc_5() {
        return drug_desc_5;
    }

    public void setDrug_desc_5(String drug_desc_5) {
        this.drug_desc_5 = drug_desc_5;
    }

    public String getBegin_date_5() {
        return begin_date_5;
    }

    public void setBegin_date_5(String begin_date_5) {
        this.begin_date_5 = begin_date_5;
    }

    public String getEnd_date_5() {
        return end_date_5;
    }

    public void setEnd_date_5(String end_date_5) {
        this.end_date_5 = end_date_5;
    }
}
