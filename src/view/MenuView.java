package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Scanner;

import javax.swing.*;

import controller.ClienteController;
import controller.PacoteViagemController;
import controller.ServicoController;
import db.DB;

public class MenuView extends JFrame {

    private static final long serialVersionUID = 1L;

    private final ClienteController clienteController;
    private final PacoteViagemController pacoteViagemController;
    private final ServicoController servicoController;

    public MenuView() {
        Connection conn = DB.getConnection();

        // Criar as views antes dos controllers
        ClienteView clienteView = new ClienteView();
        PacoteViagemView pacoteViagemView = new PacoteViagemView();
        ServicoView servicoView = new ServicoView();
        ClientePacoteServicoView relView = new ClientePacoteServicoView();

        // Criar controllers
        this.clienteController = new ClienteController(new Scanner(System.in), conn, clienteView);
        this.pacoteViagemController = new PacoteViagemController(new Scanner(System.in), conn, pacoteViagemView);
        this.servicoController = new ServicoController(new Scanner(System.in), conn, servicoView);

        // Vincular controllers às views
        clienteView.setClienteController(clienteController);
        pacoteViagemView.setController(pacoteViagemController);
        servicoView.setController(servicoController);

        configurarJanela(clienteView, pacoteViagemView, servicoView, relView);
    }

    private void configurarJanela(ClienteView clienteView, PacoteViagemView pacoteViagemView, ServicoView servicoView, ClientePacoteServicoView relView) {
        setTitle("Agência de Viagens - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(0, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Botões
        painel.add(criarBotao("Gerenciar Clientes", e -> new ClienteView(clienteController)));
        painel.add(criarBotao("Gerenciar Pacotes de Viagem", e -> pacoteViagemView.menuPacoteViagem(pacoteViagemController)));
        painel.add(criarBotao("Gerenciar Serviços", e -> servicoView.menuServico(servicoController)));
        painel.add(criarBotao("Cliente compra pacote", e -> relView.associarClienteComPacote()));
        painel.add(criarBotao("Adicionar serviço a pacote", e -> relView.associarServicoComPacoteCliente()));
        painel.add(criarBotao("Listar serviços do pacote", e -> relView.mostrarServicosDoPacoteCliente()));
        painel.add(criarBotao("Sair", e -> sair()));

        add(painel);
        setVisible(true);
    }

    private JButton criarBotao(String texto, ActionListener action) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(230, 230, 250));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        button.addActionListener(action);
        return button;
    }

    private void sair() {
        DB.closeConnection();
        JOptionPane.showMessageDialog(this, "Programa encerrado.");
        dispose();
    }
}
