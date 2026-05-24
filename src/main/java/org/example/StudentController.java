package org.example;

import DtoObjects.CoursStudentDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class StudentController {

    // ── Champs injectés depuis student_view.fxml ─────────────────────────────
    @FXML private Label welcomeLabel;
    @FXML private TableView<CoursStudentDto> coursTable;
    @FXML private Label statusLabel;

    private int studentId;
    private String studentName;

    // ── Initialisation ────────────────────────────────────────────────────────
    // initialize() est appelé automatiquement par FXMLLoader juste après l'injection @FXML
    @FXML
    void initialize() {
        studentId = ViewManager.getCurrentUserId();
        studentName=ViewManager.getCurrentName();
        welcomeLabel.setText("Étudiant : "+studentName+" |  ID : " + studentId);
        setupColumns();
        loadData();
    }

    // ── Colonnes de la TableView ──────────────────────────────────────────────
    // Principe : TableColumn<T, V>
    //   T = type de l'objet représentant une LIGNE  (ici CoursStudentDto)
    //   V = type de la VALEUR affichée dans la colonne (String, Integer, etc.)
    //
    // setCellValueFactory définit comment extraire V depuis un T :
    //   d             → CellDataFeatures<T, V> (wrapper JavaFX)
    //   d.getValue()  → l'objet T de la ligne courante
    //   on retourne   → ObservableValue<V>
    //
    // SimpleStringProperty  → wraps une String en ObservableValue<String>
    // SimpleIntegerProperty → wraps un int, .asObject() le convertit en ObservableValue<Integer>
    private void setupColumns() {

        TableColumn<CoursStudentDto, Integer> colId = new TableColumn<>("ID Cours");
        colId.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getIdCoursOffert()).asObject()
        );
        colId.setPrefWidth(80);

        TableColumn<CoursStudentDto, String> colCode = new TableColumn<>("Code");
        colCode.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getCodeMatiere())
        );
        colCode.setPrefWidth(90);

        TableColumn<CoursStudentDto, String> colNom = new TableColumn<>("Matière");
        colNom.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNomMatiere())
        );
        colNom.setPrefWidth(200);

        TableColumn<CoursStudentDto, String> colEnseignant = new TableColumn<>("Enseignant");
        colEnseignant.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getNomEnseignant())
        );
        colEnseignant.setPrefWidth(160);

        TableColumn<CoursStudentDto, String> colSession = new TableColumn<>("Session");
        colSession.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getSession())
        );
        colSession.setPrefWidth(90);

        TableColumn<CoursStudentDto, Integer> colAnnee = new TableColumn<>("Année");
        colAnnee.setCellValueFactory(d ->
            new SimpleIntegerProperty(d.getValue().getAnnee()).asObject()
        );
        colAnnee.setPrefWidth(70);

        TableColumn<CoursStudentDto, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getStatut())
        );
        colStatut.setPrefWidth(100);

        // Colonne spéciale : getNote() retourne Double (nullable), pas double
        // On gère le cas null explicitement dans le lambda
        TableColumn<CoursStudentDto, String> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(d -> {
            Double note = d.getValue().getNote();
            String texte = (note == null) ? "—" : String.format("%.2f", note);
            return new SimpleStringProperty(texte);
        });
        colNote.setPrefWidth(80);

        // Ajouter colonne par colonne pour éviter les warnings de types génériques mixtes
        coursTable.getColumns().add(colId);
        coursTable.getColumns().add(colCode);
        coursTable.getColumns().add(colNom);
        coursTable.getColumns().add(colEnseignant);
        coursTable.getColumns().add(colSession);
        coursTable.getColumns().add(colAnnee);
        coursTable.getColumns().add(colStatut);
        coursTable.getColumns().add(colNote);
    }

    // ── Chargement des données ────────────────────────────────────────────────
    private void loadData() {
        try {
            List<CoursStudentDto> cours = AppContext.studentService.ConsulterMesCours(studentId);

            // FXCollections.observableArrayList : crée une ObservableList à partir d'une List normale
            // La TableView observe cette liste : toute modification future se reflète automatiquement
            coursTable.setItems(FXCollections.observableArrayList(cours));
            statusLabel.setText(cours.size() + " cours trouvé(s)");
        } catch (Exception e) {
            statusLabel.setText("Erreur de chargement : " + e.getMessage());
        }
    }

    // ── Handlers des boutons du bas ───────────────────────────────────────────

    @FXML
    void onVoirSupports() {
        // getSelectionModel().getSelectedItem() retourne l'objet de la ligne sélectionnée
        // Retourne null si aucune ligne n'est sélectionnée
        CoursStudentDto selected = coursTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Sélection requise", "Cliquez sur un cours dans le tableau d'abord.");
            return;
        }
        List<String> supports = AppContext.studentService.consulterSupportsCours(selected.getIdCoursOffert());
        if (supports.isEmpty()) {
            alert("Supports de cours", "Aucun support disponible pour ce cours.");
        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Supports — " + selected.getNomMatiere());
            a.setHeaderText(supports.size() + " support(s) :");
            a.setContentText(String.join("\n", supports));
            a.showAndWait();
        }
    }

    @FXML
    void onAbandonner() {
        CoursStudentDto selected = coursTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Sélection requise", "Cliquez sur un cours dans le tableau d'abord.");
            return;
        }
        if (!"on_going".equals(selected.getStatut())) {
            alert("Action impossible", "Seul un cours avec le statut 'on_going' peut être abandonné.");
            return;
        }
        boolean confirme = confirm(
            "Confirmer l'abandon",
            "Abandonner : " + selected.getNomMatiere()
            + " (" + selected.getSession() + " " + selected.getAnnee() + ") ?"
        );
        if (confirme) {
            boolean ok = AppContext.studentService.abandonnerCours(studentId, selected.getIdCoursOffert());
            alert(ok ? "Succès" : "Erreur", ok ? "Cours abandonné." : "L'opération a échoué.");
            if (ok) loadData(); // recharger la table pour refléter le changement
        }
    }

    @FXML
    void onDeconnexion() {
        ViewManager.showLogin();
    }

    // ── Utilitaires d'affichage ───────────────────────────────────────────────

    private void alert(String titre, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titre);
        a.setHeaderText(null); // pas de sous-titre
        a.setContentText(message);
        a.showAndWait(); // bloque jusqu'à ce que l'utilisateur ferme la boîte
    }

    private boolean confirm(String titre, String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(titre);
        a.setHeaderText(null);
        a.setContentText(message);
        // showAndWait() retourne Optional<ButtonType>
        // .filter(r -> r == ButtonType.OK) garde seulement si OK a été cliqué
        // .isPresent() retourne true si OK, false si Cancel ou fermeture
        return a.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }
}
