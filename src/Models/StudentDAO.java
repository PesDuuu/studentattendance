package Models;

import DBConnection.DBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StudentDAO {
    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getConnection();

    public ArrayList<Student> getStudentList() {
        ArrayList<Student> students = new ArrayList<Student>();
        String sql = "select * from student";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String studentCode = resultSet.getString("studentCode");
                String fullName = resultSet.getString("fullName");
                Date dateOfBirth = Date.valueOf(resultSet.getString("dateOfBirth"));
                String address = resultSet.getString("address");
                String classCode = resultSet.getString("classCode");
                String base64Image = resultSet.getString("base64Image");
                Student student = new Student(studentCode, fullName, dateOfBirth, address, classCode,base64Image);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public ArrayList<Student> getStudentListByRoomID(int ID){
        ArrayList<Student> students = new ArrayList<Student>();
        String sql = "select s.studentCode, s.fullName,s.dateOfBirth,s.address,s.classCode from student as s, classroom_student as cs where s.studentCode = cs.studentCode and cs.classroomId = '"+ID+"'";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String studentCode = resultSet.getString("studentCode");
                String fullName = resultSet.getString("fullName");
                Date dateOfBirth = Date.valueOf(resultSet.getString("dateOfBirth"));
                String address = resultSet.getString("address");
                String classCode = resultSet.getString("classCode");
                Student student = new Student(studentCode, fullName, dateOfBirth, address, classCode);
                students.add(student);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return students;
    }

    public boolean insertStudent(Student student){
        String query = "INSERT INTO student (studentCode,fullName,dateOfBirth,address,classCode,base64Image) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,student.getStudentCode());
            preparedStatement.setString(2,student.getFullName());
            preparedStatement.setDate(3,student.getDateOfBirth());
            preparedStatement.setString(4,student.getAddress());
            preparedStatement.setString(5,student.getClassCode());
            preparedStatement.setString(6,student.getBase64Image());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean update(Student student){
        String query = "update student set studentCode=?,fullName=?,dateOfBirth=?,address=?,classCode=?,base64Image=? where studentCode ='"+student.getStudentCode()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,student.getStudentCode());
            preparedStatement.setString(2,student.getFullName());
            preparedStatement.setDate(3,student.getDateOfBirth());
            preparedStatement.setString(4,student.getAddress());
            preparedStatement.setString(5,student.getClassCode());
            preparedStatement.setString(6,student.getBase64Image());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean delete(Student student){
        String sql = "delete from student where studentCode = '" + student.getStudentCode() + "' and classCode = '"+student.getClassCode()+"'";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
    public Student getStudentByID(String idStudent){
        String sql ="select * from student where studentCode = "+ idStudent  ;
        try {
            Statement statement =connection.createStatement();
            ResultSet query = statement.executeQuery(sql);
            while (query.next()){
                String studentCode =(query.getString("studentCode"));
                String studentName = query.getString("fullName");
                Date dateOfBirth = Date.valueOf(query.getString("dateOfBirth"));
                String address = query.getString("address");
                String classCode = query.getString("classCode");
                String base64Image = query.getString("base64Image");
                return new Student(studentCode,studentName,dateOfBirth,address,classCode,base64Image);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Student> getStudentListWithFilter(String classCodeWithFilter, String FullNameWithFilter, String StudentCodeWithFilter) {
        ArrayList<Student> students = new ArrayList<Student>();
//        String sql = "select * from student ";
//
//        if(classCodeWithFilter.isEmpty() == false || StudentCodeWithFilter.isEmpty() == false || FullNameWithFilter.isEmpty() == false){
//            sql+= "where";
//        }
//
//        if(classCodeWithFilter.isEmpty() == false){
//            sql += " classCode = '" + classCodeWithFilter + "'";
//        }
//        if(classCodeWithFilter.isEmpty() == false && FullNameWithFilter.isEmpty() == false){
//            sql += " and ";
//        }
//
//        if(FullNameWithFilter.isEmpty() == false){
//            sql += " fullName  like '%"+ FullNameWithFilter +"%'";
//        }
//
//        if (FullNameWithFilter.isEmpty()==false || classCodeWithFilter.isEmpty()==false){
//            sql +=" and ";
//        }
//        if(StudentCodeWithFilter.isEmpty() == false){
//            sql += " studentCode like '%"+ StudentCodeWithFilter +"%'";
//        }
        String sql = "select * from student where fullName like '%"+ FullNameWithFilter +"%' and studentCode like '%"+ StudentCodeWithFilter +"%' and classCode like '%"+ classCodeWithFilter +"%'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String studentCode = resultSet.getString("studentCode");
                String fullName = resultSet.getString("fullName");
                Date dateOfBirth = Date.valueOf(resultSet.getString("dateOfBirth"));
                String address = resultSet.getString("address");
                String classCode = resultSet.getString("classCode");
                Student student = new Student(studentCode, fullName, dateOfBirth, address, classCode);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean checkStudentCodeExit(String Code){
        String sql = "select count(*) from classroom_student where studentCode ='"+Code+"'";
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
    public boolean checkStudentIDExit(String ID){
        String sql = "select count(*) from student where studentCode ='"+ID+"'";
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
