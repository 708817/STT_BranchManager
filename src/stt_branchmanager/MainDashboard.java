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
public class MainDashboard {
    
    Scene mainScene;
    int adminBranch;
    
    // MySQL database variables
    private Connection con; // REF 1
    private Statement st; // REF 2
    private ResultSet rs; // REF 2
    
    VBox vbb; // REF 3 Eto pangdisplay lang sa buong List ng gpList
    VBox vbb2;
    List<GridPane> gpList; // REF 4 Eto lalagyan or lagayan ng mga GridPane. Each GridPane contains the Information Details mentioned above.
    List<GridPane> gpList2;
    Button btnAccept;
    Button btnDecline;
    Button btnSetAsDelivered;
    Button btnDetails;
    
    public Scene mainMethod(Stage window) {
        System.out.println("MainDashboard.java -> mainMethod");
        
        // Default GUI display declarations START
        vbb = new VBox(); // REF 3
        vbb2 = new VBox(); 

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
//        showConfirmedOrders();
        return mainScene;
    }
    
    private void showNewOrders() {
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
                                selectSQLStatement("SELECT * FROM orders WHERE Order_Status=\"Pending\" AND Branch_ID=" + adminBranch);
                                System.out.println(adminBranch);
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
                                    tempGP.add(btnDetails = new Button("More Details..."), 0, 2);
                                    tempGP.add(btnAccept = new Button("Accept"), 2, 0);
                                    tempGP.add(btnDecline = new Button("Decline"), 3, 0);
                                    
                                    // Event Handling ng mga interactive GUIs. For every customer, may
                                    // sarili silang event handling, pero same yung methods.
                                    // Interactive GUIs START
                                    btnDetails.setOnAction(e -> {
                                        try {
                                            displayOrder(dbOrder_Items, dbAccount_ID, dbBranch_ID, dbOrder_ID);
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                        run();
                                    });
                                    
                                    btnAccept.setOnAction(e -> {
                                        try {
                                            confirmNewOrder(1, dbOrder_ID, dbAccount_ID, dbOrder_Items, dbBranch_ID);
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                        run();
                                    });

                                    btnDecline.setOnAction(e -> {
                                        try {
                                            confirmNewOrder(2, dbOrder_ID, dbAccount_ID, "", dbBranch_ID);
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                        run();
                                    });
                                    
                                    // Interactive GUIs END
                                    
                                    // After setting up the format for that one customer, we add it to the GridPane List 
                                    gpList.add(tempGP);
                                }
                                
                                // After placing the format in a GridPane List, display all its contents in the VBox
                                vbb.getChildren().addAll(gpList);
                                // I-clear mo na para sa susunod na instance
                                gpList.clear();

                                rs.close();
                                st.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // Stop Refreshing every Ten Seconds. Bumabagal PC kapag tuloy-tuloy siya.
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
    
    private void showConfirmedOrders() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            
                        // LAHAT NG KAILANGAN NAG-UUPDATE SA GUI FOR EVERY TWO SECONDS, LAGAY DITO
                            
                            // For every instance, i-clear yung laman ng Vbox 
                            vbb2.getChildren().clear();
                            // Declaration of new GridPane List
                            gpList2 = new ArrayList<>(); // REF 4
                            
                            try {
                                // REF 2 
                                selectSQLStatement("SELECT * FROM orders WHERE Order_Status=\"Processing\" AND Branch_ID=" + adminBranch);
                                System.out.println(adminBranch);
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
                                    tempGP.add(btnSetAsDelivered = new Button("Set as Delivered"), 1, 0);
                                    
                                    // Event Handling ng mga interactive GUIs. For every customer, may
                                    // sarili silang event handling, pero same yung methods.
                                    // Interactive GUIs START
                                    btnSetAsDelivered.setOnAction(e -> {
                                        finishConfirmedOrder(dbOrder_ID, dbAccount_ID);
                                        run();
                                    });

                                    // Interactive GUIs END
                                    
                                    // After setting up the format for that one customer, we add it to the GridPane List 
                                    gpList2.add(tempGP);
                                }
                                
                                // After placing the format in a GridPane List, display all its contents in the VBox
                                vbb2.getChildren().addAll(gpList2);
                                // I-clear mo na para sa susunod na instance
                                gpList2.clear();

                                rs.close();
                                st.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    // Stop Refreshing every Ten Seconds. Bumabagal PC kapag tuloy-tuloy siya.
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
    
    private void confirmNewOrder(int choice, int order_id, int acnt_id, 
            String order_itm, int branch_id) throws SQLException {
        
        String newStatus = "";
        double totalPrice = (double)0.00;
        
        switch (choice) {
            case 1:
                newStatus = "Processing";
                System.out.println("You pressed Accept for "
                        + acnt_id + ", "
                        + order_id);
                break;
            case 2:
                newStatus = "Cancelled";
                System.out.println("You pressed Decline for "
                        + acnt_id + ", "
                        + order_id);
                break;
        }
        
        if (!order_itm.equals("")) {
            List<String[]> list = new ArrayList<>();
            String[] splittedItems = order_itm.split(",");

            for (String iq : splittedItems) {
                list.add(iq.split("@"));
            }

            for (String[] count : list) {
                int itemID = Integer.parseInt(count[0]);
                int orderQuantity = Integer.parseInt(count[1]);
                int quantity = 0;
                double price = 0;
                
                selectSQLStatement("SELECT * FROM stock WHERE Item_ID=" + itemID + "AND Branch_ID=" + branch_id);
                while (rs.next()) {
                    price = rs.getDouble("Price");
                    quantity = rs.getInt("Quantity");
                }
                price *= orderQuantity;
                totalPrice += price;
                updateSQLStatement("UPDATE stock SET Quantity=" + (quantity - orderQuantity) + " WHERE Item_ID=" + itemID + "AND Branch_ID=" + branch_id);
            }
        }
        updateSQLStatement("UPDATE orders SET Order_Status=\"" + newStatus + "\" WHERE Order_ID=" + order_id);
    }
    
    private void finishConfirmedOrder(int order_id, int acnt_id) {
        try {
            updateSQLStatement("UPDATE orders SET Order_Status=\"Delivered\" WHERE Order_ID=" + order_id);
            System.out.println("You pressed Set as Delivered for "
                    + acnt_id + ", "
                    + order_id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void displayOrder(String order_itm, int accnt_id, int branch_id, int order_id) throws SQLException {
        int totalPrice = 0;
        List<String[]> list = new ArrayList<>();
        List<String[]> displayDetails = new ArrayList<>();
        List<String> customerDetails = new ArrayList<>();
        String[] splittedItems = order_itm.split(",");
        String[] item = new String[4]; // Item Name, Original Price, Order Quantity, Final Price

        // Split each items orderd and put each item in one node of the List
        for (String iq : splittedItems) {
            list.add(iq.split("@"));
        }

        // Find each item in the Stock database and get its details
        for (String[] count : list) {
            int itemID = Integer.parseInt(count[0]);
            int orderQuantity = Integer.parseInt(count[1]);
            double price = 0;
            double itemPrice = 0;
            String name = "";

            selectSQLStatement("SELECT * FROM stock WHERE Item_ID=" + itemID + " AND Branch_ID=" + branch_id);
            while (rs.next()) {
                price = rs.getDouble("Price");
                name = rs.getString("Item_Name");
            }
            itemPrice = price * orderQuantity;
            totalPrice += price;
            
            item[0] = name;
            item[1] = Double.toString(price);
            item[2] = Integer.toString(orderQuantity);
            item[3] = Double.toString(itemPrice);

            displayDetails.add(item);
        }

        selectSQLStatement("SELECT * FROM customer_account WHERE Account_ID=" + accnt_id);
        while (rs.next()) {
            customerDetails.add(rs.getString("Name"));
            customerDetails.add(rs.getString("Email"));
            customerDetails.add(rs.getString("ContactNumber"));
            customerDetails.add(rs.getString("Address"));
        }
        DetailsPopup.display(order_id, totalPrice, customerDetails, displayDetails);
    }

}
