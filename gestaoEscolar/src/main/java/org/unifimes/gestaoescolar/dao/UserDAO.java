package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.Aluno;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.*;
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
                    List<Disciplina> disciplinas = disciplinaDAO.getDisciplinasByProfessorId(rs.getInt("id"));
                    User user = new User(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"), User.TipoUsuario.valueOf(rs.getString("tipo")), null, disciplinas);
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

    public boolean addUser(User user) {
        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            // Instrução para retornar o ID gerado
            PreparedStatement st = conection.getConexao().prepareStatement(
                    "INSERT INTO \"user\" (nome, cpf, senha, tipo) " +
                            "VALUES (?, ?, crypt(?, gen_salt('bf')), ?::tipo_usuario) RETURNING id;"
            );

            st.setString(1, user.getNome());
            st.setString(2, user.getCpf());
            st.setString(3, user.getSenha());
            st.setString(4, user.getTipo().name()); // usa .name() porque o tipo na tabela é ENUM

            ResultSet rs = st.executeQuery();
            int userId = -1;

            if (rs.next()) {
                userId = rs.getInt("id");
            }

            rs.close();
            st.close();

            // Se o ID foi recuperado com sucesso, associa as disciplinas
            if (userId > 0 && user.getDisciplinas() != null) {
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                for (Disciplina disciplina : user.getDisciplinas()) {
                    disciplinaDAO.addDisciplinaByUser(userId, disciplina.getId());
                }
            }

            return userId > 0;

        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(User user) {
        // exemplo básico de implementação
        String sql = "UPDATE alunos SET nome = ?, data_nascimento = ?, status = ? WHERE id = ?";
        // executar a query com os valores...

        return true;
    }

}
