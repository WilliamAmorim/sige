package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    public List<Disciplina> getDisciplinas(){
        List<Disciplina> disciplinas = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            Connection conexao = conection.getConexao();
            if(conexao != null) {
                st = conection.getConexao().prepareStatement("SELECT * FROM \"disciplina\" ");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Disciplina disciplina = new Disciplina(rs.getInt("id"), rs.getInt("carga_horaria"), rs.getString("nome"));
                    disciplinas.add(disciplina);
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return disciplinas;
    }

    public List<Disciplina> getDisciplinasByUserId(Integer id){
        List<Disciplina> disciplinas = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            st = conection.getConexao().prepareStatement("SELECT DISTINCT d.id, d.nome, d.carga_horaria\n" +
                                                                "FROM \"professor_turma_disciplina\" ptd \n" +
                                                                "JOIN \"disciplina\"  d ON ptd.disciplina_id = d.id\n" +
                                                                "WHERE ptd.professor_id = ? \n ");
            st.setInt(1,id);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Disciplina disciplina = new Disciplina(rs.getInt("id"),rs.getInt("carga_horaria"),rs.getString("nome"));
                disciplinas.add(disciplina);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return disciplinas;
    }
}
