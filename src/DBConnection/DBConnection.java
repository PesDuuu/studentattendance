package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection connection;

    public Connection getConnection(){
        String dbName = "studentattendance";
        String userName = "root";
        String password = "1234";
        try{
            Class.class.forName("com.mysql.cj.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentattendance?useUnicode=true&character_set_server=utf8mb4");
            connection =  DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,userName,password);
        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}
