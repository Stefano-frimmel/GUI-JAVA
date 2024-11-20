import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

class Pessoa {
    String nome;
    String telefone;

    Pessoa(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }
}

public class CrudTxtGUI extends JFrame {
    private ArrayList<Pessoa> listaPessoas = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable table;

    public CrudTxtGUI() {
        setTitle("Gerenciador de Pessoas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        carregarArquivo(listaPessoas);

        // Configuração da tabela
        tableModel = new DefaultTableModel(new Object[]{"Nome", "Telefone"}, 0);
        atualizarTabela();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Painel de botões
        JPanel panelButtons = new JPanel();
        JButton btnAdd = new JButton("Adicionar");
        JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Excluir");

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        // Adicionar componentes ao frame
        add(scrollPane, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);

        // Ações dos botões
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarPessoa();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarPessoa();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirPessoa();
            }
        });
    }

    private void adicionarPessoa() {
        JTextField fieldNome = new JTextField();
        JTextField fieldTelefone = new JTextField();
        Object[] message = {
            "Nome:", fieldNome,
            "Telefone:", fieldTelefone
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Adicionar Pessoa", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = fieldNome.getText();
            String telefone = fieldTelefone.getText();

            if (!nome.isEmpty() && !telefone.isEmpty()) {
                listaPessoas.add(new Pessoa(nome, telefone));
                salvarArquivo(listaPessoas);
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarPessoa() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pessoa pessoa = listaPessoas.get(selectedRow);
        JTextField fieldNome = new JTextField(pessoa.nome);
        JTextField fieldTelefone = new JTextField(pessoa.telefone);
        Object[] message = {
            "Nome:", fieldNome,
            "Telefone:", fieldTelefone
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Pessoa", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String novoNome = fieldNome.getText();
            String novoTelefone = fieldTelefone.getText();

            if (!novoNome.isEmpty() && !novoTelefone.isEmpty()) {
                listaPessoas.set(selectedRow, new Pessoa(novoNome, novoTelefone));
                salvarArquivo(listaPessoas);
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluirPessoa() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            listaPessoas.remove(selectedRow);
            salvarArquivo(listaPessoas);
            atualizarTabela();
        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        for (Pessoa p : listaPessoas) {
            tableModel.addRow(new Object[]{p.nome, p.telefone});
        }
    }

    private void salvarArquivo(ArrayList<Pessoa> listaPessoas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("pessoas.txt"))) {
            for (Pessoa p : listaPessoas) {
                writer.write("***Nome***\n" + p.nome + "\n***Telefone***\n" + p.telefone + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarArquivo(ArrayList<Pessoa> listaPessoas) {
        try (BufferedReader reader = new BufferedReader(new FileReader("pessoas.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("***Nome***")) {
                    String nome = reader.readLine();
                    reader.readLine(); // lê e ignora "***Telefone***"
                    String telefone = reader.readLine();
                    listaPessoas.add(new Pessoa(nome, telefone));
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Arquivo de dados não encontrado. Iniciando uma nova lista.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CrudTxtGUI frame = new CrudTxtGUI();
            frame.setVisible(true);
        });
    }
}
