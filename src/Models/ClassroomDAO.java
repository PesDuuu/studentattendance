package Models;

import DBConnection.DBConnection;
import com.sun.org.apache.bcel.internal.generic.ARETURN;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClassroomDAO {
    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getConnection();
    public Date convertStringToDate(String sDate) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(sDate);
        Date date = new Date(parsed.getTime());
        return date;
    }
    public ArrayList<Classroom> listClass(String teacherCodeOfClass){
        String sql = "Select * from classroom where teacherCode = '" + teacherCodeOfClass +"'";
        ArrayList<Classroom> list = new ArrayList<Classroom>();
        Classroom classroom = null;
        try {
            Statement statement =connection.createStatement();
            ResultSet query = statement.executeQuery(sql);
            while (query.next()){
                int classroomId = Integer.parseInt(query.getString("classroomId")) ;
                String classroomName = query.getString("classroomName");
                Date createDate = convertStringToDate(query.getString("createDate"));
                String teacherCode = query.getString("teacherCode");
                classroom = new Classroom(classroomId,classroomName,createDate,teacherCode);
                list.add(classroom);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public Classroom getClassroomById(int idClassroom){
        String sql ="select * from classroom where classroomId = "+ String.valueOf(idClassroom)  ;
        try {
            Statement statement =connection.createStatement();
            ResultSet query = statement.executeQuery(sql);
            while (query.next()){
                int classroomId = Integer.parseInt(query.getString("classroomId"));
                String classroomName = query.getString("classroomName");
                Date createDate = convertStringToDate(query.getString("createDate"));
                String teacherCode = query.getString("teacherCode");
                return new Classroom(classroomId,classroomName,createDate,teacherCode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList <Classroom> ClassroomList(){
        ArrayList<Classroom> classrooms = new ArrayList<Classroom>();
        String sql = "SELECT * from classroom";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Integer classroomId = resultSet.getInt("classroomId");
                String classroomName = resultSet.getString("classroomName");
                Date createDate = (resultSet.getDate("createDate"));
                String teacherCode = resultSet.getString("teacherCode");
                Classroom classroom = new Classroom(classroomId,classroomName,createDate,teacherCode);
                classrooms.add(classroom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classrooms;
    }

    public ArrayList<Classroom> getClassroomListFilter(String classroomNameWithFilter,String teacherCodeWithFilter){
        ArrayList<Classroom> classroomArrayList = new ArrayList<Classroom>();
        String sql = "select * from classroom";
        if (classroomNameWithFilter.isEmpty()==false || teacherCodeWithFilter.isEmpty()==false){
            sql += " where ";
        }
        if (classroomNameWithFilter.isEmpty()==false){
            sql +="classroomName like '%"+classroomNameWithFilter+"%'";
        }
        if (classroomNameWithFilter.isEmpty()==false && teacherCodeWithFilter.isEmpty()==false){
            sql +=" and ";
        }
        if (teacherCodeWithFilter.isEmpty()==false){
            sql +="teacherCode like '%"+teacherCodeWithFilter+"%'";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                Integer ID = rs.getInt("classroomId");
                String classroomName = rs.getString("classroomName");
                Date createDate = rs.getDate("createDate");
                String teacherCode = rs.getString("teacherCode");
                Classroom classroom = new Classroom(ID,classroomName,createDate,teacherCode);
                classroomArrayList.add(classroom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classroomArrayList;
    }

    public boolean insertClassroom(Classroom classroom){

        String sql = "insert into classroom (classroomName,createDate,teacherCode) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,classroom.getClassroomName());
            preparedStatement.setDate(2, (java.sql.Date) classroom.getCreateDate());
            preparedStatement.setString(3,classroom.getTeacherCode());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Classroom classroom){
        String sql = "delete from classroom where classroomId='"+classroom.getClassroomId()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean update(Classroom classroom){
        String sql ="update classroom set classroomId=?,classroomName=?,createDate=?,teacherCode=? where classroomId='"+classroom.getClassroomId()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,classroom.getClassroomId());
            preparedStatement.setString(2,classroom.getClassroomName());
            preparedStatement.setDate(3, (java.sql.Date) classroom.getCreateDate());
            preparedStatement.setString(4,classroom.getTeacherCode());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean checkClassroomIdExit(Integer ID){
        String sql ="select count(*) from classroom_student where classroomId = '"+ID+"'";
        try {
            Statement statement =connection.createStatement();
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

    public boolean checkClassroomExit(String classroomID){
        String sql ="select count(*) from classroom where classroomId = '"+Integer.valueOf(classroomID)+"'";
        try {
            Statement statement =connection.createStatement();
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

    public boolean checkClassroomExistAttendanceSession(Integer classroomID){
        String sql ="select count(*) from attendancesession where classroomId = '"+Integer.valueOf(classroomID)+"'";
        try {
            Statement statement =connection.createStatement();
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
