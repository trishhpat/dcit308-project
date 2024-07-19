package com.example.pharmacy_management_system.controllers.ControllerTraits;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Node;


public class Navigator {

    public static void navigate(String fxml, ActionEvent event) {
        // Create a task to load the new scene
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Platform.runLater(() -> stage.getScene().setRoot(root));
                return null;
            }
        };
        // Run the task in a background thread
        new Thread(task).start();
    }
    
}
