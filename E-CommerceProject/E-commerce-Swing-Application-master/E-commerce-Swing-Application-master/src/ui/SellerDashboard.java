package ui;

import dao.ProductDAO;
import model.Product;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellerDashboard extends JFrame {
    private JTextField nameField, colorField, categoryField, stockField, priceField, productWeightField;
    private JTextArea descriptionArea;
    private JButton addProductButton;
    
    public SellerDashboard() {
        setTitle("Satıcı Paneli");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Menü çubuğu ekleme
        JMenuBar menuBar = new JMenuBar();
        JMenu sellerMenu = new JMenu("Hesap");
        JMenuItem logoutItem = new JMenuItem("Çıkış Yap");
        
        logoutItem.addActionListener(e -> logout());
        sellerMenu.add(logoutItem);
        menuBar.add(sellerMenu);
        setJMenuBar(menuBar);
        
        // Ana panel - BorderLayout kullanıyoruz
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form alanları için panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        
        formPanel.add(new JLabel("Ürün Adı:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Renk:"));
        colorField = new JTextField();
        formPanel.add(colorField);
        
        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JTextField();
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Stok:"));
        stockField = new JTextField();
        formPanel.add(stockField);
        
        formPanel.add(new JLabel("Fiyat:"));
        priceField = new JTextField();
        formPanel.add(priceField);
        
        formPanel.add(new JLabel("Ağırlık:"));
        productWeightField = new JTextField();  // Düzeltme: Yeni bir JTextField eklendi
        formPanel.add(productWeightField);
        
        formPanel.add(new JLabel("Açıklama:"));
        descriptionArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Butonlar için panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addProductButton = new JButton("Ürün Ekle");
        buttonPanel.add(addProductButton);
        
        JButton logoutButton = new JButton("Çıkış Yap");
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    String color = colorField.getText();
                    String category = categoryField.getText();
                    int stock = Integer.parseInt(stockField.getText());
                    double price = Double.parseDouble(priceField.getText());
                    double weight = Double.parseDouble(productWeightField.getText());  // Ağırlık alanı eklendi
                    String description = descriptionArea.getText();
                    
                    Product product = new Product(name, color, category, stock, price, description, weight);
                    product.setProductWeight(weight);  // Ağırlık bilgisini ekledik
                    
                    ProductDAO productDAO = new ProductDAO();
                    if (productDAO.addProduct(product)) {
                        JOptionPane.showMessageDialog(SellerDashboard.this, "Ürün başarıyla eklendi!");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(SellerDashboard.this, "Ürün eklenirken bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SellerDashboard.this, "Geçersiz sayı formatı!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        add(mainPanel);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Çıkış yapmak istediğinize emin misiniz?",
            "Çıkış Onayı",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Mevcut pencereyi kapat
            new LoginFrame().setVisible(true); // Giriş ekranını aç
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        colorField.setText("");
        categoryField.setText("");
        stockField.setText("");
        priceField.setText("");
        productWeightField.setText("");
        descriptionArea.setText("");
    }
}