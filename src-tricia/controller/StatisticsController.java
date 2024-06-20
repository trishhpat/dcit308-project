package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StatisticsController {
    @FXML
    private Button addButton;

    public void handleBack(ActionEvent event) {
        try {
            Parent homePage = FXMLLoader.load(getClass().getResource("/com/example/pharmacy_management_system/hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(homePage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStatistics() { }
        // Handle add button action

      @FXML
      private Button drugStatBtn;

      @FXML
      private void drugStatPage() throws IOException {
          navigate("/view/drug_statistics.fxml", "Drug Statistics");
      }
  
      private void navigate(String fxmlPath, String title) throws IOException {
          Stage stage = new Stage();
          Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
          stage.setTitle(title);
          stage.setScene(new Scene(root));
          stage.show();
      }
    }  
      

      

     

       
    
        

       


       


    

