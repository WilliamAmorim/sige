package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.User;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> getUser(){
        List<User> users = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            Statement st = conection.getConexao().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM \"user\" ");
            while (rs.next()) {
                User user = new User(rs.getString("nome"),rs.getString("cpf"),rs.getString("tipo"),null,null);
                users.add(user);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return users;
    }

}
