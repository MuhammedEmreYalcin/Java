package libraryProject;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class LibraryBranch extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtLibraryBranchName;
    private JTextField txtLibraryBranchAdress;
    private Connection connection;
    private JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LibraryBranch frame = new LibraryBranch();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LibraryBranch() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 350);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblLibraryBranchName = new JLabel("Kütüphane Adı:");
        lblLibraryBranchName.setBounds(10, 10, 90, 25);
        contentPane.add(lblLibraryBranchName);

        txtLibraryBranchName = new JTextField();
        txtLibraryBranchName.setBounds(100, 10, 135, 25);
        contentPane.add(txtLibraryBranchName);
        txtLibraryBranchName.setColumns(10);

        JLabel lblLibraryBranchAdress = new JLabel("Adresi:");
        lblLibraryBranchAdress.setBounds(10, 50, 54, 14);
        contentPane.add(lblLibraryBranchAdress);

        txtLibraryBranchAdress = new JTextField();
        txtLibraryBranchAdress.setBounds(100, 50, 135, 25);
        contentPane.add(txtLibraryBranchAdress);
        txtLibraryBranchAdress.setColumns(10);

        JButton btnLibraryBranchAdd = new JButton("Ekle");
        btnLibraryBranchAdd.setBounds(10, 100, 89, 23);
        contentPane.add(btnLibraryBranchAdd);

        JButton btnLibraryBranchReset = new JButton("Sıfırla");
        btnLibraryBranchReset.setBounds(238, 100, 89, 23);
        contentPane.add(btnLibraryBranchReset);

        JButton btnLibraryBranchDelete = new JButton("Sil");
        btnLibraryBranchDelete.setBounds(124, 100, 89, 23);
        contentPane.add(btnLibraryBranchDelete);

        // Veritabanı bağlantısı kur
        connect();

        // Ekle düğmesine tıklandığında veritabanına yeni kütüphane bilgisi ekle
        btnLibraryBranchAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String branchName = txtLibraryBranchName.getText();
                String branchAddress = txtLibraryBranchAdress.getText();
                addLibraryBranch(branchName, branchAddress);
                displayLibraryBranches();
            }
        });

        // Sıfırla düğmesine tıklandığında text alanlarını temizle
        btnLibraryBranchReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtLibraryBranchAdress.setText("");
                txtLibraryBranchName.setText("");
            }
        });

        // Sil düğmesine tıklandığında seçili satırı sil
        btnLibraryBranchDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String branchName = (String) table.getValueAt(selectedRow, 0);
                    deleteLibraryBranch(branchName);
                    displayLibraryBranches();
                }
            }
        });

        // Create a table to display library branch information
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 140, 460, 210);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // Display library branch information from the database
        displayLibraryBranches();
    }

    // Veritabanı bağlantısı kurma işlemi
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject", "root", "12345");
            System.out.println("Veritabanına bağlandı.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Yeni kütüphane bilgisini veritabanına ekleme işlemi
    private void addLibraryBranch(String name, String address) {
        try {
            // Veritabanında aynı isimde ve adresde kütüphane olup olmadığını kontrol et
            String checkSql = "SELECT * FROM library_branch WHERE BranchName = ? AND BranchAddress = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, name);
            checkStatement.setString(2, address);
            if (checkStatement.executeQuery().next()) {
                System.out.println("Bu kütüphane zaten var.");
                return; // Eğer kütüphane zaten varsa işlemi sonlandır
            }

            // Kütüphane yoksa yeni kütüphane ekle
            String sql = "INSERT INTO library_branch (BranchName, BranchAddress) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, address);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Yeni kütüphane başarıyla eklendi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kütüphane bilgisini veritabanından silme işlemi
    private void deleteLibraryBranch(String name) {
        try {
            String sql = "DELETE FROM library_branch WHERE BranchName = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Kütüphane başarıyla silindi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Veritabanından kütüphane bilgilerini alıp tabloya gösterme
    private void displayLibraryBranches() {
        try {
            String sql = "SELECT * FROM library_branch";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Create a vector to hold column names
            Vector<String> columns = new Vector<>();
            columns.add("Kütüphane Adı");
            columns.add("Adresi");

            // Create a vector to hold data
            Vector<Vector<Object>> data = new Vector<>();

            // Add rows to the data vector
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getString("BranchName"));
                row.add(resultSet.getString("BranchAddress"));
                data.add(row);
            }

            // Create a table model and set the column names and data
            DefaultTableModel model = new DefaultTableModel(data, columns);

            // Set the table model to the table
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





