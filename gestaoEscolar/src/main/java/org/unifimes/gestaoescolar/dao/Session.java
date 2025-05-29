package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    }

    public static String getLoggedUser() {
        return prefs.get("loggedUser", null); // retorna null se não existir
    }

    public static boolean isLoggedUser(){
        return prefs.get("loggedUser", null) != null;
    }
}
