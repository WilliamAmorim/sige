package org.unifimes.gestaoescolar.dao;

import org.unifimes.gestaoescolar.model.Disciplina;
import org.unifimes.gestaoescolar.model.User;
import org.unifimes.gestaoescolar.util.ConectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    public List<Disciplina> findDisciplina(Boolean isProf){
        List<Disciplina> disciplinas = new ArrayList<>();

        ConectionDB conection = new ConectionDB();
        conection.connect();
        PreparedStatement st;
        try {
            //if(isProf){
            //    st = conection.getConexao().prepareStatement("SELECT * FROM \"disciplina\" where ?");
            //}else{
            //}


            st = conection.getConexao().prepareStatement("SELECT * FROM \"disciplina\" ");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Disciplina disciplina = new Disciplina(rs.getInt("carga_horaria"),rs.getString("nome"));
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
