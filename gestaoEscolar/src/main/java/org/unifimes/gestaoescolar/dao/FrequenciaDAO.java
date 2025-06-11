package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.Frequencia;
import org.unifimes.gestaoescolar.model.FrequenciaTable;
import org.unifimes.gestaoescolar.model.Turma;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FrequenciaDAO {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public boolean addMultipleFrequencia(List<Frequencia> frequencias){
        for (Frequencia frequencia : frequencias) {
            if(!addFrequencia(frequencia)){
                return false;
            }
        }

        return true;
    }

    public boolean addFrequencia(Frequencia frequencia) {
        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            PreparedStatement st = conection.getConexao().prepareStatement("INSERT INTO \"frequencia\" (aluno_id, disciplina_id, isfalta) VALUES (?, ?,?);");
            st.setInt(1, frequencia.getAlunoId());
            st.setInt(2, frequencia.getDisciplinaId());
            st.setBoolean(3, frequencia.isFalta());

            int rowsInserted = st.executeUpdate();

            st.close();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isFrequencia(int turma,int disciplina){
        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            PreparedStatement st = conection.getConexao().prepareStatement(
                    "select * from frequencia f join aluno a on a.id = f.aluno_id AND a.turma_id = ? where f.disciplina_id = ? AND f.createdate = ?"
            );
            LocalDate hoje = LocalDate.now();
            Date hojeFormat = java.sql.Date.valueOf(hoje.format(FORMATTER));

            st.setInt(1, turma);
            st.setInt(2, disciplina);
            st.setDate(3,hojeFormat);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return false;
            }


            st.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return true;
        }


    }
}
