package DataAccessObject;
import Objects.Enseignant;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class EnseignantDao {

public void insert(Enseignant enseignant) {
    String sql="insert into enseignant(id,nom,email) values(?,?,?)";
    Connection conn=DataBaseConnection.getConnection();
    assert conn != null;
    try {
        PreparedStatement ps= conn.prepareStatement(sql);
        ps.setInt(1,enseignant.getId());
        ps.setString(2,enseignant.getNom());
        ps.setString(3,enseignant.getEmail());
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

}

    public Enseignant getEnseignantById(int id) {
        String sql = "SELECT * FROM Enseignant WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return new Enseignant(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email")
                    );
                }

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return null;
    }

    public List<Enseignant> getAllEnseignants() {
        List<Enseignant> list = new ArrayList<>();
        String sql = "SELECT * FROM Enseignant";

        try (Connection conn =  DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    list.add(new Enseignant(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email")
                    ));
                }

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return list;
    }


    public boolean updateEnseignant(Enseignant e) {
        String sql = "UPDATE Enseignant SET nom = ?, email = ? WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, e.getNom());

                ps.setString(2, e.getEmail());
                ps.setInt(3, e.getId());

                return ps.executeUpdate() > 0;

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erreur de connexion au serveur MySQL.\n" +
                            "impossible d'update.\n\n" +
                            "Détails : " + ex.getMessage(),
                    "Erreur de connexion",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;

        }
    }

    public boolean deleteEnseignant(int id) {
        String sql = "DELETE FROM Enseignant WHERE id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                return ps.executeUpdate() > 0;

            }
        } catch (SQLException ex) {
            return false;
        }
    }

}
