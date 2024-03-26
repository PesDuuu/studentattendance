package Models;

import DBConnection.DBConnection;
import com.mysql.cj.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.ARETURN;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class FacultyDAO {

    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getConnection();

    public ArrayList<Faculty> getFacultyList() {
        ArrayList<Faculty> facultiesList = new ArrayList<Faculty>();
        String sql = "select * from faculty";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String facultyCode = resultSet.getString("facultyCode");
                String facultyName = resultSet.getString("facultyName");
                Faculty faculty = new Faculty(facultyCode, facultyName);
                facultiesList.add(faculty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facultiesList;
    }

    public String getFacultyCodeByFacultyName(String FacultyName) {
        String sql = "select facultyCode from faculty where facultyName like '" + FacultyName + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getString("facultyCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public ArrayList<Faculty> facultyArrayListWithFilter(String facultyCodeWithFilter, String facultyNameWithFilter) {
        ArrayList<Faculty> faculties = new ArrayList<Faculty>();
        String sql = "select * from faculty";
        if (facultyCodeWithFilter.isEmpty() == false || facultyNameWithFilter.isEmpty() == false) {
            sql += " where ";
        }
        if (facultyCodeWithFilter.isEmpty() == false) {
            sql += "facultyCode like '%" + facultyCodeWithFilter + "%'";
        }
        if (facultyCodeWithFilter.isEmpty() == false && facultyNameWithFilter.isEmpty() == false) {
            sql += " and ";
        }
        if (facultyNameWithFilter.isEmpty() == false) {
            sql += "facultyName like '%" + facultyNameWithFilter + "%'";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String facultyCode = rs.getString("facultyCode");
                String facultyName = rs.getString("facultyName");
                Faculty faculty = new Faculty(facultyCode, facultyName);
                faculties.add(faculty);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return faculties;
    }

    public boolean insert(Faculty faculty) {
        String sql = "insert into faculty (facultyCode,facultyName) values (?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, faculty.getFacultyCode());
            preparedStatement.setString(2, faculty.getFacultyName());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Faculty faculty) {
        String sql = "delete from faculty where facultyCode = '" + faculty.getFacultyCode() + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Faculty faculty) {

        String sql = "update faculty set facultyCode=?, facultyName=? where facultyCode ='" + faculty.getFacultyCode() + "'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, faculty.getFacultyCode());
            preparedStatement.setString(2, faculty.getFacultyName());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public String getFacultyNameByClassCode(String classCode) {
        String sql = "select * from faculty where facultyCode=(select facultyCode from class where classCode='" + classCode + "')";
        try {
            Statement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getString("facultyName");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getFacultyName(String ID) {
        String sql = "select * from faculty where facultyCode = '" + ID + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getString("facultyName");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getFacultyNameByFacultyCode(String FacultyCode) {
        String sql = "select facultyName from faculty where facultyCode like '" + FacultyCode + "'";
//        System.out.println(sql);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getString("facultyName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean checkFacultyCodeExit(String facultyCode) {
        String sql = "select count(*) from class where facultyCode='" + facultyCode + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean checkFacultyIDExit(String ID) {
        String sql = "select count(*) from faculty where facultyCode='" +ID+ "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }
}
