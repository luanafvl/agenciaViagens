package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.dao.ServicoDao;
import model.entities.Servico;

public class ServicoDaoJDBC implements ServicoDao {

	Scanner sc = new Scanner(System.in);
	
	private Connection conn;
	
	public ServicoDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Servico servico) {
		
		PreparedStatement st = null;
		
		if (conn != null) {
			try {
				st = conn.prepareStatement(
						"INSERT INTO tb_servico "
						+ "(servico, preco, descricao) "
						+ "VALUES (?, ?, ?)"
						);
				
				st.setString(1, servico.getNome());
				st.setDouble(2, servico.getPreco());
				st.setString(3, servico.getDescricao());
				
				st.executeUpdate();
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao adicionar serviço: " + e.getMessage());
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
	public void update(Integer id, Servico servico) {
		
		PreparedStatement st = null;
		
		if (conn != null) {
			try {
				
				st = conn.prepareStatement(
						"UPDATE tb_servico "
						+ "SET servico = ?, preco = ?, descricao = ? "
						+ "WHERE id_servico = ?"
						);
				
				st.setString(1, servico.getNome());
				st.setDouble(2, servico.getPreco());
				st.setString(3, servico.getDescricao());
				st.setInt(4, id);
				
				st.executeUpdate();
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao atualizar serviço: " + e.getMessage());
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
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		if(conn != null) {
			try {
				
				st = conn.prepareStatement(
						"DELETE FROM tb_servico "
						+ "WHERE id_servico = ?"
						);
				
				st.setInt(1, id);
				
				st.executeUpdate();
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao remover serviço: " + e.getMessage());
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
	public Servico findById(Integer id) {
		
		Servico servico = null;
		
		PreparedStatement st = null;
		ResultSet rs = null;
		if(conn != null) {
			try {
		
				st = conn.prepareStatement(
						"SELECT "
						+ "id_servico, "
						+ "servico, "
						+ "preco, "
						+ "descricao "
						+ "FROM tb_servico "
						+ "WHERE id_servico = ?"
						);

				st.setInt(1, id);
				
				rs = st.executeQuery();
				
				servico = new Servico();
				
				if (rs.next()) {
					servico.setId(rs.getInt("id_servico"));
					servico.setNome(rs.getString("servico"));
					servico.setPreco(rs.getDouble("preco"));
					servico.setDescricao(rs.getString("descricao"));
				} else {
					return null;
				}
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar serviço: " + e.getMessage());
				return null;
			}
		}
		return servico;
	}
	
	
	@Override
	public List<Servico> findAll() {
		
		List<Servico> servicos = new ArrayList<>();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(conn!=null) {
			
			try {
				
				st = conn.prepareStatement(
						"SELECT "
						+ "id_servico, "
						+ "servico, "
						+ "preco, "
						+ "descricao "
						+ "FROM tb_servico "
						);
				
				rs = st.executeQuery();
				
				
				
				while(rs.next()) {
					Servico servico = new Servico();
					servico.setId(rs.getInt("id_servico"));
		            servico.setNome(rs.getString("servico"));
		            servico.setPreco(rs.getDouble("preco"));
					servico.setDescricao(rs.getString("descricao"));
		            
		            servicos.add(servico);
				}
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar serviços: " + e.getMessage());
				return new ArrayList<>();
			}
		}
		return servicos;
	}
}