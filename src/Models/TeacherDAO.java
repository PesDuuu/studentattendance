package Models;

import DBConnection.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class TeacherDAO {
    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getConnection();
    public ArrayList<Teacher> getTeacherList() {
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();
        DBConnection DBConnection = new DBConnection();
        Connection connection = DBConnection.getConnection();
        String sql = "select * from teacher";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String teacherCode = resultSet.getString("teacherCode");
                String fullName = resultSet.getString("fullName");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phoneNumber");
                Teacher teacher = new Teacher(teacherCode, fullName, email , phoneNumber);
                teachers.add(teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teachers;
    }

    public Teacher getTeacherInfo(String teacherName){
        DBConnection DBConnection = new DBConnection();
        Connection connection = DBConnection.getConnection();
        String sql = "SELECT * FROM teacher WHERE teacherCode = (SELECT teacherCode FROM ACCOUNT WHERE username= '" + teacherName +"')";
        try {
            Statement statement =connection.createStatement();
            ResultSet query = statement.executeQuery(sql);
            while (query.next()){
                String teacherCode = query.getString("teacherCode");
                String fullName = query.getString("fullName");
                String email = query.getString("email");
                String phoneNumber = query.getString("phoneNumber");
                Teacher teacher = new Teacher(teacherCode,fullName,email,phoneNumber);
                return teacher;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean insertTeacher(Teacher teacher){

        String sql = "insert into teacher (teacherCode,fullName,email,phoneNumber) values (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,teacher.getTeacherCode());
            preparedStatement.setString(2,teacher.getFullName());
            preparedStatement.setString(3,teacher.getEmail());
            preparedStatement.setString(4,teacher.getPhoneNumber());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Teacher teacher){
        String sql = "delete from teacher where teacherCode = '" + teacher.getTeacherCode() + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Teacher teacher){

        String sql = "update teacher set teacherCode=? ,fullname=?,email=?,phoneNumber=? where teacherCode='"+teacher.getTeacherCode()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,teacher.getTeacherCode());
            preparedStatement.setString(2,teacher.getFullName());
            preparedStatement.setString(3,teacher.getEmail());
            preparedStatement.setString(4,teacher.getPhoneNumber());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    public ArrayList<Teacher> getTeacherListWithFilter(String teacherCodeWithFilter,String teacherNameWithFilter){
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();
        String sql = "select * from teacher";
        if (teacherCodeWithFilter.isEmpty()==false || teacherNameWithFilter.isEmpty()==false){
            sql +=" where ";
        }
        if (teacherCodeWithFilter.isEmpty()==false){
            sql +="teacherCode like '%"+teacherCodeWithFilter+"%'";
        }
        if (teacherCodeWithFilter.isEmpty()==false && teacherNameWithFilter.isEmpty()==false){
            sql +=" and ";
        }
        if (teacherNameWithFilter.isEmpty()==false){
            sql +="fullName like '%"+teacherNameWithFilter+"%'";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                String teacherID = rs.getString("teacherCode");
                String fullName = rs.getString("fullName");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                Teacher teacher = new Teacher(teacherID,fullName,email,phoneNumber);
                teachers.add(teacher);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teachers;
    }

    public boolean checkTeacherCodeExist(String ID){
        String sql = "select count(*) from classroom where teacherCode ='"+ID+"'";
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

    public boolean checkTeacherExit(String teacherCode){
        String sql = "select count(*) from teacher where teacherCode ='"+teacherCode+"'";
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
}
