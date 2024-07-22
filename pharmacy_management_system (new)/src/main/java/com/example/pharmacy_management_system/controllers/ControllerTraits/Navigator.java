package com.example.pharmacy_management_system.controllers.ControllerTraits;
import java.util.Stack;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;


public class Navigator {
    private static Stack<String> priviousRoute = new Stack<String>();

    public static void navigate(String fxml, ActionEvent event, Label loadingLabel) {
         // Show the loading label
         loadingLabel.setVisible(true);

         priviousRoute.push(fxml);
         // Create a task to load the new scene
         Task<Void> task = new Task<>() {
             @Override
             protected Void call() throws Exception {
                 FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                 Parent root = loader.load();
                 Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                 // Update the UI on the JavaFX Application Thread
                 javafx.application.Platform.runLater(() -> stage.getScene().setRoot(root));
                 return null;
             }
         };
 
         // Set the task to hide the loading label after loading is done
         task.setOnSucceeded(e -> loadingLabel.setVisible(false));
 
         // Run the task in a background thread
         new Thread(task).start();
    }

    public static String getPreviousRoute(){
        if(priviousRoute.isEmpty()){return null;}
        else{return priviousRoute.pop();}
    }
    

    
}
