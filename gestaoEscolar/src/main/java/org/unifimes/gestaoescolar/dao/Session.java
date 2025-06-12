package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Session {
    private static final Preferences prefs = Preferences.userRoot().node("sige");

    public static boolean loginDAO(String cpf, String senha,Boolean rememberMe){
        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            PreparedStatement st = conection.getConexao().prepareStatement("SELECT * FROM \"user\" where \"cpf\" = ? and \"senha\" = crypt(?, senha) ");
            st.setString(1, cpf);
            st.setString(2,senha);

            ResultSet rs = st.executeQuery();

            while(rs.next()){
                if(rememberMe){
                    prefs.put("loggedUser",rs.getString("id"));

                }
                prefs.remove("tipoUser");
                prefs.remove("userId");
                prefs.put("tipoUser",rs.getString("tipo"));
                prefs.put("userId", String.valueOf(rs.getInt("id")));

                return true;
            }

            rs.close();
            st.close();

            return false;
        } catch (SQLException e) {
            return false;
        }

    }

    public static void logout() {
        prefs.remove("loggedUser");
        prefs.remove("tipoUser");
        prefs.remove("userId");
    }

    public static String getLoggedUser() {
        return prefs.get("loggedUser", null);
    }

    public static boolean isLoggedUser(){
        return prefs.get("loggedUser", null) != null;
    }

    public static String userLoggedId(){
        return prefs.get("userId", null);
    }

    public static boolean isLoggedAdmin(){
        System.out.println("Tipo Usuário:"+prefs.get("tipoUser", null));
        if(Objects.equals(prefs.get("tipoUser", null), "ADMIN")) {
            return true;
        }
        return false;
    }
}
