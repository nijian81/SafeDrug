package cn.com.phhc.drugSafeApp.utils;

/**
 * 保存添加药品信息的实体信息 尼见 2015-03-23
 */
public class AddDrugInfo {

    String basicDrugID;
    String productDrugID;
    String drugName;
    String cycleDays;          //用药频率中的天数
    String cycleCount;         //用药频率
    String intervalValue;      //用药间隔
    String intervalUnit;       //间隔单位
    String begin;              //服药开始日期
    String end;                //服药结束日期
    String drugCount;          //服用剂量
    String drugCountUnit;       //服用剂量单位ID
    String remark;             //备注

    public AddDrugInfo() {
    }

    public AddDrugInfo(String basicDrugID, String productDrugID, String drugName, String cycleDays, String cycleCount, String intervalValue, String intervalUnit, String begin, String end, String drugCount, String drugCountUnit, String remark) {
        this.basicDrugID = basicDrugID;
        this.productDrugID = productDrugID;
        this.drugName = drugName;
        this.cycleDays = cycleDays;
        this.cycleCount = cycleCount;
        this.intervalValue = intervalValue;
        this.intervalUnit = intervalUnit;
        this.begin = begin;
        this.end = end;
        this.drugCount = drugCount;
        this.drugCountUnit = drugCountUnit;
        this.remark = remark;
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
}
