package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.Turma;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    public List<Turma> getTurmas(){
        List<Turma> turmas = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            Connection conexao = conection.getConexao();
            if(conexao != null) {
                st = conection.getConexao().prepareStatement("SELECT * FROM \"turma\" ");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Turma turma = new Turma(rs.getInt("id"),rs.getString("codigo"),rs.getString("turno"),rs.getInt("ano"),null);
                    turmas.add(turma);
                }
                rs.close();
                st.close();
            }
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return turmas;
    }
}
