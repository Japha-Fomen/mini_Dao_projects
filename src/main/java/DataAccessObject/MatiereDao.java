package DataAccessObject;
import Objects.Matiere;

import javax.management.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MatiereDao {
public void insert(Matiere matiere){
    String query="insert into Matiere(Idmatiere,nom,Description) values(?,?,?)";
    Connection conn=DataBaseConnection.getConnection();
    assert conn != null;
    try {
        PreparedStatement pst= conn.prepareStatement(query);
        pst.setString(1, matiere.getIdMatiere());
        pst.setString(2,matiere.getNameMatiere());
        pst.setString(3,matiere.getDescription());
        pst.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
public void insert(String IdMatiere,String nom,String description){
    Connection conn=DataBaseConnection.getConnection();
    String query="insert into Matiere values(?,?,?)";
    try {
        PreparedStatement pstmt=conn != null ? conn.prepareStatement(query) : null;
        assert pstmt != null;
        pstmt.setString(1,IdMatiere);
        pstmt.setString(2,nom);
        pstmt.setString(3,description);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
public Matiere findMatiere(String id){
    Connection conn=DataBaseConnection.getConnection();
    String query="select * from Matiere where id=?";
    try {
        assert conn != null;
        PreparedStatement pstmt=conn.prepareStatement(query);
        pstmt.setString(1,id);
        ResultSet rs=pstmt.executeQuery();
        if(rs.next()) return new Matiere(rs.getString("IdMatiere"),rs.getString("nom"),rs.getString("Description"));
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return null;
}
public List<Matiere> findAll(){
    Connection conn=DataBaseConnection.getConnection();
    String query="select * from Matiere";
    List<Matiere> matieres=new ArrayList<>();
    try {
        assert conn != null;
        PreparedStatement pstmt=conn.prepareStatement(query);
        ResultSet rs=pstmt.executeQuery();
        while(rs.next()){
            matieres.add(new Matiere(rs.getString("id"),rs.getString("nom"),rs.getString("description")));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return matieres;
}
public void delete(String id){
    Connection conn=DataBaseConnection.getConnection();
    String query="delete from Matiere where id=?";
    try {
        assert conn != null;
        PreparedStatement pstmt=conn.prepareStatement(query);
        pstmt.setString(1,id);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}
}
