package Models;

import DBConnection.DBConnection;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class AccountDAO {
    DBConnection dbConnection = new DBConnection();
    Connection connection = dbConnection.getConnection();

    public ArrayList<Account> getAccountList (){
            ArrayList<Account> AccountList = new ArrayList<Account>();
            String sql = "SELECT * FROM account" ;
            try{
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    Integer accountID = resultSet.getInt("accountId");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String teacherCode = resultSet.getString("teacherCode");
                    Integer typeAccount = resultSet.getInt("typeAccount");
                    Account account = new Account(accountID,username,password,teacherCode,typeAccount);
                    AccountList.add(account);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return AccountList;
    }

    public ArrayList<Account> getAccountListFilter(String cbbType,String username,String code){
        ArrayList<Account> accountArrayList = new ArrayList<Account>();
        String sql = "SELECT * FROM account";
        if (cbbType.isEmpty()==false || username.isEmpty()==false || code.isEmpty()==false){
            sql +=" where ";
        }
        if (cbbType.isEmpty()==false){
            sql +="typeAccount = '"+Integer.valueOf(cbbType)+"'";
        }
        if (cbbType.isEmpty()==false && username.isEmpty()==false){
            sql +=" and ";
        }
        if (username.isEmpty()==false){
            sql +="username like '%"+username+"%'";
        }
        if (username.isEmpty()==false && code.isEmpty()==false){
            sql += " and ";
        }
        if (code.isEmpty()==false){
            sql += "teacherCode like '%"+code+"%'";
        }
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Integer accountID = resultSet.getInt("accountId");
                String name = resultSet.getString("username");
                String password = resultSet.getString("password");
                String teacherCode = resultSet.getString("teacherCode");
                Integer typeAccount = resultSet.getInt("typeAccount");
                Account account = new Account(accountID,name,password,teacherCode,typeAccount);
                accountArrayList.add(account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountArrayList;
    }
    public int validateLogin(String username, String password){
        String sql = "SELECT * FROM ACCOUNT WHERE username = '" +username + "' AND password = '" +getMd5(password) + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(sql);
            while (queryResult.next()){
                if(queryResult.getInt("typeAccount") == 1){
                    return 1;
                }else {
                    return 0;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public static String getMd5(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Account getUserName(String userName){
        String query = "select * from account where username = '" +userName +"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                Integer accountId = rs.getInt("accountId");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String teacherCode = rs.getString("teacherCode");
                Integer typeAccount = rs.getInt("typeAccount");
                Account account = new Account(accountId,username,password,teacherCode,typeAccount);
                return account;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean insertAccount(Account account){

        String sql = "insert into account (username,password,teacherCode,typeAccount) values (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,getMd5(account.getPassword()));
            preparedStatement.setString(3,account.getTeacherCode());
            preparedStatement.setInt(4,account.getType());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Account account){
        String sql = "delete from account where accountId='"+account.getAccountId()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Account account){
        if(!account.password.isEmpty()){
            String sql="update account set accountId=?,username=?,password=?,teacherCode=?,typeAccount=? where accountId='"+account.getAccountId()+"'";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,account.getAccountId());
                preparedStatement.setString(2,account.getUsername());
                preparedStatement.setString(3,getMd5(account.getPassword()));
                preparedStatement.setString(4,account.getTeacherCode());
                preparedStatement.setInt(5,account.getType());
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
        }else {
            String sql="update account set accountId=?,username=?,teacherCode=?,typeAccount=? where accountId='"+account.getAccountId()+"'";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,account.getAccountId());
                preparedStatement.setString(2,account.getUsername());
                preparedStatement.setString(3,account.getTeacherCode());
                preparedStatement.setInt(4,account.getType());
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
        }
        return true;
    }
    public boolean checkUsernameExist(String username){
        String sql ="select count(*) from account where username = '"+username+"'" ;
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getInt(1)>0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean checkTeacherCodeExist(String ID){
        String sql ="select count(*) from account where teacherCode = '"+ID+"'";
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getInt(1)>0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean changePassword(int accountId, String newPassword){
        String sql="update account set password=? where accountId='"+accountId+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,getMd5(newPassword));
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

}
