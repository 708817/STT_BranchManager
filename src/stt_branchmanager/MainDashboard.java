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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author Dido
 */
public class MainDashboard {
    
    Scene mainScene;
    int adminBranch;
    
    Thread th, th2;
    
    // MySQL database variables
    private Connection con; // REF 1
    private Statement st; // REF 2
    private ResultSet rs; // REF 2
    
    VBox vbb; // REF 3 Eto pangdisplay lang sa buong List ng gpList
    VBox vbb2;
    VBox vbMain;
    TabPane tpThreads;
    Tab tab1;
    Tab tab2;
    List<GridPane> gpList; // REF 4 Eto lalagyan or lagayan ng mga GridPane. Each GridPane contains the Information Details mentioned above.
    List<GridPane> gpList2;
    Button btnAccept;
    Button btnDecline;
    Button btnSetAsDelivered;
    Button btnDetails;
    Button btnLogout;
    Label lblHeader;
    GridPane gpTest;
    HBox bs;
    
    public Scene mainMethod(Stage window) {
        System.out.println("MainDashboard.java -> mainMethod");
        
        showConfirmedOrders();
        showNewOrders();
        // Default GUI display declarations START
        vbb = new VBox(20); // REF 3
        vbb.setPadding(new Insets(30,20,10,20));
        vbb2 = new VBox(); 
        tpThreads = new TabPane();
        tpThreads.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        tab1 = new Tab("New Orders");
        tab1.setContent(vbb);
        tpThreads.getTabs().add(tab1);
        
        tab2 = new Tab("Confirmed Orders");
        tab2.setContent(vbb2);
        tpThreads.getTabs().add(tab2);
        
        lblHeader = new Label("GETMED");
        lblHeader.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> {
            try {
                rs.close();
                con.close();
                th.stop();
                th2.stop();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            AdminLogin scenes = new AdminLogin();
            window.setTitle("GetMed");
            window.setScene(scenes.loginMethod(window));
            window.show();
        });
        
        bs = new HBox();
        bs.getChildren().add(btnLogout);
        bs.setAlignment(Pos.CENTER_RIGHT);
        
        vbMain = new VBox(10);
        
        gpTest = new GridPane();
        gpTest.add(lblHeader, 0, 0);
        
        gpTest.add(bs, 1, 0);

        GridPane.setHgrow(bs, Priority.ALWAYS);
        
        tpThreads.tabMinWidthProperty().bind(vbMain.widthProperty().divide(tpThreads.getTabs().size()).subtract(20));

        mainScene = new Scene(vbMain, 440, 640);
        // Default GUI display declarations END

        // Creating Connectivity to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java3_project_stt?autoReconnect=true&useSSL=false", "root", "12345"); // REF 1
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mainScene;
    }
    
    private void showNewOrders() {
//        th.sto
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
                            vbMain.getChildren().clear();
                            // Declaration of new GridPane List
                            gpList = new ArrayList<>(); // REF 4
                            
                            try {
                                // REF 2 
                                selectSQLStatement("SELECT * FROM orders WHERE status=\"Pending\" AND branch_id=" + adminBranch);
                                System.out.println(adminBranch);
                                while (rs.next()) {
                                    
                                    // Dito nilalagay ang declarations ng mga information details,
                                    // so kung gusto niyo lagyan ng Address, declare nalang ng Label para
                                    // ilagay doon. REF 5
                                    // NOTE: dapat yung pangalan sa loob ng rs.getString(); may parehas doon
                                    // sa MySQL Database.
                                    
                                    int dbOrder_ID = rs.getInt("orders_id"),
                                            dbAccount_ID = rs.getInt("account_id"),
                                            dbBranch_ID = rs.getInt("branch_id"),
                                            dbPrescribed = rs.getInt("prescribed");
                                    String dbOrder_Status = rs.getString("status"),
                                            dbOrder_Items = rs.getString("order_items");
                                    String strPrescribed = "";
                                    
                                    if (dbPrescribed != 0) {
                                        strPrescribed = ", Prescription Needed";
                                    }
                                    
                                    Label detailsRow1 = new Label ("Order #" + dbOrder_ID);
                                    detailsRow1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                                    
                                    Label detailsRow2 = new Label (dbOrder_Status + strPrescribed);
                                    
                                    // Dito finoformat yung layout ng information detail ng isang customer.
                                    // Gamit ko GridPane since akala ko eto yung pinakamadaling i-format.
                                    VBox vbLeft = new VBox(5);
                                    vbLeft.getChildren().add(detailsRow1);
                                    vbLeft.getChildren().add(detailsRow2);
                                    
                                    HBox hbRight = new HBox(10);
                                    hbRight.getChildren().add(btnAccept = new Button("Accept"));
                                    hbRight.getChildren().add(btnDecline = new Button("Decline"));
                                    hbRight.getChildren().add(btnDetails = new Button("Details"));
                                    hbRight.setAlignment(Pos.TOP_RIGHT);
                                    
                                    GridPane tempGP = new GridPane();
                                    tempGP.add(vbLeft,0,0);
                                    tempGP.add(hbRight,1,0);
                                    tempGP.setHgap(10);
                                    tempGP.setVgap(5);
                                    tempGP.prefWidthProperty().bind(vbMain.widthProperty());
                                    
                                    GridPane.setHgrow(hbRight, Priority.ALWAYS);
                                    
                                    // Event Handling ng mga interactive GUIs. For every customer, may
                                    // sarili silang event handling, pero same yung methods.
                                    // Interactive GUIs START
                                    btnDetails.setOnAction(e -> {
                                        try {
                                            displayOrder(dbOrder_Items, dbAccount_ID, dbBranch_ID, dbOrder_ID, dbPrescribed);
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
                                vbMain.getChildren().add(gpTest);
                                vbMain.getChildren().add(tpThreads);
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
        th = new Thread(task);
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
                                selectSQLStatement("SELECT * FROM orders WHERE status=\"Processing\" AND branch_ID=" + adminBranch);
                                System.out.println(adminBranch);
                                while (rs.next()) {
                                    
                                    // Dito nilalagay ang declarations ng mga information details,
                                    // so kung gusto niyo lagyan ng Address, declare nalang ng Label para
                                    // ilagay doon. REF 5
                                    // NOTE: dapat yung pangalan sa loob ng rs.getString(); may parehas doon
                                    // sa MySQL Database.
                                    
                                    int dbOrder_ID = rs.getInt("orders_id"),
                                            dbAccount_ID = rs.getInt("account_id"),
                                            dbBranch_ID = rs.getInt("branch_id"),
                                            dbPrescribed = rs.getInt("prescribed");
                                    String dbOrder_Status = rs.getString("status"),
                                            dbOrder_Items = rs.getString("order_items");
                                    
                                    Label detailsRow1 = new Label (dbOrder_ID + ", " + dbAccount_ID + ", " + dbOrder_Status);
                                    Label detailsRow2 = new Label (dbOrder_Items);
                                    
                                    // Dito finoformat yung layout ng information detail ng isang customer.
                                    // Gamit ko GridPane since akala ko eto yung pinakamadaling i-format.
                                    GridPane tempGP = new GridPane();
                                    tempGP.add(detailsRow1, 0, 0);
                                    tempGP.add(detailsRow2, 0, 1);
                                    tempGP.add(btnSetAsDelivered = new Button("Set as Complete"), 1, 0);
                                    
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
        th2 = new Thread(task);
        // KAILANGAN ITO PARA KAPAG CINLOSE MO YUNG APP, MAGSASARA RIN YUNG THREAD
        th2.setDaemon(true);
        // Start na si thread.
        th2.start();
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
                
                selectSQLStatement("SELECT * FROM stock WHERE item_id=" + itemID + " AND branch_id=" + branch_id);
                while (rs.next()) {
                    price = rs.getDouble("Price");
                    quantity = rs.getInt("Quantity");
                }
                price *= orderQuantity;
                totalPrice += price;
                updateSQLStatement("UPDATE stock SET quantity=" + (quantity - orderQuantity) + " WHERE item_id=" + itemID + " AND branch_id=" + branch_id);
            }
        }
        updateSQLStatement("UPDATE orders SET status=\"" + newStatus + "\" WHERE orders_ID=" + order_id);
    }
    
    private void finishConfirmedOrder(int order_id, int acnt_id) {
        try {
            updateSQLStatement("UPDATE orders SET status=\"Delivered\" WHERE orders_id=" + order_id);
            System.out.println("You pressed Set as Delivered for "
                    + acnt_id + ", "
                    + order_id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void displayOrder(String order_itm, int accnt_id, int branch_id, int order_id, int prescribed) throws SQLException {
        double totalPrice = 0;
        List<String[]> list = new ArrayList<>();
        List<String[]> displayDetails = new ArrayList<>();
        List<String> customerDetails = new ArrayList<>();
        String[] splittedItems = order_itm.split(",");

        // Split each items orderd and put each item in one node of the List
        for (String iq : splittedItems) {
            list.add(iq.split("@"));
        }

        // Find each item in the Stock database and get its details
        for (String[] count : list) {
        String[] item = new String[4]; // Item Name, Original Price, Order Quantity, Final Price
            int itemID = Integer.parseInt(count[0]);
            int orderQuantity = Integer.parseInt(count[1]);
            double price = 0;
            double itemPrice = 0;
            String name = "";

            selectSQLStatement("SELECT * FROM stock WHERE item_id=" + itemID + " AND branch_id=" + branch_id);
            while (rs.next()) {
                price = rs.getDouble("Price");
                name = rs.getString("Item_Name");
            }
            itemPrice = price * orderQuantity;
            totalPrice += itemPrice;
            System.out.println(name + ", " + totalPrice);
            item[0] = name;
            item[1] = Double.toString(price);
            item[2] = Integer.toString(orderQuantity);
            item[3] = Double.toString(itemPrice);

            displayDetails.add(item);
            
        }
        
        selectSQLStatement("SELECT * FROM customer_account WHERE account_id=" + accnt_id);
        while (rs.next()) {
            String first = rs.getString("firstname"),
                    middle = rs.getString("middlename"),
                    last = rs.getString("lastname");
            
            String name = "";
            name += first;
            if (middle != null)
                name += " " + middle;
            name += " " + last;
            
            customerDetails.add(name);
            customerDetails.add(rs.getString("contactnumber"));
            customerDetails.add(rs.getString("email"));
        }
        
        selectSQLStatement("SELECT * FROM address WHERE account_id=" + accnt_id);
        while (rs.next()) {
            String address = "";
            String floor = "";
            String building = "";
            String housenumber = "";
            
            if (rs.getString("floor") != null) {
                floor = rs.getString("floor");
                address += floor + " Floor, ";
            }
            if (rs.getString("building") != null) {
                building = rs.getString("building");
                address += "Building " + building + ", ";
            }
            if (rs.getString("housenumber") != null) {
                housenumber = rs.getString("housenumber");
                address += housenumber + ", ";
            }
            address += rs.getString("street") + ", " + 
                    rs.getString("barangay") + ", " + rs.getString("city");
            
            customerDetails.add(address);
        }
        DetailsPopup.display(order_id, totalPrice, customerDetails, displayDetails, prescribed);
    }

}
