package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.Map;

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

import controller.PacoteViagemController;
import model.entities.PacoteViagem;

public class PacoteViagemView extends JFrame {

    private static final long serialVersionUID = 1L;

    private PacoteViagemController pacoteViagemController;
    private DefaultTableModel model;
    private JTable tabelaPacote;

    public PacoteViagemView() {
        
    }

    public void setController(PacoteViagemController controller) {
        this.pacoteViagemController = controller;
    }

    public void menuPacoteViagem(PacoteViagemController controller) {
        this.pacoteViagemController = controller;

        setTitle("Gerenciar Pacotes de Viagem");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 450);
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
                PacoteViagem pacoteViagem = pacoteViagemController.findPacoteViagemById(id);
                model.setRowCount(0); // limpa a tabela para que o único conteúdo seja o cliente buscado

                if (pacoteViagem != null) { // Verificação se o cliente existe
                	
                    // Adiciona o cliente achado na tabela
                    model.addRow(new Object[]{
                        pacoteViagem.getId(), 
                        pacoteViagem.getNome(), 
                        pacoteViagem.getDescricao(), 
                        pacoteViagem.getPreco(), 
                        pacoteViagem.getDuracao(), 
                        pacoteViagem.getDestino(), 
                        pacoteViagem.getTipo(), 
                        "X"
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Pacote não encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
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
        String[] colunas = {"ID", "Nome", "Descrição", "Preço", "Duração", "Destino", "Tipo", "Excluir"};
        model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        tabelaPacote = new JTable(model);
        tabelaPacote.setRowHeight(30);
        tabelaPacote.setFillsViewportHeight(true);

        // Centralizar conteúdo das células
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabelaPacote.getColumnCount(); i++) {
            tabelaPacote.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Renderer e editor do botão "X"
        tabelaPacote.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
        tabelaPacote.getColumn("Excluir").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabelaPacote), BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.add(criarBotao("Cadastrar Pacote", e -> {
            controller.insertPacoteViagem();
            atualizarTabela();
        }));
        painelBotoes.add(criarBotao("Atualizar Pacote", e -> {
            controller.updatePacoteViagem();
            atualizarTabela();
        }));
        painelBotoes.add(criarBotao("Voltar", e -> dispose()));

        add(painelBotoes, BorderLayout.SOUTH);

        atualizarTabela();
        setVisible(true);
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener action) {
        JButton button = new JButton(texto);
        button.setPreferredSize(new Dimension(160, 35));
        button.addActionListener(action);
        return button;
    }

    public void atualizarTabela() {
        model.setRowCount(0);
        List<PacoteViagem> pacotes = pacoteViagemController.findAllPacoteViagens();
        for (PacoteViagem p : pacotes) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getPreco(),
                p.getDuracao(),
                p.getDestino(),
                p.getTipo(),
                "X"
            });
        }
    }
    
    public void mostrarDestinosPopup() {
        Map<Integer, String> destinos = pacoteViagemController.findAllDestinos();

        String[] colunas = {"ID", "Nome do Destino"};
        DefaultTableModel destinoModel = new DefaultTableModel(colunas, 0);

        for (Map.Entry<Integer, String> entry : destinos.entrySet()) {
            destinoModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        JTable tabela = new JTable(destinoModel);
        tabela.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Destinos disponíveis", JOptionPane.NO_OPTION);
    }

    public void mostrarTiposPopup() {
        Map<Integer, String> tipos = pacoteViagemController.findAllTipos();

        String[] colunas = {"ID", "Tipo de Pacote"};
        DefaultTableModel tipoModel = new DefaultTableModel(colunas, 0);

        for (Map.Entry<Integer, String> entry : tipos.entrySet()) {
            tipoModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        JTable tabela = new JTable(tipoModel);
        tabela.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JOptionPane.showMessageDialog(this, scrollPane, "Tipos disponíveis", JOptionPane.INFORMATION_MESSAGE);
    }

    public PacoteViagem coletaDadosPacoteViagem() {
        JTextField nomeField = new JTextField();
        JTextField descricaoField = new JTextField();
        JTextField precoField = new JTextField();
        JTextField duracaoField = new JTextField();
        
        mostrarDestinosPopup();
        pacoteViagemController.findAllDestinos();
        String idDestinoStr = JOptionPane.showInputDialog(this, "Informe o ID do destino:");

        mostrarTiposPopup();
        pacoteViagemController.findAllTipos();
        String idTipoStr = JOptionPane.showInputDialog(this, "Informe o ID do tipo:");

        int confirm = JOptionPane.showConfirmDialog(this, new Object[]{
            "Nome:", nomeField,
            "Descrição:", descricaoField,
            "Preço:", precoField,
            "Duração (dias):", duracaoField
        }, "Cadastrar Pacote", JOptionPane.OK_CANCEL_OPTION);

        if (confirm != JOptionPane.OK_OPTION) return null;

        try {
            PacoteViagem pacote = new PacoteViagem();
            pacote.setNome(nomeField.getText());
            pacote.setDescricao(descricaoField.getText());
            pacote.setPreco(Double.parseDouble(precoField.getText()));
            pacote.setDuracao(Integer.parseInt(duracaoField.getText()));
            pacote.setIdDestino(Integer.parseInt(idDestinoStr));
            pacote.setIdTipo(Integer.parseInt(idTipoStr));
            return pacote;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public Integer coletaIdPacoteViagem() {
        try {
            String input = JOptionPane.showInputDialog(this, "Informe o ID do pacote:");
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
                int id = (int) tabelaPacote.getValueAt(linha, 0);
                pacoteViagemController.deletePacoteViagemById(id);
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
}
