/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Dido
 */
public class AdminLogin extends STT_BranchManager {
    
    private boolean login;
    private Scene loginScene;
     
    public Scene loginMethod(Stage window) {
        
        System.out.println("AdminLogin.java -> loginMethod");
        
        Label lblUser = new Label("Admin Name");
        TextField userTextField = new TextField();

        Label passLabel = new Label("Password");
        PasswordField passTextField = new PasswordField();

        GridPane loginGridPane = new GridPane();
        loginGridPane.add(lblUser, 0, 0);
        loginGridPane.add(userTextField, 1, 0);
        loginGridPane.add(passLabel, 0, 1);
        loginGridPane.add(passTextField, 1, 1);
        loginGridPane.setAlignment(Pos.CENTER); 
        loginGridPane.setHgap(10);
        loginGridPane.setVgap(5);

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> {
            DBAdmin admin = new DBAdmin();
            try {
                login = admin.DBAdmin(userTextField.getText(), passTextField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            if (login == true) {
                MainDashboard mdb = new MainDashboard();
                mdb.adminBranch = admin.adminBranch;
                window.setScene(mdb.mainMethod(window));
            } else {
//                Popup.display("Error Logging In", "Log-in Failed. Try again.");
            }
        });

        VBox loginVBox = new VBox(10);
        loginVBox.getChildren().addAll(loginGridPane, loginBtn);
        loginVBox.setPadding(new Insets(10, 10, 10, 10));
        loginVBox.setAlignment(Pos.CENTER);

        StackPane loginLayout = new StackPane();
        loginLayout.getChildren().add(loginVBox);

        loginScene = new Scene(loginLayout, 560, 640);
        return loginScene;
    }
    
}


