package libraryProject;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookCopies extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> comboBoxBookCopiesId;
    private JComboBox<String> comboBoxBookCopiesBranchId;
    private JComboBox<String> comboBoxBookTitle;
    private JComboBox<String> comboBoxBranchName;
    private JLabel lblBookCopiesSearch;
    private JTable table;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BookCopies frame = new BookCopies();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public BookCopies() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblBookCopiesBookId = new JLabel("Kitap No:");
        lblBookCopiesBookId.setBounds(10, 10, 80, 25);
        contentPane.add(lblBookCopiesBookId);

        comboBoxBookCopiesId = new JComboBox<String>();
        comboBoxBookCopiesId.setBounds(10, 40, 200, 22);
        contentPane.add(comboBoxBookCopiesId);

        JLabel lblBookCopiesBranchId = new JLabel("Şube No:");
        lblBookCopiesBranchId.setBounds(10, 80, 80, 14);
        contentPane.add(lblBookCopiesBranchId);

        comboBoxBookCopiesBranchId = new JComboBox<String>();
        comboBoxBookCopiesBranchId.setBounds(10, 100, 200, 22);
        contentPane.add(comboBoxBookCopiesBranchId);

        lblBookCopiesSearch = new JLabel("");
        lblBookCopiesSearch.setBounds(10, 140, 200, 50);
        contentPane.add(lblBookCopiesSearch);

        JLabel lblBookTitle = new JLabel("Kitap Adı:");
        lblBookTitle.setBounds(300, 15, 80, 14);
        contentPane.add(lblBookTitle);

        comboBoxBookTitle = new JComboBox<String>();
        comboBoxBookTitle.setBounds(300, 40, 200, 22);
        contentPane.add(comboBoxBookTitle);

        JLabel lblBranchName = new JLabel("Kütüphane Adı:");
        lblBranchName.setBounds(300, 80, 120, 14);
        contentPane.add(lblBranchName);

        comboBoxBranchName = new JComboBox<String>();
        comboBoxBranchName.setBounds(300, 100, 200, 22);
        contentPane.add(comboBoxBranchName);

        lblBookCopiesSearch = new JLabel("");
        lblBookCopiesSearch.setBounds(10, 180, 300, 50);
        contentPane.add(lblBookCopiesSearch);

        JButton btnBookCopiesSearch = new JButton("Ara");
        btnBookCopiesSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBookCopies();
            }
        });
        btnBookCopiesSearch.setBounds(10, 150, 89, 23);
        contentPane.add(btnBookCopiesSearch);

        JButton btnBooksReset = new JButton("Sıfırla");
        btnBooksReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxBookCopiesId.setSelectedIndex(0);
                comboBoxBookCopiesBranchId.setSelectedIndex(0);
                lblBookCopiesSearch.setText("");
            }
        });
        btnBooksReset.setBounds(124, 150, 89, 23);
        contentPane.add(btnBooksReset);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 250, 560, 200);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        connect();
        loadComboBoxData();
        fillTable();

        comboBoxBookCopiesId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxBookCopiesId.getSelectedIndex();
                comboBoxBookTitle.setSelectedIndex(selectedIndex);
            }
        });

        comboBoxBookTitle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxBookTitle.getSelectedIndex();
                comboBoxBookCopiesId.setSelectedIndex(selectedIndex);
            }
        });

        comboBoxBookCopiesBranchId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxBookCopiesBranchId.getSelectedIndex();
                comboBoxBranchName.setSelectedIndex(selectedIndex);
            }
        });

        comboBoxBranchName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxBranchName.getSelectedIndex();
                comboBoxBookCopiesBranchId.setSelectedIndex(selectedIndex);
            }
        });
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject", "root", "12345");
            System.out.println("Veritabanına bağlandı.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadComboBoxData() {
        try {
            String query = "SELECT BookID FROM books ORDER BY BookID";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comboBoxBookCopiesId.addItem(resultSet.getString("BookID"));
            }

            query = "SELECT BranchID FROM library_branch";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comboBoxBookCopiesBranchId.addItem(resultSet.getString("BranchID"));
            }

            query = "SELECT Title FROM books ORDER BY BookID";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comboBoxBookTitle.addItem(resultSet.getString("Title"));
            }

            query = "SELECT BranchName FROM library_branch ORDER BY BranchID";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comboBoxBranchName.addItem(resultSet.getString("BranchName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchBookCopies() {
        try {
            String bookID = comboBoxBookCopiesId.getSelectedItem().toString();
            String branchID = comboBoxBookCopiesBranchId.getSelectedItem().toString();
            String query = "SELECT DISTINCT bc.No_Of_Copies AS NumberOfCopies " +
                    "FROM book_copies AS bc " +
                    "INNER JOIN books AS b ON bc.BookID = b.BookID " +
                    "INNER JOIN library_branch AS lb ON bc.BranchID = lb.BranchID " +
                    "WHERE b.BookID = ? AND lb.BranchID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bookID);
            preparedStatement.setString(2, branchID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int numberOfCopies = resultSet.getInt("NumberOfCopies");
                lblBookCopiesSearch.setText("Aradığınız kitaptan " + numberOfCopies + " adet bulunmaktadır.");
            } else {
                lblBookCopiesSearch.setText("Aradığınız kitap bu kütüphanede bulunmamaktadır.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillTable() {
        try {
            String query = "SELECT bc.BookID, b.Title, bc.BranchID, lb.BranchName, bc.No_Of_Copies " +
                    "FROM book_copies bc " +
                    "INNER JOIN books b ON bc.BookID = b.BookID " +
                    "INNER JOIN library_branch lb ON bc.BranchID = lb.BranchID";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            String[] columnNames = {"Kitap No", "Kitap Adı", "Şube No", "Şube Adı", "Kitap Adedi"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String bookID = resultSet.getString("BookID");
                String title = resultSet.getString("Title");
                String branchID = resultSet.getString("BranchID");
                String branchName = resultSet.getString("BranchName");
                int noOfCopies = resultSet.getInt("No_Of_Copies");
                Object[] row = {bookID, title, branchID, branchName, noOfCopies};
                model.addRow(row);
            }
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}










