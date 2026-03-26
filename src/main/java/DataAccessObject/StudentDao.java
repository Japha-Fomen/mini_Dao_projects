package DataAccessObject;

import Objects.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    public void insert(int id,String name,int age) throws SQLException {
        String query = "insert into students(id,name,age) values (?,?,?)";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null;
        assert pstmt != null;
        pstmt.setInt(1,id);
        pstmt.setString(2,name);
        pstmt.setInt(3,age);
        pstmt.executeUpdate();
    }
    public void delete(int id){
        String query = "delete from students where id=?";
        Connection conn=DataBaseConnection.getConnection();
        try {
            assert conn != null;
            PreparedStatement ptmt=conn.prepareStatement(query);
            ptmt.setInt(1,id);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Student find(int id){
        String query = "select * from students where id=?";
        Connection conn=DataBaseConnection.getConnection();
        try {
            assert conn != null;
            PreparedStatement ptmt=conn.prepareStatement(query);
            ptmt.setInt(1,id);
            ResultSet rs=ptmt.executeQuery();
            if(rs.next()){
                return new Student(rs.getInt("id"),rs.getString("name"),rs.getInt("age"));

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public List<Student> findAll(){
        List<Student> list=new ArrayList<>();
        String query = "select * from students";
        Connection conn=DataBaseConnection.getConnection();
        try {
            assert conn != null;
            PreparedStatement ptmt=conn.prepareStatement(query);
            ResultSet rs=ptmt.executeQuery();
            while(rs.next()){
                list.add(new Student(rs.getInt("id"),rs.getString("name"),rs.getInt("age")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  list;
    }

}
