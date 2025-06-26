package controller;

import java.sql.Connection;
import java.util.Scanner;

import db.DB;
import model.dao.impl.ClientePacoteServicoDaoJDBC;
import view.ClienteView;
import view.PacoteViagemView;
import view.ServicoView;

public class ClientePacoteServicoController {
	
	private static Connection conn;
	
	private ClienteController clienteController;
	private PacoteViagemController pacoteViagemController;
	private ServicoController servicoController;
	
	private ClienteView clienteView;
	private PacoteViagemView pacoteViagemView;
	private ServicoView servicoView;
	
	private ClientePacoteServicoDaoJDBC cliPacSerDao;
	
	public ClientePacoteServicoController() {
	        conn = DB.getConnection();
	}
	 
	public ClientePacoteServicoController(Scanner sc, Connection conn, ClienteController clienteController, ClienteView clienteView, PacoteViagemController pacoteViagemController, PacoteViagemView pacoteViagemView, ServicoController servicoController, ServicoView servicoView) {
		this.clienteController = clienteController;
		this.clienteView = clienteView;
		this.pacoteViagemController = pacoteViagemController;
		this.pacoteViagemView = pacoteViagemView;
		this.servicoController = servicoController;
		this.servicoView = servicoView;
		this.cliPacSerDao = new ClientePacoteServicoDaoJDBC(conn);
	}
	
	public void addPacoteParaCliente(Integer idCliente) {
		if (idCliente != 0) {
			// fazer tabela
			pacoteViagemController.findAllPacoteViagens();
			Integer idPacoteViagem = pacoteViagemView.coletaIdPacoteViagem();
			if (idPacoteViagem != 0) cliPacSerDao.adicionarPacoteParaCliente(idCliente, idPacoteViagem);
			else System.out.println("Id inválido: pacote viagem.");
		} else {
			System.out.println("Id inválido: cliente.");
		}
	}
	
	public Integer listarPacotesDoCliente() {
		clienteController.findAllClientes();
		Integer idCliente = clienteView.coletaIdCliente();
		cliPacSerDao.listarPacotesDoCliente(idCliente);
		return idCliente;
	}
	
	public void addServicoAoPacoteDoCliente() {
		Integer idCliente = listarPacotesDoCliente();
		Integer idPacoteViagem = pacoteViagemView.coletaIdPacoteViagem();
		servicoController.findAllServicos();
		if(idCliente != 0 && idPacoteViagem != 0) {
			Integer idServico = servicoView.coletaIdServico();
			if (idServico != 0) cliPacSerDao.adicionarServicoAoPacoteCliente(idCliente, idPacoteViagem, idServico);
		}
	}
	
	public void listarServicosDoClienteNoPacote() {
		Integer idCliente = listarPacotesDoCliente();
		Integer idPacoteViagem = pacoteViagemView.coletaIdPacoteViagem();
		if (idCliente != 0 && idPacoteViagem != 0) cliPacSerDao.listarServicosDoClienteNoPacote(idCliente, idPacoteViagem);
	}
	
	public Connection getConnection() {
		return conn;
	}
}