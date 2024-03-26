package Models;

public class Account {
    int accountId;
    String username;
    String password;
    String teacherCode;
    int type;

    public Account() {
    }

    public Account(int accountId, String username, String password, String teacherCode, int type) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.teacherCode = teacherCode;
        this.type = type;
    }

    public Account( String username, String password, String teacherCode, int type) {
        this.username = username;
        this.password = password;
        this.teacherCode = teacherCode;
        this.type = type;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

