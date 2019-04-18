/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Dido
 */
public class MainDashboard extends AdminLogin {
    
    Scene mainScene;
    
    
    // MySQL database variables
    private Connection con; // REF 1
    private Statement st; // REF 2
    private ResultSet rs; // REF 2
    
    VBox vbb; // REF 3 Eto pangdisplay lang sa buong List ng gpList
    List<GridPane> gpList; // REF 4 Eto lalagyan or lagayan ng mga GridPane. Each GridPane contains the Information Details mentioned above.
    Button btnAccept;
    Button btnDecline;
    
    public Scene mainMethod(Stage window) {
        System.out.println("MainDashboard.java -> mainMethod");
        
        // Default GUI display declarations START
        vbb = new VBox(); // REF 3

        StackPane root = new StackPane();
        root.getChildren().add(vbb);

        mainScene = new Scene(root, 560, 640);
        // Default GUI display declarations END
        
        // Creating Connectivity to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false", "root", "12345"); // REF 1
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        showNewOrders();
        
        return mainScene;
    }
    
    public void showNewOrders() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            
                        // LAHAT NG KAILANGAN NAG-UUPDATE SA GUI FOR EVERY TWO SECONDS, LAGAY DITO
                            
                            // For every instance, i-clear yung laman ng Vbox 
                            vbb.getChildren().clear();
                            // Declaration of new GridPane List
                            gpList = new ArrayList<>(); // REF 4
                            
                            try {
                                // REF 2 
                                selectSQLStatement("SELECT * FROM orders WHERE Order_Status=\"Pending\"");
                                
                                while (rs.next()) {
                                    
                                    // Dito nilalagay ang declarations ng mga information details,
                                    // so kung gusto niyo lagyan ng Address, declare nalang ng Label para
                                    // ilagay doon. REF 5
                                    // NOTE: dapat yung pangalan sa loob ng rs.getString(); may parehas doon
                                    // sa MySQL Database.
                                    
                                    int dbOrder_ID = rs.getInt("Order_ID"),
                                            dbAccount_ID = rs.getInt("Account_ID"),
                                            dbBranch_ID = rs.getInt("Branch_ID");
                                    String dbOrder_Status = rs.getString("Order_Status"),
                                            dbOrder_Items = rs.getString("Order_Items");
                                    
                                    Label detailsRow1 = new Label (dbOrder_ID + ", " + dbAccount_ID + ", " + dbOrder_Status);
                                    Label detailsRow2 = new Label (dbOrder_Items);
                                    
                                    // Dito finoformat yung layout ng information detail ng isang customer.
                                    // Gamit ko GridPane since akala ko eto yung pinakamadaling i-format.
                                    GridPane tempGP = new GridPane();
                                    tempGP.add(detailsRow1, 0, 0);
                                    tempGP.add(detailsRow2, 0, 1);
                                    tempGP.add(btnAccept = new Button("Accept"), 1, 0);
                                    tempGP.add(btnDecline = new Button("Decline"), 2, 0);
                                    
                                    // Event Handling ng mga interactive GUIs. For every customer, may
                                    // sarili silang event handling, pero same yung methods.
                                    // Interactive GUIs START
                                    btnAccept.setOnAction(e -> {
                                        System.out.println("You pressed Accept for " 
                                                + dbAccount_ID + ", "
                                                + dbOrder_ID);
                                        try {
                                            updateSQLStatement("UPDATE orders SET Order_Status=\"Processing\" WHERE Order_ID=" + dbOrder_ID);
                                            run();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        } 
                                    });

                                    btnDecline.setOnAction(e -> {
                                        System.out.println("You pressed Decline for " 
                                                + dbAccount_ID + ", " 
                                                + dbOrder_ID);
                                    });
                                    // Interactive GUIs END
                                    
                                    // After setting up the format for that one customer, we add it to the GridPane List 
                                    gpList.add(tempGP);
                                }
                                
                                // After placing the format in a GridPane List, display all its contents in the VBox
                                vbb.getChildren().addAll(gpList);
                                // I-clear mo na para sa susunod na instance
                                gpList.clear();

//                                rs.close();
//                                st.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // Stop Refreshing every Two Seconds. Bumabagal PC kapag tuloy-tuloy siya.
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        };
        
        // Declaration of thread
        Thread th = new Thread(task);
        // KAILANGAN ITO PARA KAPAG CINLOSE MO YUNG APP, MAGSASARA RIN YUNG THREAD
        th.setDaemon(true);
        // Start na si thread.
        th.start();
    }
    
    private void selectSQLStatement(String query) throws SQLException {
        System.out.println(query);
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void updateSQLStatement(String query) throws SQLException {
        System.out.println(query);
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void confirmNewOrder(int choice) {
        
    }
    
}
