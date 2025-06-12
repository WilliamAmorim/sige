package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.*;
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
            PreparedStatement st = conection.getConexao().prepareStatement("INSERT INTO \"frequencia\" (aluno_id, disciplina_id, isfalta,bimestre) VALUES (?, ?,?,?);");
            st.setInt(1, frequencia.getAlunoId());
            st.setInt(2, frequencia.getDisciplinaId());
            st.setBoolean(3, frequencia.isFalta());
            st.setInt(4,frequencia.getBimestre());

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
                    "SELECT EXISTS (" +
                            "  SELECT 1 FROM frequencia f " +
                            "  JOIN aluno a ON a.id = f.aluno_id " +
                            "  WHERE a.turma_id = ? AND f.disciplina_id = ? AND f.createdate = ?" +
                            ")"
            );
            LocalDate hoje = LocalDate.now();
            Date hojeFormat = java.sql.Date.valueOf(hoje.format(FORMATTER));

            st.setInt(1, turma);
            st.setInt(2, disciplina);
            st.setDate(3,hojeFormat);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    boolean existe = rs.getBoolean(1);
                    return !existe;  // **INVERTIDO**: se existe retorna false, se não existe retorna true
                }
            }


            st.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return true;
        }
    }

    public List<FrequenciaChart> getGraficoFrequencia(Integer turma, Integer disciplina, Integer bimestre) {
        List<FrequenciaChart> frequencia = new ArrayList<>();
        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT\n" +
                            "    a.id AS aluno_id,\n" +
                            "    a.nome AS nome_aluno,\n" +
                            "    f.bimestre,\n" +
                            "    SUM(f.faltas) AS total_faltas,\n" +
                            "    COUNT(*) AS total_registros_falta\n" +
                            "FROM frequencia f\n" +
                            "JOIN aluno a ON f.aluno_id = a.id\n" +
                            "JOIN turma t ON a.turma_id = t.id\n" +
                            "WHERE f.isfalta = true\n"
            );

            List<Object> params = new ArrayList<>();

            if (bimestre != 0) {
                sql.append(" AND f.bimestre = ?\n");
                params.add(bimestre);
            }
            if (disciplina != 0) {
                sql.append(" AND f.disciplina_id = ?\n");
                params.add(disciplina);
            }
            if (turma != 0) {
                sql.append(" AND t.id = ?\n");
                params.add(turma);
            }

            sql.append("GROUP BY a.id, a.nome, f.bimestre\n");
            sql.append("ORDER BY a.nome, f.bimestre");

            PreparedStatement st = conection.getConexao().prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));  // cuidado com o índice (começa em 1)
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                FrequenciaChart frequenciaChart = new FrequenciaChart(
                        rs.getInt("aluno_id"),
                        rs.getString("nome_aluno"),
                        rs.getInt("bimestre"),
                        rs.getInt("total_registros_falta")
                );
                frequencia.add(frequenciaChart);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error DAO:" + e);
        }

        return frequencia;
    }

}
