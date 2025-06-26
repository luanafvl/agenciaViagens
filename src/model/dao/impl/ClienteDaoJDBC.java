package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.dao.ClienteDao;
import model.entities.Cliente;

public class ClienteDaoJDBC implements ClienteDao {

	Scanner sc = new Scanner(System.in);
	
	private Connection conn;
	
	public ClienteDaoJDBC (Connection conn) {
		this.conn = conn;
		
	}
	
	@Override
	public void insert(Cliente cliente) {
		
		PreparedStatement st = null;
		
		if (conn != null) {
			try {
				
				st = conn.prepareStatement("INSERT INTO tb_cliente "
						+ "(nome, telefone, email, cpf, passaporte, id_tipo_cliente) "
						+ "VALUES (?, ?, ?, ?, ?, ?);"
						);
				
				st.setString(1, cliente.getNome());
				st.setString(2, cliente.getTelefone());
				st.setString(3, cliente.getEmail());
				st.setString(4, cliente.getCpf());
				st.setString(5, cliente.getPassaporte());
				st.setInt(6, cliente.getIdTipo());
				
				st.executeUpdate();
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao criar cliente: " + e.getMessage());
			}
			finally {
				try {
					if (st != null) st.close();
				}
				catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		    }
		}
	}

	
	@Override
	public void update(Cliente cliente) {
		
    	PreparedStatement st = null;
		
		if (conn != null) {
			try {
				
				st = conn.prepareStatement(
						"UPDATE tb_cliente "
						+ "SET nome = ?, telefone = ?, email = ?, cpf = ?, passaporte = ?, id_tipo_cliente = ? "
						+ "WHERE id_cliente = ?"
						);
				
				st.setString(1, cliente.getNome());
				st.setString(2, cliente.getTelefone());
				st.setString(3, cliente.getEmail());
				st.setString(4, cliente.getCpf());
				st.setString(5, cliente.getPassaporte());
				st.setInt(6, cliente.getIdTipo());
				st.setInt(7, cliente.getId());
			
				st.executeUpdate();
			}
			catch (SQLException e) {
				System.out.println("Erro ao atualizar cliente: " + e.getMessage());
			}
			finally {	
				try {
					if (st != null) st.close();
				}
				catch (SQLException e) {
					System.out.println("Erro ao finalizar: " + e.getMessage());
				}
		    }
		}
	}
	

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		if(conn != null) {
			try {
				
				st = conn.prepareStatement(
						"DELETE FROM tb_cliente "
						+ "WHERE id_cliente = ? "
						);
				
				st.setInt(1, id);
				
				st.executeUpdate();
			}
			catch (SQLException e) {
				System.out.println("Erro ao remover cliente: " + e.getMessage());
			}
			finally {	
				try {
	            	if (st != null) st.close();
				}
				catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		    }
		}
	}

	
	@Override
	public Cliente findById(Integer id) {

		Cliente cli = null;
		
		PreparedStatement st = null;
		ResultSet rs = null;
		if(conn != null) {
			try {
				
				st = conn.prepareStatement(
						"SELECT "
						+ "c.id_cliente as id, "
						+ "c.nome as nome, "
						+ "c.telefone, "
						+ "c.email, "
						+ "c.cpf, "
						+ "c.passaporte, "
						+ "tc.tipo_cliente "
						+ "FROM tb_cliente c "
						+ "INNER JOIN tb_tipo_cliente tc "
						+ "ON c.id_tipo_cliente = tc.id_tipo_cliente "
						+ "WHERE c.id_cliente = ?"
						);
				
				st.setInt(1, id);
				
				rs = st.executeQuery();
				
				cli = new Cliente();
				
				if (rs.next()) {	
					cli.setId(rs.getInt("id"));
					cli.setNome(rs.getString("nome"));
					cli.setTelefone(rs.getString("telefone"));
					cli.setEmail(rs.getString("email"));
					cli.setCpf(rs.getString("cpf"));
					cli.setPassaporte(rs.getString("passaporte"));
					cli.setTipo(rs.getString("tipo_cliente"));
				} else {
					return null;
				}
			}
			catch (SQLException e) {
				System.out.println("Erro ao encontrar cliente: " + e.getMessage());
				return null;
			}
			finally {	
				try {
					if (rs != null) rs.close();
	            			if (st != null) st.close();
				}
				catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		    }
		}
		return cli;
	}

	
	@Override
	public List<Cliente> findAll() {
		
		List<Cliente> clientes = new ArrayList<>();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(conn!=null) {
			
			try {
				
				st = conn.prepareStatement(
						"SELECT "
						+ "c.id_cliente, "
						+ "c.nome, "
						+ "c.telefone, "
						+ "c.email, "
						+ "c.cpf, "
						+ "c.passaporte, "
						+ "c.id_tipo_cliente, "
						+ "tc.tipo_cliente "
						+ "FROM tb_cliente c "
						+ "INNER JOIN tb_tipo_cliente tc ON "
						+ "c.id_tipo_cliente = tc.id_tipo_cliente"
						);
						
				
				rs = st.executeQuery();
				
				while(rs.next()) {
					Cliente cli = new Cliente();
		            cli.setId(rs.getInt("id_cliente"));
		            cli.setNome(rs.getString("nome"));
		            cli.setTelefone(rs.getString("telefone"));
					cli.setEmail(rs.getString("email"));
					cli.setCpf(rs.getString("cpf"));
					cli.setPassaporte(rs.getString("passaporte"));
					cli.setIdTipo(rs.getInt("id_tipo_cliente"));
					cli.setTipo(rs.getString("tipo_cliente"));
		            
		            clientes.add(cli);
				}
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar clientes: " + e.getMessage());
				return new ArrayList<>()	;
			}
		}
		return clientes;
	}
	
	@Override
	public void findAllTiposCliente() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(conn != null) {
			
			try {
				
				st = conn.prepareStatement(
						"SELECT "
						+ "id_tipo_cliente, "
						+ "tipo_cliente "
						+ "FROM tb_tipo_cliente"
						);
				
				rs = st.executeQuery();
				
				StringBuilder tipoMsg = new StringBuilder("Tipos de clientes: ");
				
				while(rs.next()) {
		            Integer idTipo = rs.getInt("id_tipo_pacote_viagem");
		            String tipo = rs.getString("tipo_pacote_viagem");
		            
		            tipoMsg.append("\n").append(idTipo).append(" - ").append(tipo);
				}
				
				System.out.println(tipoMsg);
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar tipos de pacote: " + e.getMessage());
			}
		}
	}
}