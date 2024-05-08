package libraryProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.Color;
import java.awt.Font;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GUIApp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Connection connection; // Deklaration der connection-Variablen

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUIApp frame = new GUIApp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Connect-Methode zum Herstellen einer Verbindung zur Datenbank
    public void Connect() {
        try {
            // Laden des JDBC-Treibers
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Herstellen der Verbindung zur Datenbank
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject", "root", "12345");
            System.out.println("Connected to the database.");

            // Hier können Sie Ihre Datenbankoperationen ausführen

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public GUIApp() {
        setTitle("Library Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 257, 571);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Aufruf der Connect-Methode im Konstruktor
        Connect();

        JButton btnPublisher = new JButton("Yayınevi ");
        btnPublisher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Publisher publisherPage = new Publisher();
                publisherPage.setVisible(true);
            }
        });
        btnPublisher.setBounds(49, 75, 139, 44);
        contentPane.add(btnPublisher);

        JButton btnBooks = new JButton("Kitaplar");
        btnBooks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Books booksPage = new Books();
                booksPage.setVisible(true);
            }
        });
        btnBooks.setBounds(49, 140, 139, 44);
        contentPane.add(btnBooks);

        JButton btnAuthors = new JButton("Yazar");
        btnAuthors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Authors authors = new Authors();
                authors.setVisible(true);
            }
        });
        btnAuthors.setBounds(49, 205, 139, 44);
        contentPane.add(btnAuthors);

        JButton btnLibraryBranch = new JButton("Kütüphane");
        btnLibraryBranch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LibraryBranch libraryBranch = new LibraryBranch();
                libraryBranch.setVisible(true);
            }
        });
        btnLibraryBranch.setBounds(49, 270, 139, 44);
        contentPane.add(btnLibraryBranch);

        JButton btnBorrower = new JButton("Üyelik");
        btnBorrower.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Borrower borrower = new Borrower();
                borrower.setVisible(true);
            }
        });
        btnBorrower.setBounds(49, 335, 139, 44);
        contentPane.add(btnBorrower);

        JButton btnBookLoans = new JButton("Kitap Ödünç Alma");
        btnBookLoans.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BookLoans bookLoans = new BookLoans();
                bookLoans.setVisible(true); // Düzeltme burada
            }
        });
        btnBookLoans.setBounds(49, 400, 139, 44);
        contentPane.add(btnBookLoans);

        JButton btnBookCopies = new JButton("Kitap Adeti");
        btnBookCopies.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BookCopies bookCopies = new BookCopies();
                bookCopies.setVisible(true);
            }
        });
        btnBookCopies.setBounds(49, 465, 139, 44);
        contentPane.add(btnBookCopies);

        JLabel lblLibraryManagmentSystem = new JLabel("Kütüphane Yönetim Sistemine");
        lblLibraryManagmentSystem.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblLibraryManagmentSystem.setBounds(10, 1, 221, 37);
        contentPane.add(lblLibraryManagmentSystem);

        JLabel lblLibraryManagmentSystem2 = new JLabel("Hoş Geldiniz");
        lblLibraryManagmentSystem2.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblLibraryManagmentSystem2.setBounds(72, 35, 88, 19);
        contentPane.add(lblLibraryManagmentSystem2);
    }
}



