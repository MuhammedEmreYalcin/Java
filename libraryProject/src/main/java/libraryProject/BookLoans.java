package libraryProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookLoans extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboBoxBookID;
    private JComboBox<String> comboBoxBranchID;
    private JComboBox<String> comboBoxCardNo;
    private JTextField txtDateOut;
    private JTextField txtDueDate;
    private JButton btnAdd;
    private JButton btnReset;
    private JButton btnDelete;
    private JLabel lblSelectedValues;
    private JTable table;
    private Connection connection;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BookLoans frame = new BookLoans();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public BookLoans() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblBookID = new JLabel("Kitap No:");
        lblBookID.setBounds(10, 10, 80, 25);
        contentPane.add(lblBookID);

        comboBoxBookID = new JComboBox<>();
        comboBoxBookID.setBounds(100, 10, 150, 22);
        contentPane.add(comboBoxBookID);

        JLabel lblBranchID = new JLabel("Kütüphane No:");
        lblBranchID.setBounds(10, 50, 90, 25);
        contentPane.add(lblBranchID);

        comboBoxBranchID = new JComboBox<>();
        comboBoxBranchID.setBounds(100, 50, 150, 22);
        contentPane.add(comboBoxBranchID);

        JLabel lblCardNo = new JLabel("Üye Card No:");
        lblCardNo.setBounds(10, 90, 80, 25);
        contentPane.add(lblCardNo);

        comboBoxCardNo = new JComboBox<>();
        comboBoxCardNo.setBounds(100, 90, 150, 22);
        contentPane.add(comboBoxCardNo);

        JLabel lblDateOut = new JLabel("Alma Tarihi:");
        lblDateOut.setBounds(290, 10, 80, 25);
        contentPane.add(lblDateOut);

        txtDateOut = new JTextField();
        txtDateOut.setBounds(400, 10, 150, 25);
        contentPane.add(txtDateOut);
        txtDateOut.setColumns(10);

        JLabel lblDueDate = new JLabel("Geri Verme Tarihi:");
        lblDueDate.setBounds(290, 50, 120, 25);
        contentPane.add(lblDueDate);

        txtDueDate = new JTextField();
        txtDueDate.setBounds(400, 50, 150, 25);
        contentPane.add(txtDueDate);
        txtDueDate.setColumns(10);

        btnAdd = new JButton("Ekle");
        btnAdd.setBounds(10, 140, 100, 25);
        contentPane.add(btnAdd);

        btnReset = new JButton("Sıfırla");
        btnReset.setBounds(230, 140, 100, 25);
        contentPane.add(btnReset);

        btnDelete = new JButton("Sil");
        btnDelete.setBounds(120, 140, 100, 25);
        contentPane.add(btnDelete);

        lblSelectedValues = new JLabel();
        lblSelectedValues.setBounds(10, 250, 300, 25);
        contentPane.add(lblSelectedValues);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 190, 660, 150);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // Bağlantıyı Kur
        connect();

        // ComboBox'lara verileri yükle
        loadComboBoxData();

        // Tabloyu doldur
        fillTable();

        // Ekle düğmesine tıklama olayı
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookID = comboBoxBookID.getSelectedItem().toString();
                String branchID = comboBoxBranchID.getSelectedItem().toString();
                String cardNo = comboBoxCardNo.getSelectedItem().toString();
                String dateOut = txtDateOut.getText();
                String dueDate = txtDueDate.getText();

                if (!isLoanExists(bookID, branchID, cardNo)) {
                    addLoan(bookID, branchID, cardNo, dateOut, dueDate);
                    lblSelectedValues.setText("");
                    fillTable(); // Tabloyu yeniden doldur
                } else {
                    System.out.println("Bu ödünç alma kaydı zaten var.");
                }
            }
        });

        // Sıfırla düğmesine tıklama olayı
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxBookID.setSelectedIndex(0);
                comboBoxBranchID.setSelectedIndex(0);
                comboBoxCardNo.setSelectedIndex(0);
                txtDateOut.setText("");
                txtDueDate.setText("");
                lblSelectedValues.setText("");
                fillTable(); // Tabloyu yeniden doldur
            }
        });

        // Sil düğmesine tıklama olayı
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow(); // Seçilen satırın indeksini al
                if (selectedRow != -1) { // Eğer bir satır seçilmişse
                    // Seçilen satırın kitap numarasını al
                    String bookID = table.getValueAt(selectedRow, 0).toString();
                    // Veritabanından bu kitap numarasına sahip ödünç alma kaydını sil
                    deleteLoan(bookID);
                    fillTable(); // Tabloyu yeniden doldur
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen silmek istediğiniz bir ödünç alma kaydını seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Tabloyu dolduran metod
    private void fillTable() {
        try {
            String sql = "SELECT bl.BookID, b.Title, bl.BranchID, lb.BranchName, bl.CardNo, br.BorrowerName, bl.DateOut, bl.DueDate FROM book_loans bl JOIN books b ON bl.BookID = b.BookID JOIN library_branch lb ON bl.BranchID = lb.BranchID JOIN borrower br ON bl.CardNo = br.CardNo ORDER BY bl.BookID";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Tablonun sütun başlıklarını tanımla
            String[] columnNames = {"Kitap No", "Kitap Adı", "Kütüphane No", "Kütüphane Adı", "Üye Card No", "İsim", "Alma Tarihi", "Geri Verme Tarihi"};

            // Tablo verilerini tutacak bir liste oluştur
            List<Object[]> rows = new ArrayList<>();

            // Sonuç kümesindeki her bir satırı işle
            while (resultSet.next()) {
                // Satır verilerini diziye ekle
                Object[] row = new Object[8]; // 8 sütun olduğu için 8 elemanlı bir dizi
                for (int i = 0; i < 8; i++) {
                    row[i] = resultSet.getString(i + 1); // İndexler 1'den başladığı için i + 1 kullanılır
                }
                rows.add(row);
            }

            // Tablo modelini oluştur
            DefaultTableModel model = new DefaultTableModel(rows.toArray(new Object[0][0]), columnNames);

            // Modeli tabloya ekle
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Veritabanından ödünç alma kaydı silme işlemi
    private void deleteLoan(String bookID) {
        try {
            String sql = "DELETE FROM book_loans WHERE BookID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, bookID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Ödünç alma kaydı başarıyla silindi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Veritabanı bağlantısını kurma işlemi
    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject", "root", "12345");
            System.out.println("Veritabanına bağlandı.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // ComboBox'lara veri yükleme işlemi
    private void loadComboBoxData() {
        try {
            // Kitap No'ları yükle
            String sqlBook = "SELECT BookID FROM books ORDER BY BookID";
            PreparedStatement statementBook = connection.prepareStatement(sqlBook);
            ResultSet resultSetBook = statementBook.executeQuery();
            List<String> bookIDs = new ArrayList<>();
            while (resultSetBook.next()) {
                bookIDs.add(resultSetBook.getString("BookID"));
            }
            comboBoxBookID.setModel(new DefaultComboBoxModel<>(bookIDs.toArray(new String[0])));

            // Kütüphane No'ları yükle
            String sqlBranch = "SELECT BranchID FROM library_branch ORDER BY BranchID";
            PreparedStatement statementBranch = connection.prepareStatement(sqlBranch);
            ResultSet resultSetBranch = statementBranch.executeQuery();
            List<String> branchIDs = new ArrayList<>();
            while (resultSetBranch.next()) {
                branchIDs.add(resultSetBranch.getString("BranchID"));
            }
            comboBoxBranchID.setModel(new DefaultComboBoxModel<>(branchIDs.toArray(new String[0])));

            // Üye Card No'ları yükle
            String sqlCard = "SELECT CardNo FROM borrower ORDER BY CardNo";
            PreparedStatement statementCard = connection.prepareStatement(sqlCard);
            ResultSet resultSetCard = statementCard.executeQuery();
            List<String> cardNos = new ArrayList<>();
            while (resultSetCard.next()) {
                cardNos.add(resultSetCard.getString("CardNo"));
            }
            comboBoxCardNo.setModel(new DefaultComboBoxModel<>(cardNos.toArray(new String[0])));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ödünç alma kaydının veritabanında var olup olmadığını kontrol etme işlemi
    private boolean isLoanExists(String bookID, String branchID, String cardNo) {
        // Ödünç alma kaydının veritabanında var olup olmadığını kontrol et
        // Bu kısımda veritabanı sorgusu gerçekleştirilecek
        return false; // Örnek olarak her zaman false dönüyoruz
    }

    // Ödünç alma kaydı ekleme işlemi
    private void addLoan(String bookID, String branchID, String cardNo, String dateOut, String dueDate) {
        try {
            String sql = "INSERT INTO book_loans (BookID, BranchID, CardNo, DateOut, DueDate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, bookID);
            statement.setString(2, branchID);
            statement.setString(3, cardNo);
            statement.setString(4, dateOut);
            statement.setString(5, dueDate);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Ödünç alma kaydı başarıyla eklendi.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}





