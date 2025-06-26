package controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import db.DB;
import model.dao.impl.ServicoDaoJDBC;
import model.entities.Servico;
import view.ServicoView;

public class ServicoController {
	
	private ServicoView servicoView;
	private ServicoDaoJDBC servicoDao;
	
	public ServicoController(Connection conn) {
		conn = DB.getConnection();
		this.servicoDao = new ServicoDaoJDBC(conn);
	}
	
	public ServicoController(Scanner sc, Connection conn, ServicoView servicoView) {
		this.servicoView = servicoView;
		this.servicoDao = new ServicoDaoJDBC(conn);
	}
	
	public void insertServico() {
		Servico servico = servicoView.coletaDadosServico();
		if (servico != null) servicoDao.insert(servico);
	}
	
	public void updateServico() {
		Integer id = servicoView.coletaIdServico();
		if (id != 0) {
		Servico servico = servicoView.coletaDadosServico();
			if (servico.getNome() != null) {
				servicoDao.update(id, servico);
			}
		}
	}
	
	public void deleteServicoById(Integer id) {
		if (id != 0) {
			servicoDao.deleteById(id);
		}
	}
	
	public Servico findServicoById(Integer id) {
	    if (id != 0) {
	        Servico servico = servicoDao.findById(id);
	        return servico;
	    }
	    return null;
	}
	
	public List<Servico> findAllServicos() {
		List<Servico> servicos = servicoDao.findAll();
		return servicos;
	}
}