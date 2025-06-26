package view;

import controller.ServicoController;
import model.entities.Servico;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ServicoView extends JFrame {

    private static final long serialVersionUID = 1L;

    private ServicoController servicoController;
    private JTable tabela;
    private DefaultTableModel model;

    public ServicoView() {}

    public void setController(ServicoController controller) {
        this.servicoController = controller;
    }

    public void menuServico(ServicoController controller) {
        this.servicoController = controller;

        setTitle("Gerenciar Serviços");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---------- Painel de busca por ID ----------
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField campoId = new JTextField(10);
        JButton botaoBuscar = new JButton("Buscar por ID");
        JButton botaoLimpar = new JButton("Limpar");

        botaoBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(campoId.getText());
                Servico servico = servicoController.findServicoById(id);
                model.setRowCount(0); // limpa tabela

                if (servico != null) {
                    model.addRow(new Object[]{
                        servico.getId(),
                        servico.getNome(),
                        servico.getPreco(),
                        servico.getDescricao(),
                        "X"
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Serviço não encontrado.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        botaoLimpar.addActionListener(e -> {
            campoId.setText("");
            atualizarTabela(); // restaura todos os serviços
        });

        painelPesquisa.add(new JLabel("ID:"));
        painelPesquisa.add(campoId);
        painelPesquisa.add(botaoBuscar);
        painelPesquisa.add(botaoLimpar);

        add(painelPesquisa, BorderLayout.NORTH);

        // ---------- Tabela ----------
        String[] colunas = {"ID", "Nome", "Preço", "Descrição", "Excluir"};
        model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(30);
        tabela.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tabela.getColumn("Excluir").setCellRenderer(new ButtonRenderer());
        tabela.getColumn("Excluir").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // ---------- Painel de botões ----------
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.add(criarBotao("Cadastrar Serviço", e -> {
            controller.insertServico();
            atualizarTabela();
        }));
        painelBotoes.add(criarBotao("Atualizar Serviço", e -> {
            controller.updateServico();
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
        List<Servico> servicos = servicoController.findAllServicos();
        for (Servico s : servicos) {
            model.addRow(new Object[]{
                s.getId(),
                s.getNome(),
                s.getPreco(),
                s.getDescricao(),
                "X"
            });
        }
    }

    public Servico coletaDadosServico() {
        JTextField nomeField = new JTextField();
        JTextField precoField = new JTextField();
        JTextField descricaoField = new JTextField();

        int confirm = JOptionPane.showConfirmDialog(this, new Object[]{
            "Nome do serviço:", nomeField,
            "Preço:", precoField,
            "Descrição:", descricaoField
        }, "Cadastrar Serviço", JOptionPane.OK_CANCEL_OPTION);

        if (confirm != JOptionPane.OK_OPTION) return null;

        try {
            Servico servico = new Servico();
            servico.setNome(nomeField.getText());
            servico.setPreco(Double.parseDouble(precoField.getText()));
            servico.setDescricao(descricaoField.getText());
            return servico;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public Integer coletaIdServico() {
        try {
            String input = JOptionPane.showInputDialog(this, "Informe o ID do serviço:");
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
                servicoController.deleteServicoById(id);
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
