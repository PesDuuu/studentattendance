package Models;

import DBConnection.DBConnection;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AttendanceSessionDetailsDAO {
    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getConnection();

    public ArrayList<StudentAttendance> getStudentAttendanceList(int attendanceSessionId) {
        ArrayList<StudentAttendance> students = new ArrayList<StudentAttendance>();
        String sql = "select a.studentCode 'studentCode', st.fullName 'fullName', a.timeCheckIn 'timeCheckIn', a.base64Image 'base64Image' from attendancesessiondetails as a, student as st where a.studentCode = st.studentCode and a.attendanceSessionId =" + attendanceSessionId + " order by a.timeCheckIn desc ";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String studentCode = resultSet.getString("studentCode");
                String fullName = resultSet.getString("fullName");
                Timestamp timeCheckIn = resultSet.getTimestamp("timeCheckIn");
                String base64Image = resultSet.getString("base64Image");
                StudentAttendance student = new StudentAttendance(studentCode, fullName, timeCheckIn, base64Image);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public ArrayList<StudentAttendance> getStudentAttendanceListByStudentCode(int attendanceSessionId, String txtStudentCode) {
        ArrayList<StudentAttendance> students = new ArrayList<StudentAttendance>();
        String sql = "select a.studentCode 'studentCode', st.fullName 'fullName', a.timeCheckIn 'timeCheckIn', a.base64Image 'base64Image' from attendancesessiondetails as a, student as st where a.studentCode = st.studentCode and a.attendanceSessionId = " + attendanceSessionId +" and a.studentCode like '%" +txtStudentCode + "%' order by a.timeCheckIn desc ";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String studentCode = resultSet.getString("studentCode");
                String fullName = resultSet.getString("fullName");
                Timestamp timeCheckIn = resultSet.getTimestamp("timeCheckIn");
                String base64Image = resultSet.getString("base64Image");
                StudentAttendance student = new StudentAttendance(studentCode, fullName, timeCheckIn, base64Image);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean checkExistStudentInAttendanceSessionDetails(int attendanceSessionId,String studentCode){
        String sql ="select count(*) from attendancesessiondetails where attendanceSessionId = '" + attendanceSessionId +"' and studentCode = '" + studentCode + "'";
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

    public boolean insertStudent(int attendanceSessionId, String studentCode){
        String query = "INSERT INTO attendancesessiondetails (attendanceSessionId, studentCode) VALUES(?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, attendanceSessionId);
            preparedStatement.setString(2, studentCode);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteStudent(int attendanceSessionId, String studentCode){
        String query = "delete from attendancesessiondetails where attendanceSessionId = "+ attendanceSessionId +" and studentCode = '" + studentCode  +"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public String getBase64ImageFromAtt(int attendanceSessionId, String studentCode){
        String sql ="select base64Image from attendancesessiondetails where attendanceSessionId = '" + attendanceSessionId +"' and studentCode = '" + studentCode + "'";
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "";
        }
        return "";
    }

    public boolean insertStudentAtt(int attendanceSessionId, String studentCode,String base64Image){
        String query = "INSERT INTO attendancesessiondetails (attendanceSessionId, studentCode, base64Image) VALUES(?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, attendanceSessionId);
            preparedStatement.setString(2, studentCode);
            preparedStatement.setString(3, base64Image);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
