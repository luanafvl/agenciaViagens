package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.entities.PacoteViagem;
import model.entities.Servico;

public class ClientePacoteServicoDaoJDBC {
	
	Scanner sc = new Scanner(System.in);
	
    private Connection conn;

    public ClientePacoteServicoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    public void adicionarPacoteParaCliente(Integer idCliente, Integer idPacoteViagem) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
        	
            st = conn.prepareStatement(
            		"INSERT INTO rel_cliente_pacote "
            		+ "(id_cliente, id_pacote_viagem) "
            		+ "VALUES (?, ?)"
            		);
            
            st.setInt(1, idCliente);
            st.setInt(2, idPacoteViagem);

            st.executeUpdate();

        } 
        catch (SQLException e) {
            System.out.println("Erro ao adicionar pacote para cliente: " + e.getMessage());
        } 
        finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }


    public List<PacoteViagem> listarPacotesDoCliente(Integer idCliente) {
    	
    	List<PacoteViagem> pacotes = new ArrayList<>();
    	
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            
    		st = conn.prepareStatement(
					"SELECT "
					+ "pv.id_pacote_viagem, "
					+ "pv.nome "
					+ "FROM tb_pacote_viagem pv "
					+ "INNER JOIN rel_cliente_pacote r ON pv.id_pacote_viagem = r.id_pacote_viagem "
	        		+ "WHERE r.id_cliente = ?"
					);
    
            st.setInt(1, idCliente);
            rs = st.executeQuery();

            while (rs.next()) {
            	PacoteViagem pacote = new PacoteViagem();
            	pacote.setId(rs.getInt("id_pacote_viagem"));
                pacote.setNome(rs.getString("nome"));
                
                pacotes.add(pacote);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar pacotes do cliente: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                System.out.println("Erro finalizar: " + e.getMessage());
            }
        }
        return pacotes;
    }
    
    public void adicionarServicoAoPacoteCliente(Integer idCliente, Integer idPacote, Integer idServico) {
        
    	PreparedStatement st = null;
        ResultSet rs = null;

        try {
        	st = conn.prepareStatement(
                "INSERT INTO rel_cliente_pacote_servico "
                + "(id_cliente, id_pacote_viagem, id_servico) "
                + "VALUES (?, ?, ?)"
            );
            
            st.setInt(1, idCliente);
            st.setInt(2, idPacote);
            st.setInt(3, idServico);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao associar serviço ao pacote: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                System.out.println("Erro ao finalizar: " + e.getMessage());
            }
        }
    }
    
    
    public List<Servico> listarServicosDoClienteNoPacote(Integer idCliente, Integer idPacoteViagem) {

    	List<Servico> servicos = new ArrayList<>();
    	
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
        	
    	    st = conn.prepareStatement(
    	    		"SELECT "
    	    		+ "s.id_servico, "
	    			+ "s.servico "
	                + "FROM tb_servico s " 
	    			+ "INNER JOIN rel_cliente_pacote_servico r "
	    			+ "ON r.id_servico = s.id_servico " 
	                + "WHERE r.id_cliente = ? AND r.id_pacote_viagem = ?"
    	    		);
    	    
    	    st.setInt(1, idCliente);
    	    st.setInt(2, idPacoteViagem);

    	    rs = st.executeQuery();

    	    while (rs.next()) {
    	    	Servico servico = new Servico();
    	        servico.setId(rs.getInt("id_servico"));
    	        servico.setNome(rs.getString("servico"));
    	        
    	        servicos.add(servico);
    	    }
        } catch (SQLException e) {
            System.out.println("Erro ao listar serviços: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                System.out.println("Erro ao finalizar: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return servicos;
    }
}