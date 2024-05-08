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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Borrower extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtBorrowerName;
	private JTextField txtBorrowerAdress;
	private JTextField txtBorrowerPhone;
	private JButton btnBorrowerAdd;
	private JButton btnBorrowerReset;
	private Connection connection;
	private JButton btnBorrowerDelete;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Borrower frame = new Borrower();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Borrower() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblBorrowerName = new JLabel("Isim:");
		lblBorrowerName.setBounds(10, 10, 80, 25);
		contentPane.add(lblBorrowerName);

		txtBorrowerName = new JTextField();
		txtBorrowerName.setBounds(50, 10, 165, 25);
		contentPane.add(txtBorrowerName);
		txtBorrowerName.setColumns(10);

		JLabel lblBorrowerAdress = new JLabel("Adres:");
		lblBorrowerAdress.setBounds(240, 10, 80, 25);
		contentPane.add(lblBorrowerAdress);

		txtBorrowerAdress = new JTextField();
		txtBorrowerAdress.setBounds(290, 10, 165, 25);
		contentPane.add(txtBorrowerAdress);
		txtBorrowerAdress.setColumns(10);

		JLabel lblBorrowerPhone = new JLabel("Tel No:");
		lblBorrowerPhone.setBounds(490, 10, 80, 25);
		contentPane.add(lblBorrowerPhone);

		txtBorrowerPhone = new JTextField();
		txtBorrowerPhone.setBounds(540, 10, 165, 25);
		contentPane.add(txtBorrowerPhone);
		txtBorrowerPhone.setColumns(10);

		btnBorrowerAdd = new JButton("Ekle");
		btnBorrowerAdd.setBounds(10, 62, 99, 23);
		contentPane.add(btnBorrowerAdd);

		btnBorrowerReset = new JButton("Sıfırla");
		btnBorrowerReset.setBounds(234, 62, 99, 23);
		contentPane.add(btnBorrowerReset);

		btnBorrowerDelete = new JButton("Sil");
		btnBorrowerDelete.setBounds(123, 62, 99, 23);
		contentPane.add(btnBorrowerDelete);

		// Veritabanı bağlantısı kurma işlemi
		connect();

		// Ekle düğmesine tıklandığında veritabanına yeni borçlu bilgisi ekleme işlemi
		btnBorrowerAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String borrowerName = txtBorrowerName.getText();
				String borrowerAddress = txtBorrowerAdress.getText();
				String borrowerPhone = txtBorrowerPhone.getText();
				if (!isBorrowerExists(borrowerName, borrowerAddress, borrowerPhone)) {
					addBorrower(borrowerName, borrowerAddress, borrowerPhone);
					displayBorrowers();
				} else {
					System.out.println("Bu Kişi zaten var.");
				}
			}
		});

		// Sıfırla düğmesine tıklandığında text alanlarını temizle
		btnBorrowerReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtBorrowerName.setText("");
				txtBorrowerAdress.setText("");
				txtBorrowerPhone.setText("");
			}
		});

		// Sil düğmesine tıklandığında seçili satırı sil
		btnBorrowerDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String borrowerName = (String) table.getValueAt(selectedRow, 0);
					deleteBorrower(borrowerName);
					displayBorrowers();
				}
			}
		});

		// Create a table to display borrower information
		JPanel panelTable = new JPanel();
		panelTable.setBounds(10, 120, 700, 200);
		contentPane.add(panelTable);
		panelTable.setLayout(null);

		// Create a table to display author information
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 700, 200);
		panelTable.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		// Create a model for the table
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Üye Ismi");
		model.addColumn("Adres");
		model.addColumn("Tel No");
		table.setModel(model);

		// Display borrower information from the database
		displayBorrowers();
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

	// Yeni borçlu bilgisini veritabanına ekleme işlemi
	private void addBorrower(String name, String address, String phone) {
		try {
			String sql = "INSERT INTO borrower (BorrowerName, BorrowerAddress, BorrowerPhone) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, address);
			statement.setString(3, phone);
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Yeni Kişi eklendi.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Belirtilen isim, adres ve telefon numarasına sahip bir borçlu veritabanında zaten var mı kontrol etme
	private boolean isBorrowerExists(String name, String address, String phone) {
		try {
			String sql = "SELECT * FROM borrower WHERE BorrowerName = ? AND BorrowerAddress = ? AND BorrowerPhone = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, address);
			statement.setString(3, phone);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Borçlu bilgisini silme işlemi
	private void deleteBorrower(String name) {
		try {
			String sql = "DELETE FROM borrower WHERE BorrowerName = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Borçlu başarıyla silindi.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Borçluları tabloya ekleme işlemi
	private void displayBorrowers() {
		try {
			String sql = "SELECT * FROM borrower";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			// Create a vector to hold column names
			Vector<String> columns = new Vector<>();
			columns.add("Isim");
			columns.add("Adres");
			columns.add("Tel No");

			// Create a vector to hold data
			Vector<Vector<Object>> data = new Vector<>();

			// Add rows to the data vector
			while (resultSet.next()) {
				Vector<Object> row = new Vector<>();
				row.add(resultSet.getString("BorrowerName"));
				row.add(resultSet.getString("BorrowerAddress"));
				row.add(resultSet.getString("BorrowerPhone"));
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



