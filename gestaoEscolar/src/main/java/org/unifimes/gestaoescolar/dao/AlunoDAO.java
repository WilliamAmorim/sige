package org.unifimes.gestaoescolar.dao;

import com.sun.tools.jconsole.JConsoleContext;
import org.postgresql.util.PGobject;
import org.unifimes.gestaoescolar.model.*;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AlunoDAO {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Aluno> getAlunosByTurma(Integer turma){
        List<Aluno> alunos = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            st = conection.getConexao().prepareStatement("SELECT * FROM \"aluno\" where turma_id = ? ");
            st.setInt(1,turma);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Aluno aluno = new Aluno(rs.getInt("id"),rs.getString("nome"),rs.getString("matricula"), rs.getString("data_nascimento"),rs.getString("turma_id"),true);
                alunos.add(aluno);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return alunos;
    }

    public boolean addAluno(Aluno aluno) {

        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            PreparedStatement st = conection.getConexao().prepareStatement(
                    "INSERT INTO \"aluno\" (nome, matricula, data_nascimento, status, turma_id) VALUES (?, ?, ?, ?, ?);"
            );

            DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(aluno.getDataNascimento(), FORMATTER);
            java.sql.Date dataFormat = java.sql.Date.valueOf(data);

            // Criando o enum como PGobject
            PGobject status = new PGobject();
            status.setType("status_aluno"); // Nome do ENUM no PostgreSQL
            status.setValue(aluno.isAtivo() ? "ATIVO" : "INATIVO");

            st.setString(1, aluno.getNome());
            st.setString(2, gerarMatriculaAutomatica());
            st.setDate(3, dataFormat);
            st.setObject(4, status);  // aqui vai o enum corretamente
            st.setInt(5, Integer.parseInt(aluno.getTurma()));

            int rowsInserted = st.executeUpdate();

            st.close();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }

    public String gerarMatriculaAutomatica() {
        String ano = String.valueOf(LocalDate.now().getYear());
        int numeroAleatorio = new Random().nextInt(99999); // ex: 12345
        return ano + String.format("%05d", numeroAleatorio); // ex: 202512345
    }

    public boolean addAlunoNota(Nota nota){

        ConectionDB conection = new ConectionDB();
        conection.connect();

        try {
            PreparedStatement st = conection.getConexao().prepareStatement(
                    "INSERT INTO \"nota\" (aluno_id, disciplina_id,bimestre,valor) VALUES (?,?,?,?);"
            );

            st.setInt(1, nota.getAlunoId());
            st.setInt(2, nota.getDisciplinaId());
            st.setInt(3, nota.getBimestre());
            st.setDouble(4, nota.getValor());

            int rowsInserted = st.executeUpdate();

            st.close();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }

    public List<NotaTable> getAlunosNotaByTurma(Integer turma,Integer disciplina){
        List<NotaTable> notas = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            st = conection.getConexao().prepareStatement("SELECT\n" +
                    "    a.id AS aluno_id,\n" +
                    "    a.nome AS nome_aluno,\n" +
                    "    a.matricula,\n" +
                    "    t.nome AS turma,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 1 THEN n.valor END), 0) AS nota01,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 2 THEN n.valor END), 0) AS nota02,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 3 THEN n.valor END), 0) AS nota03,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 4 THEN n.valor END), 0) AS nota04\n" +
                    "FROM aluno a\n" +
                    "JOIN turma t ON a.turma_id = t.id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        n.*,\n" +
                    "        ROW_NUMBER() OVER (\n" +
                    "            PARTITION BY n.aluno_id\n" +
                    "            ORDER BY n.created ASC\n" +
                    "        ) AS rn\n" +
                    "    FROM nota n\n" +
                    "    WHERE n.created >= ? AND n.created <= ? AND n.disciplina_id = ? \n" +
                    ") n ON a.id = n.aluno_id\n" +
                    "WHERE t.id = ?\n" +
                    "GROUP BY a.id, a.nome, a.matricula, t.nome\n" +
                    "ORDER BY a.nome;");

            Date inicio = java.sql.Date.valueOf(getInicioSemestreAtual());
            Date fim = java.sql.Date.valueOf(getFimSemestreAtual());

            st.setDate(1, inicio);
            st.setDate(2, fim);
            st.setInt(3, disciplina);
            st.setInt(4, turma);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                System.out.println("Nome:"+rs.getInt("aluno_id"));
                double media = (rs.getDouble("nota01")+rs.getDouble("nota02")+rs.getDouble("nota03")+rs.getDouble("nota04")) / 4;
                NotaTable nota = new NotaTable(rs.getInt("aluno_id"),rs.getString("nome_aluno"),rs.getString("matricula"),media,rs.getDouble("nota01"),rs.getDouble("nota02"),rs.getDouble("nota03"),rs.getDouble("nota04"));
                notas.add(nota);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return notas;
    }

    public List<NotaTable> getAlunoBoletimByTurma(Integer turma,Integer aluno,Integer bimestre){
        String bimestreQuery = "";
        if(bimestre > 0){
            bimestreQuery = "   AND n.bimestre = ?  \n";
        }
        List<NotaTable> notas = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            st = conection.getConexao().prepareStatement("SELECT\n" +
                    "    d.nome AS disciplina,\n" +
                    "    a.id AS aluno_id,\n" +
                    "    a.nome AS nome_aluno,\n" +
                    "    a.matricula,\n" +
                    "    t.nome AS turma,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 1 THEN n.valor END), 0) AS nota01,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 2 THEN n.valor END), 0) AS nota02,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 3 THEN n.valor END), 0) AS nota03,\n" +
                    "    COALESCE(MAX(CASE WHEN rn = 4 THEN n.valor END), 0) AS nota04\n" +
                    "FROM aluno a\n" +
                    "JOIN turma t ON a.turma_id = t.id\n" +
                    "JOIN turma_disciplina td ON t.id = td.turma_id\n" +
                    "JOIN disciplina d ON td.disciplina_id = d.id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        n.*,\n" +
                    "        ROW_NUMBER() OVER (\n" +
                    "            PARTITION BY n.aluno_id, n.disciplina_id\n" +
                    "            ORDER BY n.created ASC\n" +
                    "        ) AS rn\n" +
                    "    FROM nota n\n" +
                    "    WHERE n.created >= ? AND n.created <= ? \n" +
                    bimestreQuery+
                    ") n ON a.id = n.aluno_id AND d.id = n.disciplina_id\n" +
                    "WHERE t.id = ? AND a.id = ?\n" +
                    "GROUP BY d.nome, a.id, a.nome, a.matricula, t.nome\n" +
                    "ORDER BY d.nome;\n");

            Date inicio = java.sql.Date.valueOf(getInicioSemestreAtual());
            Date fim = java.sql.Date.valueOf(getFimSemestreAtual());

            if(bimestre > 0){
                st.setDate(1, inicio);
                st.setDate(2, fim);
                st.setInt(3, bimestre);
                st.setInt(4, turma);
                st.setInt(5, aluno);
            }else{
                st.setDate(1, inicio);
                st.setDate(2, fim);
                st.setInt(3, turma);
                st.setInt(4, aluno);
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                System.out.println("Nome:"+rs.getInt("aluno_id"));
                double media = (rs.getDouble("nota01")+rs.getDouble("nota02")+rs.getDouble("nota03")+rs.getDouble("nota04")) / 4;
                NotaTable nota = new NotaTable(rs.getInt("aluno_id"),rs.getString("disciplina"),rs.getString("matricula"),media,rs.getDouble("nota01"),rs.getDouble("nota02"),rs.getDouble("nota03"),rs.getDouble("nota04"));
                notas.add(nota);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return notas;
    }

    public String[] getTotalAlunos(int turma,int bimestre,List<Disciplina> disciplinas,List<Aluno> alunos){
        int totalAprovados = 0;
        int totalReprovados = 0;
        for(Aluno aluno : alunos){
                List<NotaTable> boletim = getAlunoBoletimByTurma(turma,aluno.getId(),bimestre);
                if(isAprovadoEmTodasAsDisciplinas(boletim)){
                    totalAprovados++;
                }else{
                    totalReprovados++;
                }
        }
        return new String[]{String.valueOf(alunos.size()), String.valueOf(totalAprovados), String.valueOf(totalReprovados)};
    }

    public boolean isAprovadoEmTodasAsDisciplinas(List<NotaTable> notas) {
        for (NotaTable nota : notas) {
            if (nota.getMedia() < 6.0) {
                return false; // encontrou uma disciplina com média menor que 6
            }
        }
        return true; // todas as médias são maiores ou iguais a 6
    }


    public String[] getTotalAlunos(int turma,Integer bimestre,Integer disciplina){
        String[] dados = {"","","",""};

        String bimestreQuery = "";
        if(bimestre > 0){
            bimestreQuery += "   AND n.bimestre = ?  \n";
        }
        if(disciplina > 0){
            bimestreQuery += "   AND n.disciplina_id = ?  \n";
        }

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            st = conection.getConexao().prepareStatement("SELECT\n" +
                    "    t.nome AS turma,\n" +
                    "    COUNT(DISTINCT a.id) AS total_alunos,\n" +
                    "    COUNT(DISTINCT CASE WHEN COALESCE(notas.media_geral, 0) >= 6 THEN a.id END) AS total_aprovados,\n" +
                    "    COUNT(DISTINCT CASE WHEN COALESCE(notas.media_geral, 0) < 6 THEN a.id END) AS total_reprovados\n" +
                    "FROM aluno a\n" +
                    "JOIN turma t ON a.turma_id = t.id\n" +
                    "LEFT JOIN (\n" +
                    "    SELECT\n" +
                    "        n.aluno_id,\n" +
                    "        AVG(n.valor) AS media_geral\n" +
                    "    FROM nota n\n" +
                    "    WHERE n.created >= ? AND n.created <= ?\n" +
                    bimestreQuery+
                    "    GROUP BY n.aluno_id\n" +
                    ") notas ON a.id = notas.aluno_id\n" +
                    "WHERE t.id = ?\n" +
                    "GROUP BY t.nome;");

            Date inicio = java.sql.Date.valueOf(getInicioSemestreAtual());
            Date fim = java.sql.Date.valueOf(getFimSemestreAtual());
            if(bimestre > 0 && disciplina > 0) {
                st.setDate(1, inicio);
                st.setDate(2, fim);
                st.setInt(3, bimestre);
                st.setInt(4, disciplina);
                st.setInt(5, turma);

            }else if(bimestre > 0) {
                st.setDate(1, inicio);
                st.setDate(2, fim);
                st.setInt(3, bimestre);
                st.setInt(4, turma);

            }else if(disciplina > 0) {
                st.setDate(1, inicio);
                st.setDate(2, fim);
                st.setInt(3, disciplina);
                st.setInt(4, turma);

            }else{
                st.setDate(1, inicio);
                st.setDate(2, fim);
                st.setInt(3, turma);
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                dados[0] = rs.getString("total_alunos");
                dados[1] = rs.getString("total_aprovados");
                dados[2] = rs.getString("total_reprovados");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error DAO:"+e);
        }

        return dados;
    }

    private static String getInicioSemestreAtual() {
        LocalDate hoje = LocalDate.now();
        int ano = hoje.getYear();

        LocalDate inicio = (hoje.getMonthValue() <= 6)
                ? LocalDate.of(ano, 1, 1)
                : LocalDate.of(ano, 7, 1);

        return inicio.format(FORMATTER);
    }

    private static String getFimSemestreAtual() {
        LocalDate hoje = LocalDate.now();
        int ano = hoje.getYear();

        LocalDate fim = (hoje.getMonthValue() <= 6)
                ? LocalDate.of(ano, 6, 30)
                : LocalDate.of(ano, 12, 31);

        return fim.format(FORMATTER);
    }

    public boolean updateAluno(Aluno aluno) {
        return true;
    }
}
