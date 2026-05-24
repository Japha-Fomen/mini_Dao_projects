package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Gestionnaire centralisé de la navigation entre vues.
 *
 * LEÇON — Pattern "Navigation centralisée"
 * ─────────────────────────────────────────
 * En JavaFX, changer de vue = changer la Scene du Stage.
 * Plutôt que chaque controller tienne une référence au Stage,
 * on centralise ici :
 *   1. La référence au Stage principal (setStage)
 *   2. L'état de session (utilisateur connecté + rôle)
 *   3. Les méthodes de navigation (showLogin, showStudentView, etc.)
 *
 * FXMLLoader.load() fait deux choses :
 *   a) Parse le fichier .fxml et crée l'arbre de nœuds (SceneGraph)
 *   b) Instancie le controller, injecte les @FXML, appelle initialize()
 */
public class ViewManager {

    private static Stage  mainStage;
    private static int    currentUserId;
    private static String currentRole;   // "student" | "enseignant" | "admin"
    private static String currentName;

    public static void setStage(Stage stage) { mainStage = stage; }

    public static void setCurrentName(String currentName) {
        ViewManager.currentName = currentName;
    }

    public static void   setCurrentUser(int id, String role) { currentUserId = id; currentRole = role; }
    public static int    getCurrentUserId() { return currentUserId; }
    public static String getCurrentRole()   { return currentRole;   }
    public static String getCurrentName()    { return currentName; }

    // ─────────────────────────────────────────────────────────────────────────

    private static void switchTo(String fxmlName, String title, double w, double h) {
        try {
            // getResource() cherche le fichier relatif au package de ViewManager
            // (src/main/resources/org/example/<fxmlName>)
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(fxmlName));
            Scene scene = new Scene(loader.load(), w, h);

            String cssUrl = ViewManager.class.getResource("styles.css").toExternalForm();
            scene.getStylesheets().add(cssUrl);

            mainStage.setTitle(title);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger : " + fxmlName, e);
        }
    }

    // ─── Points d'entrée ────────────────────────────────────────────────────

    public static void showLogin() {
        switchTo("login.fxml", "Gestion Académique", 500, 440);
    }

    public static void showStudentView() {
        switchTo("student_view.fxml", "Portail Étudiant", 920, 620);
    }

    public static void showEnseignantView() {
        switchTo("enseignant_view.fxml", "Portail Enseignant", 1050, 660);
    }

    public static void showAdminView() {
        switchTo("admin_view.fxml", "Administration", 1100, 700);
    }
}
