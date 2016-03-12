package cn.com.phhc.drugSafeApp.utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lenovo on 2015/4/1.
 * 生成用药时间的列表实体,这种用于传参的实体,必须调用implements serilizable接口，不然传值会有问题，
 * 有可能在添加药品页面时，按下主界面时，应用会闪退。
 */
public class GenerateTakeDrugTimeList implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int location;
    String basicDrugID;
    String productDrugID;
    String guideDrugID;
    String tempDrugID;
    String drugName;
    String cycleDays;
    String cycleCount;      //drugFrequency
    String intervalValue;   //drugInterval
    String intervalUnit;
    String isUsing;
    String begin;
    String end;
    String drugCount;       //drugDose
    String drugCountUnit;   //drugDoseUnit
    String remark;
    String statusCode;
    ArrayList<TakeTimeList> takeTimeList;

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getBasicDrugID() {
        return basicDrugID;
    }

    public void setBasicDrugID(String basicDrugID) {
        this.basicDrugID = basicDrugID;
    }

    public String getProductDrugID() {
        return productDrugID;
    }

    public void setProductDrugID(String productDrugID) {
        this.productDrugID = productDrugID;
    }

    public String getGuideDrugID() {
        return guideDrugID;
    }

    public void setGuideDrugID(String guideDrugID) {
        this.guideDrugID = guideDrugID;
    }

    public String getTempDrugID() {
        return tempDrugID;
    }

    public void setTempDrugID(String tempDrugID) {
        this.tempDrugID = tempDrugID;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getCycleDays() {
        return cycleDays;
    }

    public void setCycleDays(String cycleDays) {
        this.cycleDays = cycleDays;
    }

    public String getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(String cycleCount) {
        this.cycleCount = cycleCount;
    }

    public String getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(String intervalValue) {
        this.intervalValue = intervalValue;
    }

    public String getIntervalUnit() {
        return intervalUnit;
    }

    public void setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit;
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDrugCount() {
        return drugCount;
    }

    public void setDrugCount(String drugCount) {
        this.drugCount = drugCount;
    }

    public String getDrugCountUnit() {
        return drugCountUnit;
    }

    public void setDrugCountUnit(String drugCountUnit) {
        this.drugCountUnit = drugCountUnit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<TakeTimeList> getTakeTimeList() {
        return takeTimeList;
    }

    public void setTakeTimeList(ArrayList<TakeTimeList> takeTimeList) {
        this.takeTimeList = takeTimeList;
    }

    public GenerateTakeDrugTimeList() {
    }

    public GenerateTakeDrugTimeList(int location, String basicDrugID, String productDrugID, String guideDrugID, String tempDrugID, String drugName, String cycleDays, String cycleCount, String intervalValue, String intervalUnit, String isUsing, String begin, String end, String drugCount, String drugCountUnit, String remark, String statusCode, ArrayList<TakeTimeList> takeTimeList) {
        this.location = location;
        this.basicDrugID = basicDrugID;
        this.productDrugID = productDrugID;
        this.guideDrugID = guideDrugID;
        this.tempDrugID = tempDrugID;
        this.drugName = drugName;
        this.cycleDays = cycleDays;
        this.cycleCount = cycleCount;
        this.intervalValue = intervalValue;
        this.intervalUnit = intervalUnit;
        this.isUsing = isUsing;
        this.begin = begin;
        this.end = end;
        this.drugCount = drugCount;
        this.drugCountUnit = drugCountUnit;
        this.remark = remark;
        this.statusCode = statusCode;
        this.takeTimeList = takeTimeList;
    }

}
