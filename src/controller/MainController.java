package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

public class MainController {

    @FXML
    private TextField username_field;

    @FXML
    private TextField password_taker;  
    @FXML
    private Button loginBtn;


    @FXML
    public void loginBtn() throws IOException{

        User defaultUser = new User("justice","1234");
        String username = username_field.getText();
        String password = password_taker.getText();  // Get the password from the field

        if(defaultUser.isDefaultPassword(username, password)){
            FXMLLoader landingPage = new FXMLLoader(getClass().getResource("/view/landingPage.fxml"));
            Parent root = landingPage.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            loginBtn.getScene().getWindow().hide();  // Hide the current stage containing the login page

            System.out.println("You are trying to log in");
        } else {
            System.out.println("not vaid details");
        }
    }
}









// package controller;

// import java.io.IOException;

// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.stage.Stage;
// import javafx.stage.StageStyle;
// import model.User;

// public class MainController {

//     @FXML
//     private TextField username_feild;

//     private TextField password_taker;

//     @FXML
//     private Button loginBtn;

//     @FXML
//     public void loginBtn() throws IOException{

//         User defaultUser = new User("justice","1234");
//         String username = username_feild.getText();

//         if(defaultUser.isDefaultPassword(username,"1234")){
//             FXMLLoader landingPage = new FXMLLoader(getClass().getResource("/view/landingPage.fxml"));
//             Parent root = landingPage.load();
//             Stage stage = new Stage();
//             Scene scene = new Scene(root);
//             stage.initStyle(StageStyle.TRANSPARENT);
//             stage.setScene(scene);
//             stage.show();

//             loginBtn.getScene().getWindow().hide();//now we hide the stage containg the loginpage

//             System.out.println("You are trying to log in ");
//         }else {
//             System.out.println("your somehtinf is not done well ");  
//         }
//     }
// }
