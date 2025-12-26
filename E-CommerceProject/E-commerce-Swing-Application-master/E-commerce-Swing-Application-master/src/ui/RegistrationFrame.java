package ui;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField, nameField, surnameField, emailField, homeAddressField, workAddressField;
    private JPasswordField passwordField;
    private JComboBox<Integer> dayCombo, monthCombo, yearCombo;
    private JButton registerButton;
    
    public RegistrationFrame() {
        setTitle("Kullanıcı Kayıt");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Kullanıcı Adı:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        
        panel.add(new JLabel("Şifre:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        
        panel.add(new JLabel("Ad:"));
        nameField = new JTextField();
        panel.add(nameField);
        
        panel.add(new JLabel("Soyad:"));
        surnameField = new JTextField();
        panel.add(surnameField);
        
        panel.add(new JLabel("Doğum Tarihi:"));
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Gün seçimi
        dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(i);
        }
        datePanel.add(dayCombo);
        
        // Ay seçimi
        monthCombo = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthCombo.addItem(i);
        }
        datePanel.add(monthCombo);
        
        // Yıl seçimi
        yearCombo = new JComboBox<>();
        for (int i = 1900; i <= 2023; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedItem(2000);
        datePanel.add(yearCombo);
        
        panel.add(datePanel);
        
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        
        panel.add(new JLabel("Ev Adresi:"));
        homeAddressField = new JTextField();
        panel.add(homeAddressField);
        
        panel.add(new JLabel("İş Adresi:"));
        workAddressField = new JTextField();
        panel.add(workAddressField);
        
        registerButton = new JButton("Kayıt Ol");
        panel.add(registerButton);
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String name = nameField.getText();
                String surname = surnameField.getText();
                int day = (int) dayCombo.getSelectedItem();
                int month = (int) monthCombo.getSelectedItem();
                int year = (int) yearCombo.getSelectedItem();
                String email = emailField.getText();
                String homeAddress = homeAddressField.getText();
                String workAddress = workAddressField.getText();
                
                @SuppressWarnings("deprecation")
                Date birthDate = new Date(year - 1900, month - 1, day);
                
                User user = new User(username, name, surname, birthDate, password, email, homeAddress, workAddress);
                
                UserDAO userDAO = new UserDAO();
                if (userDAO.addUser(user)) {
                    JOptionPane.showMessageDialog(RegistrationFrame.this, "Kayıt başarılı!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(RegistrationFrame.this, "Kayıt sırasında bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        add(panel);
    }
}