package model.dao;

import java.util.List;
import java.util.Map;

import model.entities.PacoteViagem;

public interface PacoteViagemDao {

	/**
 	* Insere um novo pacote no banco de dados.
 	* @param pacote O objeto PacoteViagem a ser inserido.
 	*/
	void insert(PacoteViagem obj);
	
	/**
 	* Atualiza os dados de um pacote existente no banco.
 	* @param O id do PacoteViagem a ser atualizado e o objeto com os dados atualizados.
 	*/
	void update(Integer id, PacoteViagem obj);
	
	/**
 	* Remove um pacote do banco com base no ID.
 	* @param id O ID do pacote que deve ser removido.
 	*/
	void deleteById(Integer id);
	
	/**
	 * Busca um pacote pelo seu ID.
 	* @param id O ID do pacote que deve ser buscado.
 	* @return O objeto PacoteViagem correspondente, ou null se não encontrado.
 	*/
	PacoteViagem findById(Integer id);
	
	/**
	 * Retorna todos os pacotes cadastrados.
 	* @return Lista de PacoteViagem.
 	*/
	List<PacoteViagem> findAll();
	
	/**
	 * Retorna todos os destinos cadastrados.
 	* @return Map de Destino.
 	*/
	Map<Integer, String> findAllDestinos();
	
	/**
	 * Retorna todos os tipos de pacote cadastrados.
 	* @return Map de Tipo.
 	*/
	Map<Integer, String> findAllTipos();
}