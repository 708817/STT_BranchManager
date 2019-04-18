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
class DBAdmin {
    String username, password;
    String admin = "", pass = "";

    public boolean DBAdmin(String username, String password) {
        System.out.println("DBAdmin.java -> DBAdmin");

        this.username = username;
        this.password = password;

        return loginAdmin();
    }

    private boolean loginAdmin() {
        System.out.println("DBAdmin.java -> loginAdmin");
        boolean checker;
        // insert db method start
        admin = "";
        pass = "";
        // insert db method end
        if (username.equals(admin)
                && password.equals(pass)) {
            checker = true;
        } else {
            checker = false;
        }
        
        return checker;
    }
}
