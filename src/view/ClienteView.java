package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.ClienteController;
import controller.PacoteViagemController;
import model.dao.impl.ClientePacoteServicoDaoJDBC;
import model.entities.Cliente;
import model.entities.PacoteViagem;

public class ClienteView extends JFrame {

    private static final long serialVersionUID = 1L;

    private ClienteController clienteController;
    private DefaultTableModel model;
    private JTable tabela;

    // Construtores
    public ClienteView() {}

    public ClienteView(ClienteController clienteController) {
        this.clienteController = clienteController;
        configurarJanela();
    }

    // Janela para gerenciar clientes
    private void configurarJanela() {
        setTitle("Gerenciar Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel de busca por ID
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField campoId = new JTextField(10);
        JButton botaoBuscar = new JButton("Buscar por ID");
        JButton botaoLimpar = new JButton("Limpar");

        // Adicionando botoes no painel de buscar ID
        painelPesquisa.add(new JLabel("ID:"));
        painelPesquisa.add(campoId);
        painelPesquisa.add(botaoBuscar);
        painelPesquisa.add(botaoLimpar);
        add(painelPesquisa, BorderLayout.NORTH);
        
        // Trigger de ação para o botão buscar o ID
        botaoBuscar.addActionListener(e -> {
        	// Pega o conteudo da caixa de texto
            String texto = campoId.getText().trim();
            // Se estiver vazio, aparece um aviso para que o usuario não deixe em branco
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um ID.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificação se o conteúdo da caixa de texto é inteiro
            try {
                int id = Integer.parseInt(texto);
                Cliente cliente = clienteController.findClienteById(id);
                model.setRowCount(0); // limpa a tabela para que o único conteúdo seja o cliente buscado

                if (cliente != null) { // Verificação se o cliente existe
                	// Aplicação de if ternário para saber se o cliente é brasileiro ou estrangeiro e então
                	// definir o tipo de documento
                	String tipo = (cliente.getIdTipo() != null && cliente.getIdTipo() == 1) ? "Brasileiro" : "Estrangeiro";
                    String documento = cliente.getCpf() != null ? cliente.getCpf() : cliente.getPassaporte();
                    
                    // Adiciona o cliente achado na tabela
                    model.addRow(new Object[]{
                        cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getEmail(), documento, tipo, "X"
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão "Limpar" para resetar a caixa de texto
        botaoLimpar.addActionListener(e -> {
            campoId.setText("");
            atualizarTabela();
        });

        
        // Tabela
        String[] colunas = {"ID", "Nome", "Telefone", "Email", "Documento", "Tipo", "Excluir"};
        model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        tabela = new JTable(model);
        tabela.setRowHeight(30);
        tabela.setFillsViewportHeight(true);

        // Centralizar conteúdo das células
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Renderer e editor do botão "X"
        tabela.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Excluir").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.add(criarBotao("Cadastrar Cliente", e -> {
            clienteController.insertCliente();
            atualizarTabela();
        }));
        painelBotoes.add(criarBotao("Atualizar Cliente", e -> {
            clienteController.updateCliente();
            atualizarTabela();
        }));
        painelBotoes.add(criarBotao("Voltar", e -> dispose()));

        add(painelBotoes, BorderLayout.SOUTH);

        atualizarTabela();
        setVisible(true);
        
        // Detecta clique na coluna "Nome" para mostrar os pacotes do cliente
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabela.rowAtPoint(evt.getPoint());
                int col = tabela.columnAtPoint(evt.getPoint());

                if (row >= 0 && col == 1) { // ← EVITA ACESSO COM ÍNDICE -1
                    Integer idCliente = (Integer) tabela.getValueAt(row, 0);
                    mostrarPacotesDoCliente(idCliente);
                }
            }
        });
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener action) {
        JButton button = new JButton(texto);
        button.setPreferredSize(new Dimension(160, 35));
        button.addActionListener(action);
        return button;
    }

    public void atualizarTabela() {
        model.setRowCount(0);
        List<Cliente> clientes = clienteController.findAllClientes();
        for (Cliente c : clientes) {
            String documento = c.getCpf() != null ? c.getCpf() : c.getPassaporte();
            String tipo = (c.getIdTipo() != null && c.getIdTipo() == 1) ? "Brasileiro" : "Estrangeiro";
            model.addRow(new Object[]{c.getId(), c.getNome(), c.getTelefone(), c.getEmail(), documento, tipo, "X"});
        }
    }
    
    private void mostrarPacotesDoCliente(Integer idCliente) {
        try {
            Connection conn = clienteController.getConnection();
            ClientePacoteServicoDaoJDBC relDao = new ClientePacoteServicoDaoJDBC(conn);

            List<PacoteViagem> pacotes = relDao.listarPacotesDoCliente(idCliente);

            if (pacotes == null || pacotes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum pacote encontrado para o cliente.", "Pacotes", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("Pacotes do cliente:\n");
            for (PacoteViagem p : pacotes) {
                sb.append("ID: ").append(p.getId()).append(" - ").append(p.getNome()).append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Pacotes do Cliente", JOptionPane.INFORMATION_MESSAGE);
            int opcao = JOptionPane.showConfirmDialog(this, "Deseja associar um novo pacote a este cliente?", "Associar", JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                mostrarPacotesDisponiveisParaAssociar(idCliente);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar pacotes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarPacotesDisponiveisParaAssociar(int idCliente) {
        try {
            Connection conn = clienteController.getConnection();
            ClientePacoteServicoDaoJDBC dao = new ClientePacoteServicoDaoJDBC(conn);

            // Buscar todos os pacotes
            List<PacoteViagem> pacotes = new PacoteViagemController(conn).findAllPacoteViagens();

            // Criar a janela com a tabela
            JFrame frame = new JFrame("Associar Pacote ao Cliente " + idCliente);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(this);
            frame.setLayout(new BorderLayout());

            String[] colunas = {"ID", "Nome", "Associar"};
            DefaultTableModel pacoteModel = new DefaultTableModel(colunas, 0) {
                public boolean isCellEditable(int row, int column) {
                    return column == 2;
                }
            };
            JTable tabelaPacotes = new JTable(pacoteModel);
            tabelaPacotes.setRowHeight(30);

            for (PacoteViagem p : pacotes) {
                pacoteModel.addRow(new Object[]{p.getId(), p.getNome(), "Associar"});
            }

            // Botão associar na célula
            tabelaPacotes.getColumn("Associar").setCellRenderer(new ButtonRenderer());
            tabelaPacotes.getColumn("Associar").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
                JButton button = new JButton("Associar");
                int selectedRow;

                {
                    button.setBackground(Color.GREEN.darker());
                    button.setForeground(Color.WHITE);
                    button.addActionListener(e -> {
                        int idPacote = (int) tabelaPacotes.getValueAt(selectedRow, 0);
                        dao.adicionarPacoteParaCliente(idCliente, idPacote);
                        JOptionPane.showMessageDialog(frame, "Pacote associado com sucesso!");
                        
                        // Fecha a janela
                        frame.dispose();

                        // Atualiza a lista de pacotes do cliente
                        mostrarPacotesDoCliente(idCliente);
                    });
                }

                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    selectedRow = row;
                    return button;
                }

                public Object getCellEditorValue() {
                    return "Associar";
                }
            });

            frame.add(new JScrollPane(tabelaPacotes), BorderLayout.CENTER);
            frame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }



    public Cliente coletaDadosCliente() {
        Cliente cliente = new Cliente();
        try {
            JTextField nomeField = new JTextField();
            JTextField telefoneField = new JTextField();
            JTextField emailField = new JTextField();
            String[] opcoes = {"Brasileiro", "Estrangeiro"};
            int escolha = JOptionPane.showOptionDialog(
                this,
                new Object[]{"Nome:", nomeField, "Telefone:", telefoneField, "Email:", emailField},
                "Cadastrar Cliente",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );
            if (escolha == JOptionPane.CLOSED_OPTION) return null;

            cliente.setNome(nomeField.getText());
            cliente.setTelefone(telefoneField.getText());
            cliente.setEmail(emailField.getText());

            if (escolha == 0) {
                String cpf = JOptionPane.showInputDialog(this, "Informe o CPF:");
                if (cpf == null) return null;
                cliente.setCpf(cpf);
                cliente.setIdTipo(1);
            } else {
                String passaporte = JOptionPane.showInputDialog(this, "Informe o Passaporte:");
                if (passaporte == null) return null;
                cliente.setPassaporte(passaporte);
                cliente.setIdTipo(2);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return cliente;
    }

    public Integer coletaIdCliente() {
        try {
            String input = JOptionPane.showInputDialog(this, "Informe o ID do cliente:");
            return Integer.parseInt(input);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }

    // ---------------------------
    // CLASSES INTERNAS DE BOTÃO
    // ---------------------------

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setText("X");
            setFont(getFont().deriveFont(Font.BOLD));
            setForeground(Color.WHITE);
            setBackground(Color.RED);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int linha;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("X");
            button.setFont(button.getFont().deriveFont(Font.BOLD));
            button.setForeground(Color.WHITE);
            button.setBackground(Color.RED);
            button.addActionListener(e -> {
                int id = (int) tabela.getValueAt(linha, 0);
                clienteController.deleteClienteById(id);
                atualizarTabela();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            linha = row;
            return button;
        }

        public Object getCellEditorValue() {
            return "X";
        }
    }

	public void setClienteController(ClienteController clienteController2) {
		
		
	}
}
