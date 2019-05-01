/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stt_branchmanager;

/**
 *
 * @author Dido
 */
public class SQLAdmin {
    String username, password;
    
    // PLACEHOLDERS
    String phAdmin = "", phPass = "";
    int phBranch = 0;
    String phEmail = "";

    public boolean SQLAdmin(String username, String password) {
        System.out.println("DBAdmin.java -> DBAdmin");

        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        
        this.username = username;
        this.password = password;

        return loginAdmin(username, password);
    }

    private static boolean loginAdmin(java.lang.String user, java.lang.String pass) {
        stt.Getmed_Service service = new stt.Getmed_Service();
        stt.Getmed port = service.getGetmedPort();
        return port.loginAdmin(user, pass);
    }

}
