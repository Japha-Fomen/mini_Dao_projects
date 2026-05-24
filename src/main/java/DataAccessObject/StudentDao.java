package DataAccessObject;

import Objects.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des étudiants dans la base de données.
 * Fournit les opérations CRUD : insertion, suppression, recherche et mise à jour.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class StudentDao {

    /**
     * Insère un étudiant dans la base en utilisant des paramètres simples.
     *
     * @param id identifiant de l'étudiant
     * @param name nom de l'étudiant
     * @param age âge de l'étudiant
     * @throws SQLException en cas d'erreur SQL
     */
    public void insert(int id, String name, int age) throws SQLException {
        String query = "insert into Student(id,name,age) values (?,?,?)";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null;

        assert pstmt != null; // Vérifie que la connexion est valide

        pstmt.setInt(1, id);
        pstmt.setString(2, name);
        pstmt.setInt(3, age);

        pstmt.executeUpdate();
    }

    /**
     * Insère un étudiant dans la base à partir d'un objet Student.
     *
     * @param student objet étudiant à insérer
     * @throws SQLException en cas d'erreur SQL
     */
    public void insert(Student student) throws SQLException {
        String query = "insert into Student(id,name,age) values (?,?,?)";
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null;

        assert pstmt != null;

        pstmt.setInt(1, student.getId());
        pstmt.setString(2, student.getName());
        pstmt.setInt(3, student.getAge());

        pstmt.executeUpdate();
    }

    /**
     * Supprime un étudiant selon son identifiant.
     *
     * @param id identifiant de l'étudiant à supprimer
     */
    public void delete(int id) {
        String query = "delete from Student where id=?";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, id);
            ptmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recherche un étudiant par son identifiant.
     *
     * @param id identifiant de l'étudiant
     * @return l'étudiant trouvé ou null si aucun résultat
     */
    public Student find(int id) {
        String query = "select * from student where id=?";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, id);

            ResultSet rs = ptmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Retourne la liste complète des étudiants présents dans la base.
     *
     * @return liste des étudiants
     */
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String query = "select * from student";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement ptmt = conn.prepareStatement(query);
            ResultSet rs = ptmt.executeQuery();

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    /**
     * Met à jour les informations d'un étudiant.
     * Vérifie d'abord que l'étudiant existe via find().
     *
     * @param student étudiant contenant les nouvelles valeurs
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Student student) {
        String query = "update student set name=?,age=? where id=?";
        Connection conn = DataBaseConnection.getConnection();

        try {
            assert conn != null;
            PreparedStatement ptmt = conn.prepareStatement(query);

            // Vérifie que l'étudiant existe avant de mettre à jour
            if (find(student.getId()) != null) {

                // Correction : la requête contient 3 paramètres, donc on en met 3
                ptmt.setString(1, student.getName());
                ptmt.setInt(2, student.getAge());
                ptmt.setInt(3, student.getId());

                ptmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}