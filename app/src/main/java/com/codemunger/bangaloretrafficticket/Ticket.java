package com.codemunger.bangaloretrafficticket;

import java.util.Date;

/**
 * Created by android on 2/22/16.
 */
public class Ticket {

    private String mViolationType;
    private String mAmount;
    private String mDate;
    private String mNotice;


    public String getViolationType() {
        return mViolationType;
    }

    public void setViolationType(String violationType) {
        mViolationType = violationType;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getNotice() {
        return mNotice;
    }

    public void setNotice(String notice) {
        mNotice = notice;
    }
}
