package org.unifimes.gestaoescolar.dao;

import java.util.prefs.Preferences;

public class Session {
    private static final Preferences prefs = Preferences.userRoot().node("sige");

    public static boolean login(String cpf, String senha){
        if(cpf.equals("1234") && senha.equals("admin")){
            prefs.put("loggedUser","1");
            return true;
        }
        return false;
    }

    public static void logout() {
        prefs.remove("loggedUser");
    }

    public static String getLoggedUser() {
        return prefs.get("loggedUser", null); // retorna null se não existir
    }

    public static boolean isLoggedUser(){
        return prefs.get("loggedUser", null) != null;
    }
}
