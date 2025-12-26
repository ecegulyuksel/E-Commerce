package ui;

import dao.UserDAO;
import dao.SellerDAO;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton, sellerLoginButton;
    private JButton togglePasswordButton;
    private boolean isPasswordVisible = false;

    public LoginFrame() {
        setTitle("Welcome to E-Commerce");
        setSize(420, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🎨 Renkler ve fontlar
        Color backgroundColor = new Color(250, 250, 250);
        Color fieldColor = Color.WHITE;
        Color labelColor = new Color(60, 60, 60);
        Color buttonColor = new Color(33, 150, 243);
        Color buttonTextColor = Color.WHITE;
        Font font = new Font("Segoe UI", Font.PLAIN, 15);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);

        // 🧱 Ana panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(backgroundColor);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 🏷️ Başlık
        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel);

        // 👤 Kullanıcı adı
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(labelColor);
        userLabel.setFont(font);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // ORTALA
        mainPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(font);
        usernameField.setBackground(fieldColor);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 🔐 Şifre
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(labelColor);
        passLabel.setFont(font);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // ORTALA
        mainPanel.add(passLabel);


        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        passwordPanel.setBackground(backgroundColor);

        passwordField = new JPasswordField();
        passwordField.setFont(font);
        passwordField.setBackground(fieldColor);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        togglePasswordButton = new JButton("👁");
        togglePasswordButton.setPreferredSize(new Dimension(50, 36));
        togglePasswordButton.setFocusPainted(false);
        togglePasswordButton.setBackground(buttonColor);
        togglePasswordButton.setForeground(Color.WHITE);
        togglePasswordButton.setBorder(null);

        togglePasswordButton.addActionListener(e -> {
            isPasswordVisible = !isPasswordVisible;
            passwordField.setEchoChar(isPasswordVisible ? (char) 0 : '•');
            togglePasswordButton.setText(isPasswordVisible ? "🙈" : "👁");
        });

        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(togglePasswordButton, BorderLayout.EAST);
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // 🔘 Buton paneli
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Login");
        registerButton = new JButton("Sign Up");
        sellerLoginButton = new JButton("Seller Login");

        JButton[] buttons = {loginButton, registerButton, sellerLoginButton};
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(buttonTextColor);
            button.setFocusPainted(false);
            button.setFont(font);
            button.setPreferredSize(new Dimension(150, 35));
            buttonPanel.add(button);
        }

        mainPanel.add(buttonPanel);
        add(mainPanel);

        // 🎯 Olaylar
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            UserDAO userDAO = new UserDAO();
            if (userDAO.validateUser(username, password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Login successful");
                new UserDashboard(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        sellerLoginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            SellerDAO sellerDAO = new SellerDAO();
            if (sellerDAO.validateSeller(username, password)) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Seller login successful");
                new SellerDashboard().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid seller information!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> new RegistrationFrame().setVisible(true));
    }
}
