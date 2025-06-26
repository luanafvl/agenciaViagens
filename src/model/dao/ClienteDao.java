// Define o pacote onde está a interface
package model.dao;

import java.util.List;

import model.entities.Cliente;

// Interface que representa o contrato para operações de acesso a dados da entidade Cliente
public interface ClienteDao {

	/**
    * Insere um novo cliente no banco de dados.
    * @param obj O objeto Cliente a ser inserido.
    */
	void insert(Cliente obj);

	/**
    * Atualiza os dados de um cliente existente no banco.
    * @param obj O objeto com os dados atualizados.
    */
	void update(Cliente obj);

	/**
    * Remove um cliente do banco com base no ID.
    * @param id O ID do cliente que deve ser removido.
    */
	void deleteById(Integer id);

	/**
    * Busca um cliente pelo seu ID.
    * @param id O ID do cliente que deve ser buscado.
    * @return O objeto Cliente correspondente, ou null se não encontrado.
    */
	Cliente findById(Integer id);

	/**
    * Retorna todos os clientes cadastrados.
    * @return Lista de Cliente.
    */
	List<Cliente> findAll();
	
	void findAllTiposCliente();
}