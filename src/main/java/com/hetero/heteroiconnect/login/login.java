package com.hetero.heteroiconnect.login;

public class login {
    String userName;
    String password;
    long unitId;
    long cashCounterId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public long getCashCounterId() {
        return cashCounterId;
    }

    public void setCashCounterId(long cashCounterId) {
        this.cashCounterId = cashCounterId;
    }

    @Override
    public String toString() {
        return "login{" + "userName='" + userName + '\'' + ", password='" + password + '\'' + ", unitId=" + unitId + ", cashCounterId=" + cashCounterId + '}';
    }

}
