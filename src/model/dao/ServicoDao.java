package model.dao;

import java.util.List;

import model.entities.Servico;

public interface ServicoDao {

	/**
 	* Insere um novo serviço no banco de dados.
 	* @param pacote O objeto Servico a ser inserido.
 	*/
	void insert(Servico obj);
	
	/**
 	* Atualiza os dados de um serviço existente no banco.
 	* @param O id do serviço a ser atualizado e o objeto com os dados atualizados.
 	*/
	void update(Integer id, Servico obj);
	
	/**
 	* Remove um servico do banco com base no ID.
 	* @param id O ID do servico que deve ser removido.
 	*/
	void deleteById(Integer id);
	
	/**
	 * Busca um serviço pelo seu ID.
 	* @param id O ID do serviço que deve ser buscado.
 	* @return O objeto Servico correspondente, ou null se não encontrado.
 	*/
	Servico findById(Integer id);
	
	/**
	 * Retorna todos os serviços cadastrados.
 	* @return Lista de Servico.
 	*/
	List<Servico> findAll();
}