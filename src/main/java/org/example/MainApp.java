package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Point d'entrée JavaFX.
 *
 * LEÇON — Cycle de vie JavaFX
 * ───────────────────────────
 * 1. main() → appelle launch() → JavaFX démarre sur le thread UI (JavaFX Application Thread)
 * 2. JavaFX crée une instance de MainApp et appelle init() (optionnel, pas overridé ici)
 * 3. JavaFX appelle start(Stage) → on reçoit le Stage principal déjà créé
 * 4. On délègue tout à ViewManager : il garde la référence au Stage et affiche la première vue
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setResizable(true);
        ViewManager.setStage(stage);
        ViewManager.showLogin();
    }

    public static void main(String[] args) {
        launch();
    }
}

