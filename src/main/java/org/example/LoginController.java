package org.example;

import Objects.Enseignant;
import Objects.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginController {

    // ── Champs injectés depuis login.fxml ─────────────────────────────────────
    @FXML private Button btnEtudiant;
    @FXML private Button btnEnseignant;
    @FXML private Button btnAdmin;
    @FXML private VBox   idSection;    // la section ID + Nom (cachée par défaut)
    @FXML private TextField idField;   // champ pour l'ID
    @FXML private TextField nomField;  // champ pour le nom (nouveau)
    @FXML private Button btnConnexion;
    @FXML private Label  errorLabel;

    private String selectedRole; // "student", "enseignant" ou "admin"

    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    void onRoleSelected(ActionEvent event) {
        Button clicked = (Button) event.getSource();
        selectedRole = (String) clicked.getUserData();

        // Réinitialiser le style de tous les boutons, puis mettre en évidence le cliqué
        for (Button b : new Button[]{btnEtudiant, btnEnseignant, btnAdmin}) {
            b.getStyleClass().remove("role-btn-selected");
        }
        clicked.getStyleClass().add("role-btn-selected");

        // Montrer les champs ID + Nom seulement pour étudiant et enseignant
        boolean needsCredentials = !"admin".equals(selectedRole);
        idSection.setVisible(needsCredentials);
        idSection.setManaged(needsCredentials);

        btnConnexion.setVisible(true);
        btnConnexion.setManaged(true);

        // Vider les champs et le message d'erreur à chaque changement de rôle
        errorLabel.setText("");
        idField.clear();
        nomField.clear();
    }

    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    void onConnexion(ActionEvent event) {
        errorLabel.setText(""); // effacer l'erreur précédente

        // ── Cas Admin : pas de vérification en base ───────────────────────────
        if ("admin".equals(selectedRole)) {
            ViewManager.setCurrentUser(-1, "admin");
            ViewManager.showAdminView();
            return;
        }

        // ── Validation des champs saisis ──────────────────────────────────────
        String idTexte  = idField.getText().trim();
        String nomSaisi = nomField.getText().trim();

        if (idTexte.isEmpty()) {
            errorLabel.setText("Veuillez entrer votre ID.");
            return;
        }
        if (nomSaisi.isEmpty()) {
            errorLabel.setText("Veuillez entrer votre nom.");
            return;
        }

        // Convertir l'ID en entier
        int id;
        try {
            id = Integer.parseInt(idTexte);
        } catch (NumberFormatException e) {
            errorLabel.setText("L'ID doit être un nombre entier.");
            return;
        }

        // ── Vérification en base de données ───────────────────────────────────
        if ("student".equals(selectedRole)) {
            verifierEtudiant(id, nomSaisi);
        } else {
            verifierEnseignant(id, nomSaisi);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Vérifie qu'un étudiant avec cet ID et ce nom existe en base.
     * Si oui → navigue vers la vue étudiant.
     * Si non → affiche un message d'erreur.
     */
    private void verifierEtudiant(int id, String nomSaisi) {
        // Étape 1 : chercher l'étudiant par ID dans la base
        Student student = AppContext.adminService.findStudentById(id);

        // Étape 2 : vérifier que l'ID existe
        if (student == null) {
            errorLabel.setText("Aucun étudiant trouvé avec l'ID " + id + ".");
            return;
        }

        // Étape 3 : vérifier que le nom correspond
        // equalsIgnoreCase → insensible à la casse ("jean" = "Jean" = "JEAN")
        if (!student.getName().equalsIgnoreCase(nomSaisi)) {
            errorLabel.setText("Le nom ne correspond pas à cet ID.");
            return;
        }

        // Étape 4 : tout est bon → accorder l'accès
        ViewManager.setCurrentUser(id, "student");
        ViewManager.setCurrentName(nomSaisi);
        ViewManager.showStudentView();
    }

    /**
     * Vérifie qu'un enseignant avec cet ID et ce nom existe en base.
     * Si oui → navigue vers la vue enseignant.
     * Si non → affiche un message d'erreur.
     */
    private void verifierEnseignant(int id, String nomSaisi) {
        // Étape 1 : chercher l'enseignant par ID dans la base
        Enseignant enseignant = AppContext.adminService.findEnseignantById(id);

        // Étape 2 : vérifier que l'ID existe
        if (enseignant == null) {
            errorLabel.setText("Aucun enseignant trouvé avec l'ID " + id + ".");
            return;
        }

        // Étape 3 : vérifier que le nom correspond
        if (!enseignant.getNom().equalsIgnoreCase(nomSaisi)) {
            errorLabel.setText("Le nom ne correspond pas à cet ID.");
            return;
        }

        // Étape 4 : tout est bon → accorder l'accès
        ViewManager.setCurrentUser(id, "enseignant");
        ViewManager.setCurrentName(nomSaisi);
        ViewManager.showEnseignantView();
    }
}
