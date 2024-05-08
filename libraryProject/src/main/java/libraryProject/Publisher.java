package libraryProject;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

public class Publisher extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtPublisherName;
	private JTextField txtPublisherAddress;
	private JTextField txtPublisherPhone;
	private JButton btnPublisherAdd;
	private JButton btnPublisherReset;
	private JButton btnPublisherDelete;
	private Connection connection;
	private JTable table;
	private DefaultTableModel model;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Publisher frame = new Publisher();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Publisher() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPublisherName = new JLabel("Yayınevi Adı:");
		lblPublisherName.setBounds(10, 10, 100, 25);
		contentPane.add(lblPublisherName);

		txtPublisherName = new JTextField();
		txtPublisherName.setBounds(90, 10, 190, 25);
		contentPane.add(txtPublisherName);
		txtPublisherName.setColumns(10);

		JLabel lblPublisherAddress = new JLabel("Adres:");
		lblPublisherAddress.setBounds(325, 10, 100, 25);
		contentPane.add(lblPublisherAddress);

		txtPublisherAddress = new JTextField();
		txtPublisherAddress.setBounds(375, 10, 190, 25);
		contentPane.add(txtPublisherAddress);
		txtPublisherAddress.setColumns(10);

		JLabel lblPublisherPhone = new JLabel("Telefon:");
		lblPublisherPhone.setBounds(10, 50, 100, 25);
		contentPane.add(lblPublisherPhone);

		txtPublisherPhone = new JTextField();
		txtPublisherPhone.setBounds(90, 50, 190, 25);
		contentPane.add(txtPublisherPhone);
		txtPublisherPhone.setColumns(10);

		btnPublisherAdd = new JButton("Ekle");
		btnPublisherAdd.setBounds(10, 100, 100, 25);
		contentPane.add(btnPublisherAdd);

		btnPublisherReset = new JButton("Sıfırla");
		btnPublisherReset.setBounds(230, 100, 100, 25);
		contentPane.add(btnPublisherReset);

		btnPublisherDelete = new JButton("Sil");
		btnPublisherDelete.setBounds(120, 100, 100, 25);
		contentPane.add(btnPublisherDelete);

		// Connect to the database
		connect();

		// Action listener for the "Add" button
		btnPublisherAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String publisherName = txtPublisherName.getText();
				String publisherAddress = txtPublisherAddress.getText();
				String publisherPhone = txtPublisherPhone.getText();
				if (!isPublisherExists(publisherName, publisherAddress, publisherPhone)) {
					addPublisher(publisherName, publisherAddress, publisherPhone);
					displayPublisherInfo();
				} else {
					System.out.println("Bu yayınevi zaten var.");
				}
			}
		});

		// Action listener for the "Reset" button
		btnPublisherReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPublisherAddress.setText("");
				txtPublisherName.setText("");
				txtPublisherPhone.setText("");
			}
		});

		// Action listener for the "Delete" button
		btnPublisherDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String publisherName = (String) table.getValueAt(selectedRow, 0);
					String publisherAddress = (String) table.getValueAt(selectedRow, 1);
					String publisherPhone = (String) table.getValueAt(selectedRow, 2);
					deletePublisher(publisherName, publisherAddress, publisherPhone);
					displayPublisherInfo();
				} else {
					System.out.println("Lütfen silmek için bir yayınevi seçin.");
				}
			}
		});

		// Create a panel to hold the table
		JPanel panel = new JPanel();
		panel.setBounds(10, 140, 560, 220);
		contentPane.add(panel);
		panel.setLayout(null);

		// Create a table to display publisher information
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 560, 220);
		panel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel();
		model.addColumn("Yayınevi Adı");
		model.addColumn("Adres");
		model.addColumn("Telefon");
		table.setModel(model);

		// Display publisher information from the database
		displayPublisherInfo();
	}

	// Connect to the database
	private void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject", "root", "12345");
			System.out.println("Veritabanına Bağlandı.");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// Add new publisher information to the database
	private void addPublisher(String name, String address, String phone) {
		try {
			String sql = "INSERT INTO publisher (PublisherName, PublisherAddress, PublisherPhone) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, address);
			statement.setString(3, phone);
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Yeni yayınevi başarıyla eklendi.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Check if the publisher information already exists in the database
	private boolean isPublisherExists(String name, String address, String phone) {
		try {
			String sql = "SELECT * FROM publisher WHERE PublisherName = ? AND PublisherAddress = ? AND PublisherPhone = ?";
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

	// Delete publisher from database
	private void deletePublisher(String name, String address, String phone) {
		try {
			String sql = "DELETE FROM publisher WHERE PublisherName = ? AND PublisherAddress = ? AND PublisherPhone = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, address);
			statement.setString(3, phone);
			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Yayınevi başarıyla silindi.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Display publisher information obtained from the database
	private void displayPublisherInfo() {
		try {
			String sql = "SELECT * FROM publisher";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			// Clear the table
			model.setRowCount(0);

			while (resultSet.next()) {
				String name = resultSet.getString("PublisherName");
				String address = resultSet.getString("PublisherAddress");
				String phone = resultSet.getString("PublisherPhone");

				model.addRow(new Object[]{name, address, phone});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}







