package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.dao.PacoteViagemDao;
import model.entities.PacoteViagem;

public class PacoteViagemDaoJDBC implements PacoteViagemDao {

	Scanner sc = new Scanner(System.in);

	private Connection conn;
	
	public PacoteViagemDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(PacoteViagem pacoteViagem) {
		
		PreparedStatement st = null;
		
		if (conn != null) {
			try {
		
        		
				
				st = conn.prepareStatement(
						"INSERT INTO tb_pacote_viagem "
						+ "(nome, preco, descricao, duracao, id_destino, id_tipo_pacote_viagem) "
						+ "VALUES (?, ?, ?, ?, ?, ?)"
						);
				
				st.setString(1, pacoteViagem.getNome());
				st.setDouble(2, pacoteViagem.getPreco());
				st.setString(3, pacoteViagem.getDescricao());
				st.setInt(4, pacoteViagem.getDuracao());
				st.setInt(5, pacoteViagem.getIdDestino());
				st.setInt(6, pacoteViagem.getIdTipo());
				
				st.executeUpdate();
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao adicionar pacote: " + e.getMessage());
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
	public void update(Integer id, PacoteViagem pacoteViagem) {
		
		PreparedStatement st = null;
		
		if (conn != null) {
			try {
				
				st = conn.prepareStatement(
						"UPDATE tb_pacote_viagem "
						+ "SET nome = ?, preco = ?, descricao = ?, duracao = ?, id_destino = ?, id_tipo_pacote_viagem = ? "
						+ "WHERE id_pacote_viagem = ?"
						);
				
				st.setString(1, pacoteViagem.getNome());
				st.setDouble(2, pacoteViagem.getPreco());
				st.setString(3, pacoteViagem.getDescricao());
				st.setInt(4, pacoteViagem.getDuracao());
				st.setInt(5, pacoteViagem.getIdDestino());
				st.setInt(6, pacoteViagem.getIdTipo());
				st.setInt(7, id);
			
				st.executeUpdate();
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao atualizar pacote: " + e.getMessage());
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
						"DELETE FROM tb_pacote_viagem "
						+ "WHERE id_pacote_viagem = ?"
						);
				
				st.setInt(1, id);
				
				st.executeUpdate();
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao remover pacote: " + e.getMessage());
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
	public PacoteViagem findById(Integer id) {
		
		PacoteViagem pacoteViagem = null;
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(conn != null) {
			try {
			
				st = conn.prepareStatement(
						"SELECT "
						+ "pv.id_pacote_viagem, "
						+ "pv.nome, "
						+ "pv.preco, "
						+ "pv.descricao, "
						+ "pv.duracao, "
						+ "d.destino, "
						+ "tpv.tipo_pacote_viagem "
						+ "FROM tb_tipo_pacote_viagem tpv "
						+ "INNER JOIN tb_pacote_viagem pv "
						+ "ON pv.id_tipo_pacote_viagem = tpv.id_tipo_pacote_viagem "
						+ "INNER JOIN tb_destino d "
						+ "ON pv.id_destino = d.id_destino "
						+ "WHERE pv.id_pacote_viagem = ?"
						);

				st.setInt(1, id);
				rs = st.executeQuery();
				
				
				
				if (rs.next()) {
					pacoteViagem = new PacoteViagem();
					pacoteViagem.setId(rs.getInt("id_pacote_viagem"));
					pacoteViagem.setNome(rs.getString("nome"));
					pacoteViagem.setPreco(rs.getDouble("preco"));
					pacoteViagem.setDescricao(rs.getString("descricao"));
					pacoteViagem.setDuracao(rs.getInt("duracao"));
					pacoteViagem.setDestino(rs.getString("destino"));
					pacoteViagem.setTipo(rs.getString("tipo_pacote_viagem"));
				} else {
					return null;
				}
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar pacote: " + e.getMessage());
				return null;
			}
		}
		
		return pacoteViagem;
	}

	@Override
	public List<PacoteViagem> findAll() {
		
		List<PacoteViagem> pacotes = new ArrayList<>();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(conn != null) {
			
			try {
				
				st = conn.prepareStatement(
						"SELECT "
						+ "pv.id_pacote_viagem, "
						+ "pv.nome, "
						+ "pv.preco, "
						+ "pv.descricao, "
						+ "pv.duracao, "
						+ "d.destino, "
						+ "tpv.tipo_pacote_viagem "
						+ "FROM tb_tipo_pacote_viagem tpv "
						+ "INNER JOIN tb_pacote_viagem pv "
						+ "ON pv.id_tipo_pacote_viagem = tpv.id_tipo_pacote_viagem "
						+ "INNER JOIN tb_destino d "
						+ "ON pv.id_destino = d.id_destino "
						);
				
				rs = st.executeQuery();
				
				while(rs.next()) {
					PacoteViagem pv = new PacoteViagem();
		            pv.setId(rs.getInt("id_pacote_viagem"));
		            pv.setNome(rs.getString("nome"));
					pv.setPreco(rs.getDouble("preco"));
					pv.setDescricao(rs.getString("descricao"));
					pv.setDuracao(rs.getInt("duracao"));
					pv.setDestino(rs.getString("destino"));
					pv.setTipo(rs.getString("tipo_pacote_viagem"));
		            
					pacotes.add(pv);
				}
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar pacotes: " + e.getMessage());
				return new ArrayList<>();
			}
		}
		return pacotes;
	}
	
	
	@Override
	public Map<Integer, String> findAllDestinos() {
		
		Map<Integer, String> destinos = new HashMap<>();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(conn != null) {
			
			try {
				
				st = conn.prepareStatement(
						"SELECT "
						+ "id_destino, "
						+ "destino "
						+ "FROM tb_destino"
						);
				
				rs = st.executeQuery();
				
				while(rs.next()) {
					Integer idDestino = rs.getInt("id_destino");
		            String destino = rs.getString("destino");
		            
		            destinos.put(idDestino, destino);
				}
				
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar destinos: " + e.getMessage());
				return null;
			}
		}
		return destinos;
	}
	
	
	@Override
	public Map <Integer, String> findAllTipos() {
		
		Map<Integer, String> tipos = new HashMap<>();

		PreparedStatement st = null;
		ResultSet rs = null;
		
		if(conn != null) {
			
			try {
				
				st = conn.prepareStatement(
						"SELECT "
						+ "id_tipo_pacote_viagem, "
						+ "tipo_pacote_viagem "
						+ "FROM tb_tipo_pacote_viagem"
						);
				
				rs = st.executeQuery();
				
				while(rs.next()) {
					Integer idTipo = rs.getInt("id_tipo_pacote_viagem");
		            String tipo = rs.getString("tipo_pacote_viagem");
		            
		            tipos.put(idTipo, tipo);
		            
				}
			}
			catch (SQLException e) {
				System.out.println("Erro ao listar tipos de pacote: " + e.getMessage());
				return null;
			}
		}
		return tipos;
	}
}