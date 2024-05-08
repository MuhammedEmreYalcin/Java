package libraryProject;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class Authors extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtAuthorName;
	private JComboBox<String> comboBoxBookIDs;
	private JComboBox<String> comboBoxBookTitles;
	private JButton btnAuthorAdd;
	private JButton btnAuthorReset;
	private JButton btnAuthorDelete;
	private Connection connection;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Authors frame = new Authors();
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
	public Authors() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblAuthorName = new JLabel("Yazar Ismi:");
		lblAuthorName.setBounds(10, 10, 80, 25);
		contentPane.add(lblAuthorName);

		txtAuthorName = new JTextField();
		txtAuthorName.setBounds(120, 10, 165, 25);
		contentPane.add(txtAuthorName);
		txtAuthorName.setColumns(10);

		JLabel lblBookID = new JLabel("Kitap Numarası:");
		lblBookID.setBounds(10, 40, 100, 25);
		contentPane.add(lblBookID);

		comboBoxBookIDs = new JComboBox<>();
		comboBoxBookIDs.setBounds(120, 40, 50, 25);
		contentPane.add(comboBoxBookIDs);

		JLabel lblBookTitle = new JLabel("Kitap Adı:");
		lblBookTitle.setBounds(10, 70, 80, 25);
		contentPane.add(lblBookTitle);

		comboBoxBookTitles = new JComboBox<>();
		comboBoxBookTitles.setBounds(120, 70, 165, 25);
		contentPane.add(comboBoxBookTitles);

		btnAuthorAdd = new JButton("Ekle");
		btnAuthorAdd.setBounds(10, 110, 99, 23);
		contentPane.add(btnAuthorAdd);

		btnAuthorReset = new JButton("Sıfırla");
		btnAuthorReset.setBounds(242, 110, 99, 23);
		contentPane.add(btnAuthorReset);

		btnAuthorDelete = new JButton("Sil");
		btnAuthorDelete.setBounds(126, 110, 99, 23);
		contentPane.add(btnAuthorDelete);

		// Bağlantıyı Kur
		connect();

		// Kitap Numaralarını ve Başlıklarını ComboBox'a Yükle
		loadBookIDsAndTitles();

		// Ekle Butonuna Tıklama Olayı
		btnAuthorAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String authorName = txtAuthorName.getText();
				String bookID = comboBoxBookIDs.getSelectedItem().toString();
				if (!isAuthorExists(authorName)) {
					addAuthor(authorName, bookID);
					displayAuthorInfo();
				} else {
					System.out.println("Bu yazar zaten var.");
				}
			}
		});

		// Sıfırla Butonuna Tıklama Olayı
		btnAuthorReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtAuthorName.setText("");
				comboBoxBookIDs.setSelectedIndex(0);
				comboBoxBookTitles.setSelectedIndex(0);
			}
		});

		// Sil Butonuna Tıklama Olayı
		btnAuthorDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String authorName = (String) table.getValueAt(selectedRow, 2);
					deleteAuthor(authorName);
					displayAuthorInfo();
				} else {
					System.out.println("Lütfen silmek için bir yazar seçin.");
				}
			}
		});

		// Kitap Numarası Seçildiğinde Kitap Adını Yükle
		comboBoxBookIDs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadBookTitleByBookID(comboBoxBookIDs.getSelectedItem().toString());
			}
		});

		// Kitap Adı Seçildiğinde Kitap Numarasını Yükle
		comboBoxBookTitles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadBookIDByTitle(comboBoxBookTitles.getSelectedItem().toString());
			}
		});

		// Create a panel to hold the table
		JPanel panelTable = new JPanel();
		panelTable.setBounds(10, 150, 565, 200);
		contentPane.add(panelTable);
		panelTable.setLayout(null);

		// Create a table to display author information
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 565, 200);
		panelTable.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		// Create a model for the table
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Yazar ID");
		model.addColumn("Kitap ID");
		model.addColumn("Yazar İsmi");
		table.setModel(model);

		// Display author information from the database
		displayAuthorInfo();
	}

	// Veritabanına Bağlanma İşlemi
	private void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject", "root", "12345");
			System.out.println("Veritabanına Bağlandı.");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	// Yeni Yazar Bilgisi Veritabanına Ekleme İşlemi
	private void addAuthor(String name, String bookID) {
		try {
			String sql = "INSERT INTO book_authors (AuthorName, BookID) VALUES (?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setString(2, bookID);
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Yeni yazar başarıyla eklendi.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Yazarı Veritabanından Silme İşlemi
	private void deleteAuthor(String name) {
		try {
			String sql = "DELETE FROM book_authors WHERE AuthorName = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Yazar başarıyla silindi.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Belirtilen isimde bir yazar veritabanında zaten var mı kontrol etme
	private boolean isAuthorExists(String name) {
		try {
			String sql = "SELECT * FROM book_authors WHERE AuthorName = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Kitap Numaralarını ve Başlıklarını ComboBox'a Yükleme İşlemi
	private void loadBookIDsAndTitles() {
		List<String> ids = new ArrayList<>();
		List<String> titles = new ArrayList<>();
		try {
			String sql = "SELECT BookID, Title FROM books";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				ids.add(resultSet.getString("BookID"));
				titles.add(resultSet.getString("Title"));
			}
			comboBoxBookIDs.setModel(new DefaultComboBoxModel<>(ids.toArray(new String[0])));
			comboBoxBookTitles.setModel(new DefaultComboBoxModel<>(titles.toArray(new String[0])));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Kitap Numarasına Göre Kitap Adını Getirme İşlemi
	private void loadBookTitleByBookID(String bookID) {
		try {
			String sql = "SELECT Title FROM books WHERE BookID = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, bookID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				comboBoxBookTitles.setSelectedItem(resultSet.getString("Title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Kitap Adına Göre Kitap Numarasını Getirme İşlemi
	private void loadBookIDByTitle(String bookTitle) {
		try {
			String sql = "SELECT BookID FROM books WHERE Title = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, bookTitle);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				comboBoxBookIDs.setSelectedItem(resultSet.getString("BookID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Veritabanından yazar bilgilerini alıp tabloya gösterme
	private void displayAuthorInfo() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Tabloyu temizle
		try {
			String sql = "SELECT * FROM book_authors";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String authorID = resultSet.getString("AuthorID");
				String bookID = resultSet.getString("BookID");
				String authorName = resultSet.getString("AuthorName");
				model.addRow(new Object[]{authorID, bookID, authorName});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}









