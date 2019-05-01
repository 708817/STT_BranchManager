/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Dido
 */
public class AdminLogin extends MainDashboard {
    
    private boolean login;
    private Scene loginScene;
    public int phBranch = 0;
    public String phEmail = "";
    int adminBranch = 0;

    public Scene loginMethod(Stage window) {
        
        System.out.println("AdminLogin.java -> loginMethod");
        
        Image imgLogo = new Image("File:res/img/getmedlogo2.png");
        ImageView ivLogo = new ImageView(imgLogo);
        
        Label lblUser = new Label("Admin ID");
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

        Button loginBtn = new Button("LOG-IN");
        loginBtn.setOnAction(e -> {
            SQLAdmin admin = new SQLAdmin();
            try {
                login = admin.SQLAdmin(userTextField.getText(), passTextField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (login == true) {

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java3_project_stt?autoReconnect=true&useSSL=false", "root", "12345");

                    String encryptPass = "";
                    String plainPassword = passTextField.getText();

                    MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
                    mdAlgorithm.update(plainPassword.getBytes());

                    byte[] digest = mdAlgorithm.digest();
                    StringBuffer hexString = new StringBuffer();

                    for (int i = 0; i < digest.length; i++) {
                        plainPassword = Integer.toHexString(0xFF & digest[i]);

                        if (plainPassword.length() < 2) {
                            plainPassword = "0" + plainPassword;
                        }
                        hexString.append(plainPassword);
                    }
                    encryptPass = hexString.toString();
                    
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * from manager_account WHERE admin_id=" + userTextField.getText() + " AND authentication=\"" + encryptPass + "\"");

                    while (rs.next()) {
                        phBranch = rs.getInt("branch_id");
                        phEmail = rs.getString("email");
                    }
                    
                    adminBranch = phBranch;

                    rs.close();
                    st.close();
                    con.close();

                } catch (SQLSyntaxErrorException sqlex) {
                    sqlex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                MainDashboard mdb = new MainDashboard();
                mdb.adminBranch = adminBranch;
                window.setTitle(getAdminInfo());
                window.setScene(mdb.mainMethod(window));
                
            } else {
                System.out.print("no");
                MessagePopup.display("Log-in Failed", "Log-in Failed. Please try again.");
            }
        });
        loginBtn.getStyleClass().add("blueButton2");

        VBox loginVBox = new VBox(10);
        loginVBox.getChildren().addAll(ivLogo, loginGridPane, loginBtn);
        loginVBox.setPadding(new Insets(150, 10, 10, 10));
        loginVBox.setAlignment(Pos.TOP_CENTER);
        
        Image imgBG = new Image("File:res/img/loginbg3.jpg");
        ImageView ivBG = new ImageView(imgBG);
        
        StackPane loginLayout = new StackPane();
        loginLayout.getChildren().add(ivBG);
        loginLayout.getChildren().add(loginVBox);

        loginScene = new Scene(loginLayout, 490, 560);
        loginScene.getStylesheets().add("css/css_demo.css");
        return loginScene;
    }

    public String getAdminInfo() {
        return "GetMed Branch " + phBranch + " | <" + phEmail + ">";
    }
    
    
    
    
}


