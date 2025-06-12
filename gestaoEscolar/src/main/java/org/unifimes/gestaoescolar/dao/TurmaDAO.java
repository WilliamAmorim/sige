package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.Aluno;
import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.Turma;
import org.unifimes.gestaoescolar.model.User;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
                    DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                    List<Disciplina> disciplinas = disciplinaDAO.getDisciplinasByTurmaId(rs.getInt("id"));
                    Turma turma = new Turma(rs.getInt("id"),rs.getString("nome"),rs.getString("turno"),rs.getInt("ano"),disciplinas);
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

    private String gerarCodigoTurma() {
        String ano = String.valueOf(LocalDate.now().getYear());
        int numeroAleatorio = new Random().nextInt(99999); // ex: 12345
        return ano + String.format("%05d", numeroAleatorio); // ex: 202512345
    }

    public boolean addTurma(Turma turma) {
        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            PreparedStatement st = conection.getConexao().prepareStatement(
                    "INSERT INTO \"turma\" (nome, turno, ano,codigo) " +
                            "VALUES (?,?,?,?) RETURNING id;"
            );

            st.setString(1, turma.getNome());
            st.setString(2, turma.getTurno());
            st.setInt(3, turma.getAno());
            st.setString(4,gerarCodigoTurma());


            ResultSet rs = st.executeQuery();
            int userId = -1;

            if (rs.next()) {
                userId = rs.getInt("id");
            }

            rs.close();
            st.close();

            // Se o ID foi recuperado com sucesso, associa as disciplinas
            if (userId > 0 && turma.getDisciplinas() != null) {
                DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
                for (Disciplina disciplina : turma.getDisciplinas()) {
                    disciplinaDAO.addDisciplinaByTurma(userId, disciplina.getId());
                }
            }

            return userId > 0;

        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTurma(Turma aluno) {
        // exemplo básico de implementação
        String sql = "UPDATE alunos SET nome = ?, data_nascimento = ?, status = ? WHERE id = ?";
        // executar a query com os valores...

        return true;
    }
}
