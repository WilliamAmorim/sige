package org.unifimes.gestaoescolar.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;


public class ConectionDB {
    private Connection conexao = null;

    public boolean connect() {
        try {
            String url = "jdbc:postgresql://localhost:5432/sige";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "1234");
            props.setProperty("ssl", "false"); // desative SSL para ambiente local

            this.conexao = DriverManager.getConnection(url, props);
            System.out.println("Conexão bem-sucedida!");
            return true;
        } catch (Exception ex) {
            System.out.println("Erro de conexão: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public Connection getConexao() {
        return this.conexao;
    }
}
