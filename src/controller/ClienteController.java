package controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import model.dao.impl.ClienteDaoJDBC;
import model.entities.Cliente;
import view.ClienteView;

public class ClienteController {

	private ClienteView clienteView;
    private ClienteDaoJDBC clienteDao;
    private static Connection conn;
	
    public ClienteController(Connection conn) {
    	this.clienteDao = new ClienteDaoJDBC(conn);
		this.conn = conn;
	}
    
	public ClienteController(Scanner sc, Connection conn, ClienteView clienteView) {
		this.conn = conn;
		this.clienteView = clienteView;
		this.clienteDao = new ClienteDaoJDBC(conn);
	}
	
	public void insertCliente() {
		Cliente cliente = clienteView.coletaDadosCliente();
		if (cliente != null) clienteDao.insert(cliente);
	}
	
	public void updateCliente() {
		Integer id = clienteView.coletaIdCliente();
		if (id != 0) {
		Cliente cliente = clienteView.coletaDadosCliente();
			if (cliente.getNome() != null) {
				clienteDao.update(cliente);
			}
		}
	}
	
	public void deleteClienteById(Integer id) {
		if (id != 0) {
			clienteDao.deleteById(id);
		}
	}
	
	public Cliente findClienteById(Integer id) {
		if (id != 0) {
			Cliente cliente = clienteDao.findById(id);
			return cliente;
		}
		return null;
	}
	
	public List<Cliente> findAllClientes() {
		List<Cliente> clientes = clienteDao.findAll();
		return clientes;
	}

	public Connection getConnection() {
		return conn;
	}
}