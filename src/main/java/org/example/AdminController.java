package org.example;

import Objects.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AdminController {

    // ── Champs injectés depuis admin_view.fxml ────────────────────────────────
    // Une TableView par onglet
    @FXML private TableView<Student>     studentsTable;
    @FXML private TableView<Enseignant>  enseignantsTable;
    @FXML private TableView<Matiere>     matieresTable;
    @FXML private TableView<CoursOffert> coursTable;
    @FXML private TableView<Inscription> inscriptionsTable;

    // ── Initialisation des 5 onglets ─────────────────────────────────────────
    @FXML
    void initialize() {
        setupStudentsTab();
        setupEnseignantsTab();
        setupMatieresTab();
        setupCoursTab();
        setupInscriptionsTab();
    }

    // ════════════════════════════════════════════════════════════════════
    // ONGLET ÉTUDIANTS
    // ════════════════════════════════════════════════════════════════════

    private void setupStudentsTab() {
        TableColumn<Student, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getId()).asObject()
        );
        colId.setPrefWidth(80);

        TableColumn<Student, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getName())
        );
        colNom.setPrefWidth(220);

        TableColumn<Student, Integer> colAge = new TableColumn<>("Âge");
        colAge.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getAge()).asObject()
        );
        colAge.setPrefWidth(80);

        studentsTable.getColumns().add(colId);
        studentsTable.getColumns().add(colNom);
        studentsTable.getColumns().add(colAge);

        refreshStudents();
    }

    private void refreshStudents() {
        studentsTable.setItems(
            FXCollections.observableArrayList(AppContext.adminService.listEtudiants())
        );
    }

    @FXML void onAjouterEtudiant() {
        // showStudentDialog(null) → mode "création" (aucun objet existant à pré-remplir)
        showStudentDialog(null).showAndWait().ifPresent(s -> {
            boolean ok = AppContext.adminService.enregistrerEtudiant(s);
            alert(ok ? "Succès" : "Erreur", ok ? "Étudiant ajouté." : "Échec (ID déjà existant ?).");
            if (ok) refreshStudents();
        });
    }

    @FXML void onModifierEtudiant() {
        Student sel = studentsTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez un étudiant."); return; }
        // showStudentDialog(sel) → mode "modification" (les champs sont pré-remplis)
        showStudentDialog(sel).showAndWait().ifPresent(s -> {
            boolean ok = AppContext.adminService.updateStudent(s);
            alert(ok ? "Succès" : "Erreur", ok ? "Modifié." : "Échec.");
            if (ok) refreshStudents();
        });
    }

    @FXML void onSupprimerEtudiant() {
        Student sel = studentsTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez un étudiant."); return; }
        if (confirm("Confirmer", "Supprimer " + sel.getName() + " ?")) {
            boolean ok = AppContext.adminService.deleteStudent(sel);
            alert(ok ? "Succès" : "Erreur", ok ? "Supprimé." : "Échec.");
            if (ok) refreshStudents();
        }
    }

    @FXML void onInscrireEtudiant() {
        Student sel = studentsTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez un étudiant d'abord."); return; }
        showInscriptionDialog(sel.getId()).showAndWait().ifPresent(ins -> {
            boolean ok = AppContext.adminService.enregistrerInscription(ins);
            alert(ok ? "Succès" : "Erreur", ok ? "Inscription créée." : "Échec (déjà inscrit ?).");
            if (ok) refreshInscriptions();
        });
    }

    // Dialog<Student> : boîte de dialogue qui retourne un objet Student
    // existing = null → création | existing = Student → modification (champs pré-remplis)
    private Dialog<Student> showStudentDialog(Student existing) {
        Dialog<Student> dlg = new Dialog<>();
        dlg.setTitle(existing == null ? "Ajouter un étudiant" : "Modifier l'étudiant");

        // Ajouter les boutons OK et Cancel à la boîte
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Créer le formulaire avec GridPane (grille ligne/colonne)
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 24, 10, 10));

        TextField tfId  = new TextField(existing == null ? "" : String.valueOf(existing.getId()));
        TextField tfNom = new TextField(existing == null ? "" : existing.getName());
        TextField tfAge = new TextField(existing == null ? "" : String.valueOf(existing.getAge()));

        // En modification, l'ID ne doit pas être changé
        tfId.setDisable(existing != null);

        // grid.add(nœud, colonne, ligne)
        grid.add(new Label("ID :"),  0, 0);  grid.add(tfId,  1, 0);
        grid.add(new Label("Nom :"), 0, 1);  grid.add(tfNom, 1, 1);
        grid.add(new Label("Âge :"), 0, 2);  grid.add(tfAge, 1, 2);

        dlg.getDialogPane().setContent(grid);

        // setResultConverter : définit comment construire l'objet retourné par la boîte
        // buttonType → le bouton cliqué par l'utilisateur (OK ou CANCEL)
        // Retourner null = la boîte retourne Optional.empty()
        dlg.setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            try {
                int id = (existing != null) ? existing.getId() : Integer.parseInt(tfId.getText().trim());
                String nom = tfNom.getText().trim();
                int age = Integer.parseInt(tfAge.getText().trim());
                if (nom.isEmpty()) return null;
                return new Student(id, nom, age);
            } catch (NumberFormatException e) {
                return null; // si l'ID ou l'âge n'est pas un entier
            }
        });
        return dlg;
    }

    // ════════════════════════════════════════════════════════════════════
    // ONGLET ENSEIGNANTS
    // ════════════════════════════════════════════════════════════════════

    private void setupEnseignantsTab() {
        TableColumn<Enseignant, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getId()).asObject()
        );
        colId.setPrefWidth(80);

        TableColumn<Enseignant, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNom())
        );
        colNom.setPrefWidth(200);

        TableColumn<Enseignant, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getEmail())
        );
        colEmail.setPrefWidth(240);

        enseignantsTable.getColumns().add(colId);
        enseignantsTable.getColumns().add(colNom);
        enseignantsTable.getColumns().add(colEmail);

        refreshEnseignants();
    }

    private void refreshEnseignants() {
        enseignantsTable.setItems(
            FXCollections.observableArrayList(AppContext.adminService.enseignantList())
        );
    }

    @FXML void onAjouterEnseignant() {
        showEnseignantDialog(null).showAndWait().ifPresent(e -> {
            boolean ok = AppContext.adminService.enregistrerEnseignant(e);
            alert(ok ? "Succès" : "Erreur", ok ? "Enseignant ajouté." : "Échec.");
            if (ok) refreshEnseignants();
        });
    }

    @FXML void onModifierEnseignant() {
        Enseignant sel = enseignantsTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez un enseignant."); return; }
        showEnseignantDialog(sel).showAndWait().ifPresent(e -> {
            boolean ok = AppContext.adminService.updateEnseignant(e);
            alert(ok ? "Succès" : "Erreur", ok ? "Modifié." : "Échec.");
            if (ok) refreshEnseignants();
        });
    }

    @FXML void onSupprimerEnseignant() {
        Enseignant sel = enseignantsTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez un enseignant."); return; }
        if (confirm("Confirmer", "Supprimer " + sel.getNom() + " ?")) {
            boolean ok = AppContext.adminService.deleteEnseignant(sel);
            alert(ok ? "Succès" : "Erreur", ok ? "Supprimé." : "Échec.");
            if (ok) refreshEnseignants();
        }
    }

    private Dialog<Enseignant> showEnseignantDialog(Enseignant existing) {
        Dialog<Enseignant> dlg = new Dialog<>();
        dlg.setTitle(existing == null ? "Ajouter un enseignant" : "Modifier l'enseignant");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(20, 24, 10, 10));

        TextField tfId    = new TextField(existing == null ? "" : String.valueOf(existing.getId()));
        TextField tfNom   = new TextField(existing == null ? "" : existing.getNom());
        TextField tfEmail = new TextField(existing == null ? "" : existing.getEmail());
        tfId.setDisable(existing != null);

        grid.add(new Label("ID :"),    0, 0); grid.add(tfId,    1, 0);
        grid.add(new Label("Nom :"),   0, 1); grid.add(tfNom,   1, 1);
        grid.add(new Label("Email :"), 0, 2); grid.add(tfEmail, 1, 2);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(btn -> {
            if (btn != ButtonType.OK) return null;
            try {
                int id = (existing != null) ? existing.getId() : Integer.parseInt(tfId.getText().trim());
                String nom   = tfNom.getText().trim();
                String email = tfEmail.getText().trim();
                return nom.isEmpty() ? null : new Enseignant(id, nom, email);
            } catch (NumberFormatException e) { return null; }
        });
        return dlg;
    }

    // ════════════════════════════════════════════════════════════════════
    // ONGLET MATIÈRES
    // ════════════════════════════════════════════════════════════════════

    private void setupMatieresTab() {
        TableColumn<Matiere, String> colCode = new TableColumn<>("Code");
        colCode.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getIdMatiere())
        );
        colCode.setPrefWidth(100);

        TableColumn<Matiere, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNameMatiere())
        );
        colNom.setPrefWidth(200);

        TableColumn<Matiere, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getDescription())
        );
        colDesc.setPrefWidth(300);

        matieresTable.getColumns().add(colCode);
        matieresTable.getColumns().add(colNom);
        matieresTable.getColumns().add(colDesc);

        refreshMatieres();
    }

    private void refreshMatieres() {
        matieresTable.setItems(
            FXCollections.observableArrayList(AppContext.adminService.listMatieres())
        );
    }

    @FXML void onAjouterMatiere() {
        showMatiereDialog().showAndWait().ifPresent(m -> {
            boolean ok = AppContext.adminService.enregistrerMatiere(m);
            alert(ok ? "Succès" : "Erreur", ok ? "Matière ajoutée." : "Échec (code déjà existant ?).");
            if (ok) refreshMatieres();
        });
    }

    @FXML void onSupprimerMatiere() {
        Matiere sel = matieresTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez une matière."); return; }
        if (confirm("Confirmer", "Supprimer : " + sel.getNameMatiere() + " ?")) {
            boolean ok = AppContext.adminService.deleteMatiere(sel);
            alert(ok ? "Succès" : "Erreur", ok ? "Supprimée." : "Échec.");
            if (ok) refreshMatieres();
        }
    }

    private Dialog<Matiere> showMatiereDialog() {
        Dialog<Matiere> dlg = new Dialog<>();
        dlg.setTitle("Ajouter une matière");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(20, 24, 10, 10));

        TextField tfCode = new TextField(); tfCode.setPromptText("Ex : INF101");
        TextField tfNom  = new TextField(); tfNom.setPromptText("Ex : Algorithmique");
        TextField tfDesc = new TextField(); tfDesc.setPromptText("Description courte");

        grid.add(new Label("Code :"),        0, 0); grid.add(tfCode, 1, 0);
        grid.add(new Label("Nom :"),          0, 1); grid.add(tfNom,  1, 1);
        grid.add(new Label("Description :"), 0, 2); grid.add(tfDesc, 1, 2);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(btn -> {
            if (btn != ButtonType.OK) return null;
            String code = tfCode.getText().trim();
            String nom  = tfNom.getText().trim();
            return (code.isEmpty() || nom.isEmpty()) ? null : new Matiere(code, nom, tfDesc.getText().trim());
        });
        return dlg;
    }

    // ════════════════════════════════════════════════════════════════════
    // ONGLET COURS OFFERTS
    // ════════════════════════════════════════════════════════════════════

    private void setupCoursTab() {
        TableColumn<CoursOffert, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdCoursOffert()).asObject()
        );
        colId.setPrefWidth(70);

        TableColumn<CoursOffert, String> colMat = new TableColumn<>("Matière");
        colMat.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getIdMatiere())
        );
        colMat.setPrefWidth(100);

        TableColumn<CoursOffert, Integer> colEns = new TableColumn<>("ID Ens.");
        colEns.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdEnseignant()).asObject()
        );
        colEns.setPrefWidth(80);

        TableColumn<CoursOffert, String> colSess = new TableColumn<>("Session");
        colSess.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getSession())
        );
        colSess.setPrefWidth(90);

        TableColumn<CoursOffert, Integer> colAnnee = new TableColumn<>("Année");
        colAnnee.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getAnnee()).asObject()
        );
        colAnnee.setPrefWidth(80);

        coursTable.getColumns().add(colId);
        coursTable.getColumns().add(colMat);
        coursTable.getColumns().add(colEns);
        coursTable.getColumns().add(colSess);
        coursTable.getColumns().add(colAnnee);

        refreshCours();
    }

    private void refreshCours() {
        coursTable.setItems(
            FXCollections.observableArrayList(AppContext.adminService.listCoursOfferts())
        );
    }

    @FXML void onAjouterCours() {
        showCoursDialog(null).showAndWait().ifPresent(c -> {
            boolean ok = AppContext.adminService.enregistrerCoursOffert(c);
            alert(ok ? "Succès" : "Erreur", ok ? "Cours ajouté." : "Échec.");
            if (ok) refreshCours();
        });
    }

    @FXML void onModifierCours() {
        CoursOffert sel = coursTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez un cours."); return; }
        showCoursDialog(sel).showAndWait().ifPresent(c -> {
            boolean ok = AppContext.adminService.updateCoursOffert(c);
            alert(ok ? "Succès" : "Erreur", ok ? "Modifié." : "Échec.");
            if (ok) refreshCours();
        });
    }

    @FXML void onSupprimerCours() {
        CoursOffert sel = coursTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez un cours."); return; }
        if (confirm("Confirmer", "Supprimer ce cours (et toutes ses inscriptions/supports) ?")) {
            boolean ok = AppContext.adminService.deleteCoursOffert(sel);
            alert(ok ? "Succès" : "Erreur", ok ? "Supprimé." : "Échec.");
            if (ok) refreshCours();
        }
    }

    private Dialog<CoursOffert> showCoursDialog(CoursOffert existing) {
        Dialog<CoursOffert> dlg = new Dialog<>();
        dlg.setTitle(existing == null ? "Ajouter un cours offert" : "Modifier le cours");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(20, 24, 10, 10));

        TextField tfMat  = new TextField(existing == null ? "" : existing.getIdMatiere());
        TextField tfEns  = new TextField(existing == null ? "" : String.valueOf(existing.getIdEnseignant()));
        // ChoiceBox<String> : liste déroulante avec choix prédéfinis
        ChoiceBox<String> cbSession = new ChoiceBox<>(
            FXCollections.observableArrayList("Automne", "Hiver", "Été", "Été 1", "Été 2")
        );
        if (existing != null) cbSession.setValue(existing.getSession());
        TextField tfAnnee = new TextField(
            existing == null ? String.valueOf(java.time.Year.now().getValue()) : String.valueOf(existing.getAnnee())
        );

        grid.add(new Label("Code matière :"),  0, 0); grid.add(tfMat,     1, 0);
        grid.add(new Label("ID enseignant :"), 0, 1); grid.add(tfEns,     1, 1);
        grid.add(new Label("Session :"),       0, 2); grid.add(cbSession, 1, 2);
        grid.add(new Label("Année :"),         0, 3); grid.add(tfAnnee,   1, 3);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(btn -> {
            if (btn != ButtonType.OK) return null;
            try {
                String mat  = tfMat.getText().trim();
                int    ens  = Integer.parseInt(tfEns.getText().trim());
                String sess = cbSession.getValue();
                int    ann  = Integer.parseInt(tfAnnee.getText().trim());
                if (mat.isEmpty() || sess == null) return null;
                // CoursOffert sans ID → la DB génère l'ID (auto-incrément)
                // CoursOffert avec ID → on garde l'ID existant pour la modification
                return (existing == null)
                    ? new CoursOffert(mat, ens, sess, ann)
                    : new CoursOffert(existing.getIdCoursOffert(), mat, ens, sess, ann);
            } catch (NumberFormatException e) { return null; }
        });
        return dlg;
    }

    // ════════════════════════════════════════════════════════════════════
    // ONGLET INSCRIPTIONS
    // ════════════════════════════════════════════════════════════════════

    private void setupInscriptionsTab() {
        TableColumn<Inscription, Integer> colEtu = new TableColumn<>("ID Étudiant");
        colEtu.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdEtudiant()).asObject()
        );
        colEtu.setPrefWidth(110);

        TableColumn<Inscription, Integer> colCours = new TableColumn<>("ID Cours");
        colCours.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdCoursOffert()).asObject()
        );
        colCours.setPrefWidth(90);

        TableColumn<Inscription, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getStatut())
        );
        colStatut.setPrefWidth(110);

        TableColumn<Inscription, String> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(d -> {
            Double note = d.getValue().getNote();
            String texte = (note == null) ? "—" : String.format("%.2f", note);
            return new SimpleStringProperty(texte);
        });
        colNote.setPrefWidth(80);

        inscriptionsTable.getColumns().add(colEtu);
        inscriptionsTable.getColumns().add(colCours);
        inscriptionsTable.getColumns().add(colStatut);
        inscriptionsTable.getColumns().add(colNote);

        refreshInscriptions();
    }

    private void refreshInscriptions() {
        inscriptionsTable.setItems(
            FXCollections.observableArrayList(AppContext.adminService.listAllInscriptions())
        );
    }

    @FXML void onAjouterInscription() {
        showInscriptionDialog(-1).showAndWait().ifPresent(ins -> {
            boolean ok = AppContext.adminService.enregistrerInscription(ins);
            alert(ok ? "Succès" : "Erreur", ok ? "Inscription créée." : "Échec (déjà inscrit ?).");
            if (ok) refreshInscriptions();
        });
    }

    @FXML void onSupprimerInscription() {
        Inscription sel = inscriptionsTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Sélection", "Sélectionnez une inscription."); return; }
        if (confirm("Confirmer", "Supprimer l'inscription étudiant "
                + sel.getIdEtudiant() + " / cours " + sel.getIdCoursOffert() + " ?")) {
            boolean ok = AppContext.adminService.deleteInscription(sel);
            alert(ok ? "Succès" : "Erreur", ok ? "Supprimée." : "Échec.");
            if (ok) refreshInscriptions();
        }
    }

    // prefilledEtudiantId : si > 0, le champ ID étudiant est pré-rempli et désactivé
    //                       si = -1, l'utilisateur doit le saisir
    private Dialog<Inscription> showInscriptionDialog(int prefilledEtudiantId) {
        Dialog<Inscription> dlg = new Dialog<>();
        dlg.setTitle("Ajouter une inscription");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(20, 24, 10, 10));

        TextField tfEtu   = new TextField(prefilledEtudiantId > 0 ? String.valueOf(prefilledEtudiantId) : "");
        TextField tfCours = new TextField();
        ChoiceBox<String> cbStatut = new ChoiceBox<>(
            FXCollections.observableArrayList("on_going", "passed", "fail")
        );
        cbStatut.setValue("on_going");

        tfEtu.setDisable(prefilledEtudiantId > 0);

        grid.add(new Label("ID Étudiant :"), 0, 0); grid.add(tfEtu,   1, 0);
        grid.add(new Label("ID Cours :"),    0, 1); grid.add(tfCours, 1, 1);
        grid.add(new Label("Statut :"),      0, 2); grid.add(cbStatut, 1, 2);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(btn -> {
            if (btn != ButtonType.OK) return null;
            try {
                int etu   = Integer.parseInt(tfEtu.getText().trim());
                int cours = Integer.parseInt(tfCours.getText().trim());
                // note = null car une nouvelle inscription n'a pas encore de note
                return new Inscription(etu, cours, cbStatut.getValue(), null);
            } catch (NumberFormatException e) { return null; }
        });
        return dlg;
    }

    @FXML void onDeconnexion() { ViewManager.showLogin(); }

    // ── Utilitaires ───────────────────────────────────────────────────────────
    private void alert(String titre, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titre); a.setHeaderText(null); a.setContentText(message); a.showAndWait();
    }

    private boolean confirm(String titre, String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(titre); a.setHeaderText(null); a.setContentText(message);
        return a.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }
}
