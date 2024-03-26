package Models;

import DBConnection.DBConnection;

import java.sql.*;
import java.util.ArrayList;


public class ScheduleDAO {
    DBConnection dbConnection = new DBConnection();
    Connection connection = dbConnection.getConnection();
    public ArrayList<Schedule> getClassroom_StudentList(){
        ArrayList<Schedule> shedules = new ArrayList<Schedule>();
        String sql = "select cr.classroomId,cr.classroomName,cr.createDate,cr.teacherCode,cs.studentCode from classroom as cr, classroom_student as cs where cs.classroomId=cr.classroomId";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Integer classID = resultSet.getInt("classroomId");
                String classroomName =resultSet.getString("classroomName");
                Date createDate = resultSet.getDate("createDate");
                String teacherCode = resultSet.getString("teacherCode");
                String studentCode = resultSet.getString("studentCode");
                Schedule shedule = new Schedule(classID,classroomName,createDate,teacherCode,studentCode);
                shedules.add(shedule);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return shedules;
    }

    public ArrayList<Schedule> getSheduleWithFilter(String studentID, String Name, String teacherName){
        ArrayList<Schedule> sheduleArrayList = new ArrayList<Schedule>();
        String sql = "select t.classroomId, t.classroomName, t.createDate, t.teacherCode, t.studentCode from (select cs.classroomId as 'classroomId', cr.classroomName as 'classroomName', cr.createDate as 'createDate', cr.teacherCode as 'teacherCode',cs.studentCode as 'studentCode' from classroom_student as cs, classroom as cr where cs.classroomId = cr.classroomId) as t ";
        if (studentID.isEmpty()==false || Name.isEmpty()==false || teacherName.isEmpty()==false){
            sql +=" where ";
        }
        if (studentID.isEmpty()==false){
            sql +="t.studentCode like '%"+studentID+"%'";
        }
        if (studentID.isEmpty()==false && Name.isEmpty()==false){
            sql +=" and ";
        }
        if (Name.isEmpty()==false){
            sql +="t.classroomName like '%"+Name+"%'";
        }
        if (Name.isEmpty()==false && teacherName.isEmpty()==false){
            sql +=" and ";
        }
        if (teacherName.isEmpty()==false){
            sql +="t.teacherCode like '%"+teacherName+"%'";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Integer classID = resultSet.getInt("classroomId");
                String classroomName =resultSet.getString("classroomName");
                Date createDate = resultSet.getDate("createDate");
                String teacherCode = resultSet.getString("teacherCode");
                String studentCode = resultSet.getString("studentCode");
                Schedule shedule = new Schedule(classID,classroomName,createDate,teacherCode,studentCode);
                sheduleArrayList.add(shedule);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sheduleArrayList;
    }
    public boolean insert(Classroom_Student cs){

        String sql = "insert into classroom_student (classroomId,studentCode) values (?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,cs.getClassroomId());
            preparedStatement.setString(2,cs.getStudentCode());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Integer cs){
        String sql="delete from classroom_student where classroomId = '"+cs+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Classroom_Student cs, String classroomId_old, String studentCode_old){
        String sql ="update classroom_student set classroomId=?,studentCode='"+ cs.getStudentCode() +"' where classroomId = " + classroomId_old + " and studentCode = '" + studentCode_old  +"'";
        System.out.println(sql);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,cs.getClassroomId());
//            preparedStatement.setString(2,cs.getStudentCode());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean checkClassroom_StudentExist(String classroomId,String studentCode){
        String sql ="select count(*) from classroom_student where classroomId = '" + classroomId +"' and studentCode = '" + studentCode + "'";
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

    public boolean checkClassroomIdExist(String classroomId){
        String sql ="select count(*) from classroom where classroomId = " + Integer.valueOf(classroomId);
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

    public boolean checkStudentCodeExist(String studentCode){
        String sql ="select count(*) from student where studentCode = '" + studentCode+"'";
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

    public ArrayList<Student> getStudent(String classroomId){
        ArrayList<Student> studentArrayList = new ArrayList<Student>();
        String sql = "select st.studentCode 'studentCode',st.fullName 'fullName', st.dateOfBirth 'dateOfBirth', st.address 'address', st.classCode 'classCode', st.base64Image 'base64Image'  from classroom_student c, student st where st.studentCode = c.studentCode and c.classroomId ="  + classroomId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String studentCode = resultSet.getString("studentCode");
                String fullName = resultSet.getString("fullName");
                Date dateOfBirth = Date.valueOf(resultSet.getString("dateOfBirth"));
                String address = resultSet.getString("address");
                String classCode = resultSet.getString("classCode");
                String base64Image = resultSet.getString("base64Image");
                Student student = new Student(studentCode, fullName, dateOfBirth, address, classCode,base64Image);
                studentArrayList.add(student);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return studentArrayList;
    }
}
