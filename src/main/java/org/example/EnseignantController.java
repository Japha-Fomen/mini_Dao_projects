package org.example;

import DtoObjects.EnseignantCoursDto;
import DtoObjects.StudentDansCoursDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class EnseignantController {

    // ── Champs injectés depuis enseignant_view.fxml ──────────────────────────
    @FXML private Label welcomeLabel;
    @FXML private TableView<EnseignantCoursDto>  coursTable;
    @FXML private TableView<StudentDansCoursDto> studentsTable;
    @FXML private Label courseDetailLabel;
    @FXML private Label statusLabel;

    private int enseignantId;
    private String nomSaisi;

    // ── Initialisation ────────────────────────────────────────────────────────
    @FXML
    void initialize() {
        enseignantId = ViewManager.getCurrentUserId();
        nomSaisi = ViewManager.getCurrentName();
        welcomeLabel.setText("Enseignant "+nomSaisi+"  |  ID : " + enseignantId);

        setupCoursColumns();
        setupStudentsColumns();
        loadCours();

        // ChangeListener sur la sélection de coursTable
        // Quand l'enseignant clique sur une ligne → charger les étudiants de ce cours
        //
        // Syntaxe : .addListener((observable, ancienneValeur, nouvelleValeur) -> { ... })
        // observable    → la propriété elle-même (rarement utile)
        // ancienneValeur → ce qui était sélectionné AVANT le clic
        // nouvelleValeur → ce qui est sélectionné MAINTENANT (null si déselection)
        coursTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, ancienCours, nouveauCours) -> {
                if (nouveauCours != null) {
                    loadStudentsDuCours(nouveauCours);
                }
            }
        );
    }

    // ── Colonnes de coursTable (TableView<EnseignantCoursDto>) ────────────────
    private void setupCoursColumns() {

        TableColumn<EnseignantCoursDto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdCoursOffert()).asObject()
        );
        colId.setPrefWidth(55);

        TableColumn<EnseignantCoursDto, String> colCode = new TableColumn<>("Code");
        colCode.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getCodeMatiere())
        );
        colCode.setPrefWidth(80);

        TableColumn<EnseignantCoursDto, String> colNom = new TableColumn<>("Matière");
        colNom.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNomMatiere())
        );
        colNom.setPrefWidth(170);

        TableColumn<EnseignantCoursDto, String> colSession = new TableColumn<>("Session");
        colSession.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getSession())
        );
        colSession.setPrefWidth(80);

        TableColumn<EnseignantCoursDto, Integer> colAnnee = new TableColumn<>("Année");
        colAnnee.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getAnnee()).asObject()
        );
        colAnnee.setPrefWidth(70);

        TableColumn<EnseignantCoursDto, Integer> colEtudiants = new TableColumn<>("Étudiants");
        colEtudiants.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getNbEtudiants()).asObject()
        );
        colEtudiants.setPrefWidth(80);

        TableColumn<EnseignantCoursDto, Integer> colSupports = new TableColumn<>("Supports");
        colSupports.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getNbSupports()).asObject()
        );
        colSupports.setPrefWidth(70);

        coursTable.getColumns().add(colId);
        coursTable.getColumns().add(colCode);
        coursTable.getColumns().add(colNom);
        coursTable.getColumns().add(colSession);
        coursTable.getColumns().add(colAnnee);
        coursTable.getColumns().add(colEtudiants);
        coursTable.getColumns().add(colSupports);
    }

    // ── Colonnes de studentsTable (TableView<StudentDansCoursDto>) ────────────
    private void setupStudentsColumns() {

        TableColumn<StudentDansCoursDto, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdEtudiant()).asObject()
        );
        colId.setPrefWidth(60);

        TableColumn<StudentDansCoursDto, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNomComplet())
        );
        colNom.setPrefWidth(170);

        TableColumn<StudentDansCoursDto, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getStatut())
        );
        colStatut.setPrefWidth(100);

        // getNote() retourne Double (nullable) → on gère le null
        TableColumn<StudentDansCoursDto, String> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(d -> {
            Double note = d.getValue().getNote();
            String texte = (note == null) ? "—" : String.format("%.2f", note);
            return new SimpleStringProperty(texte);
        });
        colNote.setPrefWidth(80);

        studentsTable.getColumns().add(colId);
        studentsTable.getColumns().add(colNom);
        studentsTable.getColumns().add(colStatut);
        studentsTable.getColumns().add(colNote);
    }

    // ── Chargement des données ────────────────────────────────────────────────

    private void loadCours() {
        try {
            List<EnseignantCoursDto> cours = AppContext.enseignantService.consulterMesCours(enseignantId);
            coursTable.setItems(FXCollections.observableArrayList(cours));
            statusLabel.setText(cours.size() + " cours. Cliquez sur un cours pour voir ses étudiants.");
        } catch (Exception e) {
            statusLabel.setText("Erreur : " + e.getMessage());
        }
    }

    private void loadStudentsDuCours(EnseignantCoursDto cours) {
        courseDetailLabel.setText(
            "Étudiants — " + cours.getNomMatiere()
            + "  (" + cours.getSession() + " " + cours.getAnnee() + ")"
        );
        try {
            List<StudentDansCoursDto> etudiants =
                AppContext.enseignantService.consulterEtudiantsDuCours(cours.getIdCoursOffert());
            studentsTable.setItems(FXCollections.observableArrayList(etudiants));
        } catch (Exception e) {
            statusLabel.setText("Erreur : " + e.getMessage());
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @FXML
    void onAjouterSupport() {
        EnseignantCoursDto cours = coursTable.getSelectionModel().getSelectedItem();
        if (cours == null) {
            alert("Sélection requise", "Sélectionnez d'abord un cours.");
            return;
        }

        // TextInputDialog : boîte de dialogue avec un simple champ texte
        // Retourne Optional<String>
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Ajouter un support de cours");
        dlg.setHeaderText("Cours : " + cours.getNomMatiere() + " (" + cours.getSession() + " " + cours.getAnnee() + ")");
        dlg.setContentText("Chemin ou URL du fichier :");

        // showAndWait() bloque jusqu'à fermeture, retourne Optional<String>
        // .filter(s -> !s.isBlank()) : ignorer si l'utilisateur n'a rien saisi
        // .ifPresent(...) : exécuter seulement si une valeur est présente
        dlg.showAndWait()
           .filter(s -> !s.isBlank())
           .ifPresent(chemin -> {
               boolean ok = AppContext.enseignantService.ajouterSupportCours(cours.getIdCoursOffert(), chemin.trim());
               alert(ok ? "Succès" : "Erreur", ok ? "Support ajouté." : "L'ajout a échoué.");
               if (ok) loadCours(); // rafraîchir pour mettre à jour le compteur de supports
           });
    }

    @FXML
    void onNoterEtudiant() {
        EnseignantCoursDto cours = coursTable.getSelectionModel().getSelectedItem();
        StudentDansCoursDto etudiant = studentsTable.getSelectionModel().getSelectedItem();
        if (cours == null || etudiant == null) {
            alert("Sélection requise", "Sélectionnez un cours ET un étudiant.");
            return;
        }

        // Pré-remplir le champ avec la note actuelle si elle existe
        String noteActuelle = (etudiant.getNote() == null) ? "" : String.valueOf(etudiant.getNote());
        TextInputDialog dlg = new TextInputDialog(noteActuelle);
        dlg.setTitle("Attribuer une note");
        dlg.setHeaderText("Étudiant : " + etudiant.getNomComplet());
        dlg.setContentText("Note (0 – 100) :");

        dlg.showAndWait().ifPresent(texte -> {
            try {
                double note = Double.parseDouble(texte.trim());
                boolean ok = AppContext.enseignantService.noterEtudiant(
                    etudiant.getIdEtudiant(), cours.getIdCoursOffert(), note
                );
                alert(ok ? "Succès" : "Erreur", ok ? "Note enregistrée." : "Échec.");
                if (ok) loadStudentsDuCours(cours);
            } catch (NumberFormatException e) {
                alert("Erreur de saisie", "Entrez un nombre valide (ex : 87.5).");
            }
        });
    }

    @FXML
    void onChangerStatut() {
        EnseignantCoursDto cours = coursTable.getSelectionModel().getSelectedItem();
        StudentDansCoursDto etudiant = studentsTable.getSelectionModel().getSelectedItem();
        if (cours == null || etudiant == null) {
            alert("Sélection requise", "Sélectionnez un cours ET un étudiant.");
            return;
        }

        // ChoiceDialog<T> : boîte avec liste déroulante, retourne Optional<T>
        // 1er argument : valeur sélectionnée par défaut
        // arguments suivants : la liste des choix
        ChoiceDialog<String> dlg = new ChoiceDialog<>(
            etudiant.getStatut(),
            "on_going", "passed", "fail"
        );
        dlg.setTitle("Changer le statut");
        dlg.setHeaderText("Étudiant : " + etudiant.getNomComplet());
        dlg.setContentText("Nouveau statut :");

        dlg.showAndWait().ifPresent(statut -> {
            boolean ok = AppContext.enseignantService.changerStatutEtudiant(
                etudiant.getIdEtudiant(), cours.getIdCoursOffert(), statut
            );
            alert(ok ? "Succès" : "Erreur", ok ? "Statut mis à jour." : "Échec.");
            if (ok) loadStudentsDuCours(cours);
        });
    }

    @FXML
    void onDeconnexion() {
        ViewManager.showLogin();
    }

    // ── Utilitaire ────────────────────────────────────────────────────────────
    private void alert(String titre, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
