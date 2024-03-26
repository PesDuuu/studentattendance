package Models;

import DBConnection.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ClassDAO {
    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getConnection();
    public ArrayList<Class> getClassList(){
        ArrayList<Class> classList = new ArrayList<Class>();

        DBConnection DBConnection = new DBConnection();
        Connection connection = DBConnection.getConnection();
        String sql = "select classCode,className,facultyName from class as c, faculty as f where c.facultyCode = f.facultyCode";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String classCode = resultSet.getString("classCode");
                String className = resultSet.getString("className");
                String facultyName = resultSet.getString("facultyName");
                Class iClass = new Class(classCode,className,facultyName);
                System.out.println();
                classList.add(iClass);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }
    public boolean insertClass(Class iclass){
        String sql = "INSERT INTO class (classCode, className, facultyCode) VALUES (?,?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,iclass.getClassCode());
            preparedStatement.setString(2,iclass.getClassName());
            preparedStatement.setString(3,iclass.getFacultyCode());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Class iclass){
        String sql = "delete from class where classCode='"+ iclass.getClassCode()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Class iclass){
        String sql  ="update class set classCode=?,className=?,facultyCode=? where classCode='"+iclass.getClassCode()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,iclass.getClassCode());
            preparedStatement.setString(2,iclass.getClassName());
            preparedStatement.setString(3,iclass.getFacultyCode());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    public ArrayList<Class> getClassListWithClassFilter(String classCodeWithFilter,String facultyNameWithFiler){
        ArrayList<Class> classes = new ArrayList<Class>();
        String sql = "select classCode,className,facultyName from class as c,faculty as f where c.facultyCode = f.facultyCode";
        if (classCodeWithFilter.isEmpty()==false||facultyNameWithFiler.isEmpty()==false){
            sql +=" and ";
        }
        if (facultyNameWithFiler.isEmpty()==false){
            sql +="facultyName='"+facultyNameWithFiler+"'";
        }
        if (classCodeWithFilter.isEmpty()==false && facultyNameWithFiler.isEmpty()==false){
            sql +=" and ";
        }
        if (classCodeWithFilter.isEmpty()==false){
            sql +=" classCode like '%"+ classCodeWithFilter +"%'";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                String ID = rs.getString("classCode");
                String Name = rs.getString("className");
                String faculty = rs.getString("facultyName");
                Class iclass = new Class(ID,Name,faculty);
                classes.add(iclass);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classes;
    }

    public boolean checkExistClassCode(String ID){
        String sql = "select count(*) from student where classCode = '" +ID+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getInt(1)> 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean checkExistClass(String classCode){
        String sql = "select count(*) from class where classCode = '" +classCode+"'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getInt(1)> 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
