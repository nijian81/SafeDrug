package cn.com.phhc.drugSafeApp.utils;

/**
 * Created by lenovo on 2015/4/14.
 * 药品信息实体
 */
public class DrugInfoTable {
    String guideDrugID;
    String userID;
    String memberID;
    String drugID;
    String drugName;
    String beginDate;
    String endDate;
    String note;
    String updateTime;
    String updateState;

    public DrugInfoTable() {
    }

    public String getGuideDrugID() {
        return guideDrugID;
    }

    public void setGuideDrugID(String guideDrugID) {
        this.guideDrugID = guideDrugID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getDrugID() {
        return drugID;
    }

    public void setDrugID(String drugID) {
        this.drugID = drugID;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateState() {
        return updateState;
    }

    public void setUpdateState(String updateState) {
        this.updateState = updateState;
    }

    public DrugInfoTable(String guideDrugID, String userID, String memberID, String drugID, String drugName, String beginDate, String endDate, String note, String updateTime, String updateState) {
        this.guideDrugID = guideDrugID;
        this.userID = userID;
        this.memberID = memberID;
        this.drugID = drugID;
        this.drugName = drugName;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.note = note;
        this.updateTime = updateTime;
        this.updateState = updateState;
    }
}
