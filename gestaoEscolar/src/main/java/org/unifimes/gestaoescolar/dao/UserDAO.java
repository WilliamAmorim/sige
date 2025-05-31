package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> getUser(){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<User> users = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            Connection conexao = conection.getConexao();
            if(conexao != null) {
                Statement st = conexao.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM \"user\" ");
                while (rs.next()) {
                    List<Disciplina> disciplinas = disciplinaDAO.getDisciplinasByUserId(rs.getInt("id"));
                    User user = new User(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"), rs.getString("tipo"), null, disciplinas);
                    users.add(user);
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return users;
    }

}
