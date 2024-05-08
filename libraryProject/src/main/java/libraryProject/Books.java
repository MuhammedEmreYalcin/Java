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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;

public class Books extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtBooksTitle;
	private JComboBox<String> comboBoxPublisher;
	private JButton btnBooksAdd;
	private JButton btnBooksReset;
	private JButton btnBooksDelete;
	private Connection connection;
	private JTable table;
	private DefaultTableModel model;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Books frame = new Books();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Books() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblBooksTitle = new JLabel("Kitap Adı:");
		lblBooksTitle.setBounds(10, 10, 80, 25);
		contentPane.add(lblBooksTitle);

		txtBooksTitle = new JTextField();
		txtBooksTitle.setBounds(95, 10, 190, 25);
		contentPane.add(txtBooksTitle);
		txtBooksTitle.setColumns(10);

		JLabel lblPublisherName = new JLabel("Yayınevi:");
		lblPublisherName.setBounds(10, 50, 80, 25);
		contentPane.add(lblPublisherName);

		comboBoxPublisher = new JComboBox<String>();
		comboBoxPublisher.setBounds(95, 50, 190, 25);
		contentPane.add(comboBoxPublisher);

		btnBooksAdd = new JButton("Ekle");
		btnBooksAdd.setBounds(10, 100, 100, 25);
		contentPane.add(btnBooksAdd);

		btnBooksReset = new JButton("Sıfırla");
		btnBooksReset.setBounds(230, 100, 100, 25);
		contentPane.add(btnBooksReset);

		btnBooksDelete = new JButton("Sil");
		btnBooksDelete.setBounds(120, 100, 100, 25);
		contentPane.add(btnBooksDelete);

		// Connect to the database
		connect();

		// Load publisher names from database to the comboBoxPublisher
		loadPublishers();

		// Action listener for "Add" button
		btnBooksAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the book title and publisher selected by the user
				String bookTitle = txtBooksTitle.getText();
				String selectedPublisher = (String) comboBoxPublisher.getSelectedItem();

				// Insert book details into the database
				insertBook(bookTitle, selectedPublisher);
				// Refresh the table to display the updated book list
				displayBooks();
			}
		});

		// Action listener for "Reset" button
		btnBooksReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtBooksTitle.setText("");
				comboBoxPublisher.setSelectedIndex(0);
			}
		});

		// Action listener for "Delete" button
		btnBooksDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the selected row index
				int selectedRow = table.getSelectedRow();

				// Check if a row is selected
				if (selectedRow != -1) {
					// Get the values from the selected row
					String bookId = (String) table.getValueAt(selectedRow, 0);
					String bookTitle = (String) table.getValueAt(selectedRow, 1);
					String publisherName = (String) table.getValueAt(selectedRow, 2);

					// Delete the selected book from the database
					deleteBook(bookId, bookTitle, publisherName);

					// Refresh the table to reflect the changes
					displayBooks();
				} else {
					System.out.println("Lütfen silmek için bir kitap seçin.");
				}
			}
		});

		// Create a panel to hold the table
		JPanel panel = new JPanel();
		panel.setBounds(10, 140, 560, 220);
		contentPane.add(panel);
		panel.setLayout(null);

		// Create a table to display books
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 560, 220);
		panel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel();
		model.addColumn("Kitap ID");
		model.addColumn("Kitap Adı");
		model.addColumn("Yayınevi");
		table.setModel(model);

		// Display the initial book list
		displayBooks();
	}

	// Method to connect to the database
	private void connect() {
		try {
			// Load the JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection to the database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject", "root", "12345");
			System.out.println("Veritabanına Bağlandı.");
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Method to load publisher names from the database and populate comboBoxPublisher
	private void loadPublishers() {
		try {
			// SQL query to select publisher names from the database
			String sql = "SELECT PublisherName FROM publisher";

			// Create a PreparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Execute the query
			ResultSet resultSet = statement.executeQuery();

			// Populate comboBoxPublisher with publisher names
			while (resultSet.next()) {
				comboBoxPublisher.addItem(resultSet.getString("PublisherName"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Method to insert book details into the database
	private void insertBook(String title, String publisher) {
		try {
			// SQL query to insert book details into the database
			String sql = "INSERT INTO books (Title, PublisherName) VALUES (?, ?)";

			// Create a PreparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, title);
			statement.setString(2, publisher);

			// Execute the query
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Kitap başarıyla eklendi.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Method to delete a book from the database
	private void deleteBook(String bookId, String title, String publisher) {
		try {
			// SQL query to delete the book from the database
			String sql = "DELETE FROM books WHERE BookID = ? AND Title = ? AND PublisherName = ?";

			// Create a PreparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, bookId);
			statement.setString(2, title);
			statement.setString(3, publisher);

			// Execute the query
			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Kitap başarıyla silindi.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	// Method to display books from the database in the table
	private void displayBooks() {
		try {
			// Clear the table
			model.setRowCount(0);

			// SQL query to select books from the database
			String sql = "SELECT BookID, Title, PublisherName FROM books";

			// Create a PreparedStatement
			PreparedStatement statement = connection.prepareStatement(sql);

			// Execute the query
			ResultSet resultSet = statement.executeQuery();

			// Populate the table with books
			while (resultSet.next()) {
				String bookId = resultSet.getString("BookID");
				String title = resultSet.getString("Title");
				String publisher = resultSet.getString("PublisherName");
				model.addRow(new Object[]{bookId, title, publisher});
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}



