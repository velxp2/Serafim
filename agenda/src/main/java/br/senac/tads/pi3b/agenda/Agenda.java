/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3b.agenda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 
 * @author victor.gserafim
 * @version 1.0
 */
public class Agenda {
    
    private Connection obterConexao()
            throws ClassNotFoundException, SQLException {
        Connection conn = null;

        //Passo 1: Registrar driver JDBC
        
        Class.forName("com.mysql.jdbc.Driver");
        //Passo 2: Obter a conexao
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/agendabd",
                "root",
                "");
        return conn;
    }
    
    
    public void executar() {

        String sql = "SELECT ID, NOME, DTNASCIMENTO FROM PESSOA";
        
        try (Connection conn = obterConexao();
            PreparedStatement preState = conn.prepareStatement(sql);
            ResultSet resultados = preState.executeQuery()){
            
                {
                    
            while (resultados.next()) {
                    long id = resultados.getLong("ID");
                    String nome = resultados.getString("NOME");
                    Date dtNascimento = resultados.getDate("DTNASCIMENTO");
                    System.out.println(id + " - " + nome + " - " + dtNascimento);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inserir(String nome, String datNasc )
            throws SQLException, Exception {
        String sql = "INSERT INTO PESSOA (NOME, DTNASCIMENTO) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement preState = null;
        try {
            conn = obterConexao();
            preState = conn.prepareStatement(sql);
            preState.setString(1, nome);
            preState.setString(2, datNasc);
            preState.execute();
        } finally {
            if (preState != null && !preState.isClosed()) {
                preState.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
    
    public void excluir(String nome) throws SQLException, Exception {
        String sql = "DELETE FROM PESSOA WHERE NOME=?";
        Connection conn = null;
        PreparedStatement preState = null;
        try {
            conn = obterConexao();
            preState = conn.prepareStatement(sql);
            preState.setString(1, nome);

            preState.execute();
        } finally {
            if (preState != null && !preState.isClosed()) {
                preState.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        
        Scanner ent = new Scanner(System.in);
        Agenda agenda = new Agenda();
        System.out.print("\nQuantidade de nomes a serem adicionados: ");
        String n = ent.nextLine();
        int num = Integer.parseInt(n);
        String nome, datNasc;
        for (int i = 0; i < num; i++) {
            System.out.print("Nome: ");
            nome = ent.nextLine();
            System.out.print("Data de nascimento (Ex: 2000-01-01):");
            datNasc = ent.nextLine();
            agenda.inserir(nome, datNasc);
        }
        System.out.println();
        agenda.executar();
        
        System.out.print("\nDigite uma pessoa a ser excluida:");
        nome = ent.nextLine();
        agenda.excluir(nome);
        agenda.executar();
        
        for (int i = 0; i < 10; i++) {
            System.out.println("Número: "+i);
        }
    }
}
