package controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.dao.impl.PacoteViagemDaoJDBC;
import model.entities.PacoteViagem;
import view.PacoteViagemView;

public class PacoteViagemController {
	
	PacoteViagemController pacoteViagemController;

	private PacoteViagemView pacoteViagemView;
	private PacoteViagemDaoJDBC pacoteViagemDao;
	
	public PacoteViagemController(Connection conn) {
		this.pacoteViagemDao = new PacoteViagemDaoJDBC(conn);
	}
	
	public PacoteViagemController(Scanner sc, Connection conn, PacoteViagemView pacoteViagemView) {
		this.pacoteViagemView = pacoteViagemView;
		this.pacoteViagemDao = new PacoteViagemDaoJDBC(conn);
	}
	
	public void insertPacoteViagem() {
		PacoteViagem pacoteViagem = pacoteViagemView.coletaDadosPacoteViagem();
		if (pacoteViagem != null) pacoteViagemDao.insert(pacoteViagem);
	}
	
	public void updatePacoteViagem() {
		Integer id = pacoteViagemView.coletaIdPacoteViagem();
		if (id != 0) {
		PacoteViagem pacoteViagem = pacoteViagemView.coletaDadosPacoteViagem();
			if (pacoteViagem.getNome() != null) {
				pacoteViagemDao.update(id, pacoteViagem);
			}
		}
	}
	
	public void deletePacoteViagemById(Integer id) {
		if (id != 0) {
			pacoteViagemDao.deleteById(id);
		}
	}
	
	public PacoteViagem findPacoteViagemById(Integer id) {
		if (id != 0) {
			PacoteViagem pacoteViagem = pacoteViagemDao.findById(id);
			return pacoteViagem;
		}
		return null;
	}
	
	public List<PacoteViagem> findAllPacoteViagens() {
		List<PacoteViagem> pacotes = pacoteViagemDao.findAll();
		return pacotes;
	}
	
	public Map<Integer, String> findAllDestinos() {
		Map<Integer, String> destinos = pacoteViagemDao.findAllDestinos();
		return destinos;
	}
	
	public Map<Integer, String> findAllTipos() {
		Map<Integer, String> tipos = pacoteViagemDao.findAllTipos();
		return tipos;
	}
}