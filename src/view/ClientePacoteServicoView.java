package view;

import java.awt.Dimension;
import java.sql.Connection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ClienteController;
import controller.ClientePacoteServicoController;
import controller.PacoteViagemController;
import controller.ServicoController;
import model.dao.impl.ClientePacoteServicoDaoJDBC;
import model.entities.Cliente;
import model.entities.PacoteViagem;

public class ClientePacoteServicoView extends JFrame {
	
	private Connection conn;
    private ClientePacoteServicoDaoJDBC clientePacoteServicoDao;
    private ClienteController clienteController;
    private PacoteViagemController pacoteViagemController;
    private ServicoController servicoController;

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public ClientePacoteServicoView() {
        
        // Estabelece conexão
        conn = new ClientePacoteServicoController().getConnection();

        if (conn == null) {
            JOptionPane.showMessageDialog(this, "❌ Conexão com o banco falhou!");
        } else {
            System.out.println("Conexão estabelecida: " + conn);
        }
    	
        // Inicializa os DAOs e Controllers
        clientePacoteServicoDao = new ClientePacoteServicoDaoJDBC(conn);
        clienteController = new ClienteController(conn);
        pacoteViagemController = new PacoteViagemController(conn);
        servicoController = new ServicoController(conn);

        // Configura janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
    }
    

	public void associarClienteComPacote() {
	    List<Cliente> clientes = clienteController.findAllClientes();
	    List<PacoteViagem> pacotes = pacoteViagemController.findAllPacoteViagens();

	    System.out.println("Clientes encontrados: " + clientes.size());
	    System.out.println("Pacotes encontrados: " + pacotes.size());

	    if (clientes.isEmpty() || pacotes.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Clientes ou pacotes indisponíveis.");
	        return;
	    }

	    // Monta lista de clientes
	    String[] opcoesClientes = clientes.stream()
	        .map(c -> "ID: " + c.getId() + " - " + c.getNome())
	        .toArray(String[]::new);

	    // Monta lista de pacotes
	    String[] opcoesPacotes = pacotes.stream()
	        .map(p -> "ID: " + p.getId() + " - " + p.getNome())
	        .toArray(String[]::new);

	    // Seleciona cliente
	    String clienteEscolhido = (String) JOptionPane.showInputDialog(
	        this,
	        "Selecione o cliente:",
	        "Selecionar Cliente",
	        JOptionPane.PLAIN_MESSAGE,
	        null,
	        opcoesClientes,
	        opcoesClientes[0]
	    );

	    if (clienteEscolhido == null) return; // cancelado

	    // Seleciona pacote
	    String pacoteEscolhido = (String) JOptionPane.showInputDialog(
	        this,
	        "Selecione o pacote:",
	        "Selecionar Pacote",
	        JOptionPane.PLAIN_MESSAGE,
	        null,
	        opcoesPacotes,
	        opcoesPacotes[0]
	    );

	    if (pacoteEscolhido == null) return; // cancelado

	    // Extrair IDs (parse dos primeiros números das strings)
	    int idCliente = Integer.parseInt(clienteEscolhido.split(" ")[1]);
	    int idPacote = Integer.parseInt(pacoteEscolhido.split(" ")[1]);

	    clientePacoteServicoDao.adicionarPacoteParaCliente(idCliente, idPacote);
	    JOptionPane.showMessageDialog(this, "Associação realizada com sucesso!");
	}
	
	public void mostrarClientesPopup() {
        List<Cliente> clientes = clienteController.findAllClientes();

        String[] colunas = {"ID", "Nome"};
        DefaultTableModel clienteModel = new DefaultTableModel(colunas, 0);

        for (Cliente c : clientes) {
        	clienteModel.addRow(new Object[]{
                    c.getId(),
                    c.getNome()
        	});
       	}
        JTable tabela = new JTable(clienteModel);
        tabela.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Destinos disponíveis", JOptionPane.NO_OPTION);
	}
    
	public void mostrarPacotesPopup() {
		List<PacoteViagem> pacotes = pacoteViagemController.findAllPacoteViagens();

        String[] colunas = {"ID", "Nome"};
        DefaultTableModel pacoteModel = new DefaultTableModel(colunas, 0);

        for (PacoteViagem p : pacotes) {
        	pacoteModel.addRow(new Object[]{
                    p.getId(),
                    p.getNome()
        	});
       	}

        JTable tabela = new JTable(pacoteModel);
        tabela.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Destinos disponíveis", JOptionPane.NO_OPTION);
	}
	

    public void associarServicoComPacoteCliente() {
        List<Cliente> clientes = clienteController.findAllClientes();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum cliente disponível.");
            return;
        }

        // Selecionar cliente
        String[] opcoesClientes = clientes.stream()
            .map(c -> "ID: " + c.getId() + " - " + c.getNome())
            .toArray(String[]::new);

        String clienteEscolhido = (String) JOptionPane.showInputDialog(
            this,
            "Selecione o cliente:",
            "Selecionar Cliente",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opcoesClientes,
            opcoesClientes[0]
        );
        if (clienteEscolhido == null) return;

        int idCliente = Integer.parseInt(clienteEscolhido.split(" ")[1]);

        // Buscar pacotes associados ao cliente
        List<PacoteViagem> pacotesCliente = clientePacoteServicoDao.listarPacotesDoCliente(idCliente);
        if (pacotesCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cliente não possui pacotes associados.");
            return;
        }

        // Selecionar pacote do cliente
        String[] opcoesPacotes = pacotesCliente.stream()
            .map(p -> "ID: " + p.getId() + " - " + p.getNome())
            .toArray(String[]::new);

        String pacoteEscolhido = (String) JOptionPane.showInputDialog(
            this,
            "Selecione o pacote do cliente:",
            "Selecionar Pacote",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opcoesPacotes,
            opcoesPacotes[0]
        );
        if (pacoteEscolhido == null) return;

        int idPacote = Integer.parseInt(pacoteEscolhido.split(" ")[1]);

        // Listar serviços disponíveis
        List<model.entities.Servico> servicos = servicoController.findAllServicos();
        if (servicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum serviço disponível.");
            return;
        }

        String[] opcoesServicos = servicos.stream()
            .map(s -> "ID: " + s.getId() + " - " + s.getNome())
            .toArray(String[]::new);

        // Selecionar serviço para adicionar
        String servicoEscolhido = (String) JOptionPane.showInputDialog(
            this,
            "Selecione o serviço para adicionar:",
            "Selecionar Serviço",
            JOptionPane.PLAIN_MESSAGE,
            null,
            opcoesServicos,
            opcoesServicos[0]
        );
        if (servicoEscolhido == null) return;

        int idServico = Integer.parseInt(servicoEscolhido.split(" ")[1]);

        clientePacoteServicoDao.adicionarServicoAoPacoteCliente(idCliente, idPacote, idServico);

        JOptionPane.showMessageDialog(this, "Serviço associado ao pacote do cliente com sucesso!");
    }
    
    public void mostrarServicosDoPacoteCliente() {
        // Selecionar Cliente
        List<Cliente> clientes = clienteController.findAllClientes();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado.");
            return;
        }

        String[] opcoesClientes = clientes.stream()
                .map(c -> "ID: " + c.getId() + " - " + c.getNome())
                .toArray(String[]::new);

        String clienteEscolhido = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o cliente:",
                "Selecionar Cliente",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoesClientes,
                opcoesClientes[0]
        );
        if (clienteEscolhido == null) return;

        int idCliente = Integer.parseInt(clienteEscolhido.split(" ")[1]);

        // Selecionar Pacote do Cliente
        List<PacoteViagem> pacotes = clientePacoteServicoDao.listarPacotesDoCliente(idCliente);
        if (pacotes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este cliente não possui pacotes.");
            return;
        }

        String[] opcoesPacotes = pacotes.stream()
                .map(p -> "ID: " + p.getId() + " - " + p.getNome())
                .toArray(String[]::new);

        String pacoteEscolhido = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o pacote do cliente:",
                "Selecionar Pacote",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoesPacotes,
                opcoesPacotes[0]
        );
        if (pacoteEscolhido == null) return;

        int idPacote = Integer.parseInt(pacoteEscolhido.split(" ")[1]);

        // Buscar serviços
        List<model.entities.Servico> servicos = clientePacoteServicoDao.listarServicosDoClienteNoPacote(idCliente, idPacote);

        if (servicos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum serviço associado a este pacote.");
            return;
        }

        // Mostrar em tabela
        String[] colunas = {"ID", "Nome do Serviço"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        for (model.entities.Servico s : servicos) {
            model.addRow(new Object[]{s.getId(), s.getNome()});
        }

        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Serviços no Pacote do Cliente", JOptionPane.INFORMATION_MESSAGE);
    }
}
