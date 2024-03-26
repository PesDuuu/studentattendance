package Models;

import DBConnection.DBConnection;

import java.sql.*;

public class AttendanceSessionDAO {
    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getConnection();

    public int countNumberOfSession(int classroomId){
        String sql ="select count(*) from attendancesession where classroomId = "+classroomId;
        try {
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
        return 0;
    }

    public boolean insertNumberSessionForClass(String classroomId){
        int numberSession = countNumberOfSession(Integer.valueOf(classroomId)) + 1;
        String query = "INSERT INTO attendancesession (numberSession, classroomId) VALUES(?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(numberSession));
            preparedStatement.setString(2, classroomId);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public int getIdByNumberSessionAndClassroomId(String NumberSession, String classroomId){
        String sql ="select attendanceSessionId from attendancesession where classroomId = "+classroomId + " and numberSession = " + NumberSession ;
        try {
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
        return 0;
    }

}
