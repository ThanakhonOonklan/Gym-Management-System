package com.raven.form;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import javax.swing.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.util.Map;
import javaapplication2.DatabaseConnection;
import java.awt.Color;

public class Cashier extends javax.swing.JPanel {

    private final HashMap<String, Integer> productCount = new HashMap<>();
    private final HashMap<String, Double> productPrices = new HashMap<>();

    public Cashier() {
        initComponents();
        initProductPrices();
        refreshTable();
        clearFields();

        tb_product_list.getTableHeader().setVisible(false);
        tb_product_list.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 0));
        tb_product_list.setRowHeight(30);
        tb_product_list.setBackground(Color.WHITE);
        tb_product_list.setGridColor(Color.WHITE);

        tableMembers1.getTableHeader().setVisible(false);
        tableMembers1.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 0));
        tableMembers1.setBackground(Color.WHITE);
        tableMembers1.setGridColor(Color.WHITE);
        tableMembers1.setRowHeight(30);

        tb_product_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    int selectedRow = tb_product_list.rowAtPoint(evt.getPoint());
                    if (selectedRow != -1) {
                        tb_product_list.setRowSelectionInterval(selectedRow, selectedRow);
                        DefaultTableModel model = (DefaultTableModel) tb_product_list.getModel();
                        String productName = model.getValueAt(selectedRow, 1).toString();
                        int quantity = (int) model.getValueAt(selectedRow, 2);

                        int confirm = JOptionPane.showConfirmDialog(
                                null,
                                "Remove ' " + productName + " ' ?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            if (productCount.containsKey(productName)) {
                                if (quantity > 1) {
                                    productCount.put(productName, quantity - 1);
                                    double productPrice = productPrices.getOrDefault(productName, 0.0);
                                    model.setValueAt(quantity - 1, selectedRow, 2);
                                    model.setValueAt(productPrice * (quantity - 1), selectedRow, 3);
                                } else {
                                    productCount.remove(productName);
                                    model.removeRow(selectedRow);
                                }
                                updateTotalPrice();
                            }
                        }
                    }
                }
            }
        });

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"No.", "ProductName", "Qty", "Price"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tb_product_list.setModel(model);

        tableMembers1.setDefaultEditor(Object.class, null);

    }

    private void initProductPrices() {
        productPrices.put("High Protein Bread", 300.0);
        productPrices.put("Mineral Water 500ml", 40.0);
        productPrices.put("Protein Bar", 60.0);
        productPrices.put("BCAA Muscle Recovery", 800.0);
        productPrices.put("Portable Hand Sanitizer", 35.0);
        productPrices.put("Herbal Inhaler", 40.0);
        productPrices.put("Muscle Relaxing Cream", 35.0);
        productPrices.put("Gym Membership - Daily", 60.0);
        productPrices.put("Gym Membership - Monthly", 2700.0);
        productPrices.put("Gym Membership - Yearly", 30000.0);
        productPrices.put("Personal Training", 2500.0);
    }

    private void addProductToTable(String productName) {
        double productPrice = productPrices.getOrDefault(productName, 0.0);
        int quantity = productCount.getOrDefault(productName, 0) + 1;
        productCount.put(productName, quantity);
        double totalPrice = productPrice * quantity;

        DefaultTableModel model = (DefaultTableModel) tb_product_list.getModel();
        boolean productExists = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(productName)) {
                int Qty = Integer.parseInt(model.getValueAt(i, 2).toString());
                double Total = Double.parseDouble(model.getValueAt(i, 3).toString());

                model.setValueAt(Qty + 1, i, 2);
                model.setValueAt(Total + productPrice, i, 3);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            model.addRow(new Object[]{0, productName, quantity, totalPrice});
        }

        updateRowNumbers();

        updateTotalPrice();
    }

    private void updateRowNumbers() {
        DefaultTableModel model = (DefaultTableModel) tb_product_list.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }

    private void updateTotalPrice() {
        DefaultTableModel model = (DefaultTableModel) tb_product_list.getModel();
        double totalBeforeTax = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 3);

            if (value != null) {
                try {
                    double price = Double.parseDouble(value.toString());
                    totalBeforeTax += price;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format at row " + i);
                }
            }
        }

        double vat = totalBeforeTax * 0.07;
        double totalAfterTax = totalBeforeTax + vat;

        txt_vatt.setText(String.format("%.2f", vat));
        txt_totalAfterTaxx.setText(String.format("%.2f", totalAfterTax));
    }

    private String generatePDFReceipt(int paymentId, int memberId, String customerName, String paymentMethod, double amountReceived, double amountChange) {

        String directoryPath = System.getProperty("user.dir") + File.separator + "receipts";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + File.separator + "receipt_" + paymentId + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("INFINITY FITNESS RECEIPT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            document.add(new Paragraph("Receipt No: " + paymentId));
            document.add(new Paragraph("Customer ID: " + memberId));
            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("Date: " + new java.util.Date().toString()));
            document.add(new Paragraph("Payment Method: " + paymentMethod));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("No.");
            table.addCell("Product Name");
            table.addCell("Quantity");
            table.addCell("Total Price");

            DefaultTableModel model = (DefaultTableModel) tb_product_list.getModel();
            double totalBeforeTax = 0;

            for (int i = 0; i < model.getRowCount(); i++) {
                table.addCell(String.valueOf(i + 1));
                table.addCell(model.getValueAt(i, 1).toString());
                table.addCell(model.getValueAt(i, 2).toString());
                double totalPrice = Double.parseDouble(model.getValueAt(i, 3).toString());
                totalBeforeTax += totalPrice;
                table.addCell(String.format("%.2f", totalPrice));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            double vat = totalBeforeTax * 0.07;
            double totalAfterTax = totalBeforeTax + vat;

            document.add(new Paragraph("Total Before Tax: ฿" + String.format("%.2f", totalBeforeTax)));
            document.add(new Paragraph("VAT (7%): ฿" + String.format("%.2f", vat)));
            document.add(new Paragraph("Net Total: ฿" + String.format("%.2f", totalAfterTax)));
            document.add(new Paragraph("Amount Received: ฿" + String.format("%.2f", amountReceived)));
            document.add(new Paragraph("Change Given: ฿" + String.format("%.2f", amountChange)));

            document.close();

            JOptionPane.showMessageDialog(null, "Receipt generated successfully!\nSaved at: " + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating PDF receipt!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return filePath;
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tableMembers1.getModel();
        model.setRowCount(0);

        String query = "SELECT member_id, firstname, lastname, gender, phone, email, "
                + "join_date, expire_date, package_id, status "
                + "FROM member  ";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("member_id"),
                    rs.getString("firstname"),
                    rs.getString("join_date"),
                    rs.getString("expire_date"),
                    rs.getString("package_id"),
                    rs.getString("status")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txt_mem_id.setText("");
        txt_fname.setText("");
        txt_package_id.setText("");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txt_mem_id = new javax.swing.JTextField();
        btn_print_save = new javax.swing.JButton();
        btn_refresh = new javax.swing.JButton();
        txt_fname = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableMembers1 = new javax.swing.JTable();
        txt_package_id = new javax.swing.JTextField();
        panelBorder8 = new com.raven.swing.PanelBorder();
        btn_receive = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        rbtn_cash = new javax.swing.JRadioButton();
        rbtn_mobileBanking = new javax.swing.JRadioButton();
        rbtn_creditCard = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_product_list = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_receivedd = new javax.swing.JLabel();
        txt_changee = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_totalAfterTaxx = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_vatt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        panelBorder9 = new com.raven.swing.PanelBorder();
        btn_pro7 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        panelBorder11 = new com.raven.swing.PanelBorder();
        btn_pro1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        panelBorder12 = new com.raven.swing.PanelBorder();
        btn_pro4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        panelBorder13 = new com.raven.swing.PanelBorder();
        btn_pro3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        panelBorder14 = new com.raven.swing.PanelBorder();
        btn_pro9 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        panelBorder15 = new com.raven.swing.PanelBorder();
        btn_pro6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        panelBorder16 = new com.raven.swing.PanelBorder();
        btn_pro2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        panelBorder17 = new com.raven.swing.PanelBorder();
        btn_daily = new javax.swing.JButton();
        btn_month = new javax.swing.JButton();
        btn_year = new javax.swing.JButton();
        btn_training = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(237, 241, 245));

        txt_mem_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_mem_idActionPerformed(evt);
            }
        });

        btn_print_save.setBackground(new java.awt.Color(51, 81, 201));
        btn_print_save.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btn_print_save.setForeground(new java.awt.Color(255, 255, 255));
        btn_print_save.setText("Print & Save ");
        btn_print_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_print_saveActionPerformed(evt);
            }
        });

        btn_refresh.setBackground(new java.awt.Color(51, 81, 201));
        btn_refresh.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });

        tableMembers1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Firstname", "Join Date", "Expire Date", "Package ID", "Status"
            }
        ));
        tableMembers1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMembers1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableMembers1);

        panelBorder8.setBackground(new java.awt.Color(255, 255, 255));

        btn_receive.setBackground(new java.awt.Color(51, 81, 201));
        btn_receive.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        btn_receive.setForeground(new java.awt.Color(255, 255, 255));
        btn_receive.setText("Payment");
        btn_receive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_receiveActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 153, 153));
        jLabel11.setText("Received");

        jLabel13.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setText("Change");

        rbtn_cash.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        rbtn_cash.setText("Cash");

        rbtn_mobileBanking.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        rbtn_mobileBanking.setText("Bank");
        rbtn_mobileBanking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_mobileBankingActionPerformed(evt);
            }
        });

        rbtn_creditCard.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        rbtn_creditCard.setText("Card");
        rbtn_creditCard.setToolTipText("");
        rbtn_creditCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtn_creditCardActionPerformed(evt);
            }
        });

        tb_product_list.setBackground(new java.awt.Color(240, 249, 255));
        tb_product_list.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tb_product_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "ProductName", "Qty", "Price"
            }
        ));
        jScrollPane1.setViewportView(tb_product_list);

        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("---------------------------------------------------------------------------------------------");

        txt_receivedd.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txt_receivedd.setText("0.00");

        txt_changee.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txt_changee.setText("0.00");

        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("-----------------------------------------------------------------------------------------------");

        txt_totalAfterTaxx.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        txt_totalAfterTaxx.setForeground(new java.awt.Color(2, 132, 199));
        txt_totalAfterTaxx.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_totalAfterTaxx.setText("0.00");

        jLabel12.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel12.setText("Net");

        txt_vatt.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txt_vatt.setText("0.00");

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 153, 153));
        jLabel9.setText("Vat 7%");

        javax.swing.GroupLayout panelBorder8Layout = new javax.swing.GroupLayout(panelBorder8);
        panelBorder8.setLayout(panelBorder8Layout);
        panelBorder8Layout.setHorizontalGroup(
            panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBorder8Layout.createSequentialGroup()
                        .addComponent(rbtn_cash, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)
                        .addComponent(rbtn_mobileBanking)
                        .addGap(72, 72, 72)
                        .addComponent(rbtn_creditCard, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_receive, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelBorder8Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder8Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder8Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBorder8Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_vatt))
                            .addGroup(panelBorder8Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_totalAfterTaxx)))
                        .addGap(63, 63, 63))))
            .addGroup(panelBorder8Layout.createSequentialGroup()
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder8Layout.createSequentialGroup()
                        .addGap(0, 8, Short.MAX_VALUE)
                        .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder8Layout.createSequentialGroup()
                                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_receivedd)
                                    .addComponent(txt_changee))
                                .addGap(61, 61, 61))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelBorder8Layout.setVerticalGroup(
            panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_vatt)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_totalAfterTaxx)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_receivedd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_changee))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtn_creditCard)
                    .addComponent(rbtn_mobileBanking)
                    .addComponent(rbtn_cash))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_receive, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelBorder9.setBackground(new java.awt.Color(255, 255, 255));

        btn_pro7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/4-removebg-preview.png"))); // NOI18N
        btn_pro7.setToolTipText("Muscle Relaxing Cream");
        btn_pro7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pro7ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel3.setText("Relaxing Cream");

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(2, 132, 199));
        jLabel15.setText("B 35.0");

        javax.swing.GroupLayout panelBorder9Layout = new javax.swing.GroupLayout(panelBorder9);
        panelBorder9.setLayout(panelBorder9Layout);
        panelBorder9Layout.setHorizontalGroup(
            panelBorder9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder9Layout.createSequentialGroup()
                        .addComponent(btn_pro7, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelBorder9Layout.createSequentialGroup()
                        .addGroup(panelBorder9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelBorder9Layout.setVerticalGroup(
            panelBorder9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pro7, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        panelBorder11.setBackground(new java.awt.Color(255, 255, 255));

        btn_pro1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/3-removebg-preview.png"))); // NOI18N
        btn_pro1.setToolTipText("High Protein Bread");
        btn_pro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pro1ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel8.setText("High Protein Bread");

        jLabel19.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(2, 132, 199));
        jLabel19.setText("B 300.0");

        javax.swing.GroupLayout panelBorder11Layout = new javax.swing.GroupLayout(panelBorder11);
        panelBorder11.setLayout(panelBorder11Layout);
        panelBorder11Layout.setHorizontalGroup(
            panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pro1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addGroup(panelBorder11Layout.createSequentialGroup()
                        .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelBorder11Layout.setVerticalGroup(
            panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pro1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBorder12.setBackground(new java.awt.Color(255, 255, 255));

        btn_pro4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/1-removebg-preview.png"))); // NOI18N
        btn_pro4.setToolTipText("BCAA Muscle Recovery");
        btn_pro4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pro4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel2.setText("BCAA Muscle Recovery");

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(2, 132, 199));
        jLabel14.setText("B 800.0");

        javax.swing.GroupLayout panelBorder12Layout = new javax.swing.GroupLayout(panelBorder12);
        panelBorder12.setLayout(panelBorder12Layout);
        panelBorder12Layout.setHorizontalGroup(
            panelBorder12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder12Layout.createSequentialGroup()
                        .addComponent(btn_pro4, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelBorder12Layout.createSequentialGroup()
                        .addGroup(panelBorder12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelBorder12Layout.setVerticalGroup(
            panelBorder12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pro4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        panelBorder13.setBackground(new java.awt.Color(255, 255, 255));

        btn_pro3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/6-removebg-preview.png"))); // NOI18N
        btn_pro3.setToolTipText("Protein Bar");
        btn_pro3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pro3ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel5.setText("Protein Bar");

        jLabel16.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(2, 132, 199));
        jLabel16.setText("B 60.0");

        javax.swing.GroupLayout panelBorder13Layout = new javax.swing.GroupLayout(panelBorder13);
        panelBorder13.setLayout(panelBorder13Layout);
        panelBorder13Layout.setHorizontalGroup(
            panelBorder13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pro3, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addGroup(panelBorder13Layout.createSequentialGroup()
                        .addGroup(panelBorder13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelBorder13Layout.setVerticalGroup(
            panelBorder13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pro3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addGap(18, 18, 18))
        );

        panelBorder14.setBackground(new java.awt.Color(255, 255, 255));

        btn_pro9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/Herbal_Inhaler.jpg"))); // NOI18N
        btn_pro9.setToolTipText("Herbal Inhaler");
        btn_pro9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pro9ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel6.setText("Herbal Inhaler");

        jLabel17.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(2, 132, 199));
        jLabel17.setText("B 40.0");

        javax.swing.GroupLayout panelBorder14Layout = new javax.swing.GroupLayout(panelBorder14);
        panelBorder14.setLayout(panelBorder14Layout);
        panelBorder14Layout.setHorizontalGroup(
            panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder14Layout.createSequentialGroup()
                        .addComponent(btn_pro9, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelBorder14Layout.createSequentialGroup()
                        .addGroup(panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelBorder14Layout.setVerticalGroup(
            panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pro9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panelBorder15.setBackground(new java.awt.Color(255, 255, 255));

        btn_pro6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/5-removebg-preview.png"))); // NOI18N
        btn_pro6.setToolTipText("Portable Hand Sanitizer");
        btn_pro6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pro6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel7.setText("Portable Hand Sanitizer");

        jLabel18.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(2, 132, 199));
        jLabel18.setText("B 35.0");

        javax.swing.GroupLayout panelBorder15Layout = new javax.swing.GroupLayout(panelBorder15);
        panelBorder15.setLayout(panelBorder15Layout);
        panelBorder15Layout.setHorizontalGroup(
            panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pro6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelBorder15Layout.createSequentialGroup()
                        .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelBorder15Layout.setVerticalGroup(
            panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pro6, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panelBorder16.setBackground(new java.awt.Color(255, 255, 255));

        btn_pro2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/7-removebg-preview.png"))); // NOI18N
        btn_pro2.setToolTipText("Mineral Water 500ml");
        btn_pro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pro2ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel10.setText("Mineral Water 500ml");

        jLabel20.setFont(new java.awt.Font("Poppins", 0, 11)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(2, 132, 199));
        jLabel20.setText("B 40.0");

        javax.swing.GroupLayout panelBorder16Layout = new javax.swing.GroupLayout(panelBorder16);
        panelBorder16.setLayout(panelBorder16Layout);
        panelBorder16Layout.setHorizontalGroup(
            panelBorder16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pro2, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addGroup(panelBorder16Layout.createSequentialGroup()
                        .addGroup(panelBorder16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelBorder16Layout.setVerticalGroup(
            panelBorder16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_pro2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addGap(20, 20, 20))
        );

        panelBorder17.setBackground(new java.awt.Color(255, 255, 255));

        btn_daily.setBackground(new java.awt.Color(240, 249, 255));
        btn_daily.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btn_daily.setText("Daily");
        btn_daily.setToolTipText("Gym Membership - Daily");
        btn_daily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dailyActionPerformed(evt);
            }
        });

        btn_month.setBackground(new java.awt.Color(240, 249, 255));
        btn_month.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btn_month.setText("Monthly");
        btn_month.setToolTipText("Gym Membership - Monthly");
        btn_month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_monthActionPerformed(evt);
            }
        });

        btn_year.setBackground(new java.awt.Color(240, 249, 255));
        btn_year.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btn_year.setText("Yearly");
        btn_year.setToolTipText("Gym Membership - Yearly");
        btn_year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_yearActionPerformed(evt);
            }
        });

        btn_training.setBackground(new java.awt.Color(240, 249, 255));
        btn_training.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        btn_training.setText("Training 10");
        btn_training.setToolTipText("Personal Training");
        btn_training.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_trainingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder17Layout = new javax.swing.GroupLayout(panelBorder17);
        panelBorder17.setLayout(panelBorder17Layout);
        panelBorder17Layout.setHorizontalGroup(
            panelBorder17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_daily, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_month, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_year, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_training)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panelBorder17Layout.setVerticalGroup(
            panelBorder17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder17Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelBorder17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_daily, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_month, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_year, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_training, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(panelBorder12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(panelBorder9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(panelBorder15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelBorder11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelBorder13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelBorder16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(95, 95, 95)
                                        .addComponent(txt_fname, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(144, 144, 144)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txt_package_id, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                            .addComponent(txt_mem_id, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(panelBorder14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelBorder17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(btn_print_save)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_refresh)
                        .addGap(20, 20, 20)))
                .addComponent(panelBorder8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(panelBorder17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btn_print_save, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelBorder9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBorder12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBorder13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBorder14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(panelBorder11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(panelBorder15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelBorder16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_package_id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_mem_id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_fname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelBorder8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_dailyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dailyActionPerformed
        String productName = btn_daily.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_dailyActionPerformed

    private void btn_trainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_trainingActionPerformed
        String productName = btn_training.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_trainingActionPerformed

    private void btn_monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_monthActionPerformed
        String productName = btn_month.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_monthActionPerformed

    private void btn_pro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pro1ActionPerformed
        String productName = btn_pro1.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_pro1ActionPerformed

    private void btn_pro4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pro4ActionPerformed
        String productName = btn_pro4.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_pro4ActionPerformed

    private void btn_pro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pro2ActionPerformed
        String productName = btn_pro2.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_pro2ActionPerformed

    private void btn_pro3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pro3ActionPerformed
        String productName = btn_pro3.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_pro3ActionPerformed

    private void btn_pro7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pro7ActionPerformed
        String productName = btn_pro7.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_pro7ActionPerformed

    private void btn_yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_yearActionPerformed
        String productName = btn_year.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_yearActionPerformed

    private void rbtn_creditCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_creditCardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtn_creditCardActionPerformed

    private void tableMembers1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMembers1MouseClicked
        int selectedRow = tableMembers1.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tableMembers1.getModel();

        txt_mem_id.setText(model.getValueAt(selectedRow, 0).toString());
        txt_fname.setText(model.getValueAt(selectedRow, 1).toString());
        txt_package_id.setText(model.getValueAt(selectedRow, 4).toString());
    }//GEN-LAST:event_tableMembers1MouseClicked

    private void btn_receiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_receiveActionPerformed
        // Define available denominations
        int[] DENOMINATIONS = {1000, 500, 100, 50, 20, 10, 5, 2, 1};
        int MAX_NOTES_PER_DENOMINATION = 20;

        try {
            // Get total amount (Net.Price) from UI
            double netPrice = txt_totalAfterTaxx.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txt_totalAfterTaxx.getText().trim());

            // Get received amount from user input
            String input = JOptionPane.showInputDialog(this, "Enter received amount:", "Payment", JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No amount entered!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amountReceived = Double.parseDouble(input.trim());
            if (amountReceived < netPrice) {
                JOptionPane.showMessageDialog(this, "Insufficient amount received!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Correct Change Calculation
            double amountChange = amountReceived - netPrice;
            int remainingChange = (int) Math.round(amountChange);

            // Calculate optimal change breakdown
            StringBuilder breakdown = new StringBuilder("Change Breakdown:\n");
            Map<Integer, Integer> changeBreakdown = new HashMap<>();

            for (int denomination : DENOMINATIONS) {
                int count = Math.min(remainingChange / denomination, MAX_NOTES_PER_DENOMINATION);
                if (count > 0) {
                    changeBreakdown.put(denomination, count);
                    remainingChange -= denomination * count;
                    breakdown.append(denomination).append(" Baht: ").append(count).append(" pcs\n");
                }
            }

            JOptionPane.showMessageDialog(this, breakdown.toString(), "Change Calculation", JOptionPane.INFORMATION_MESSAGE);

            // Update UI fields with correct values
            txt_receivedd.setText(String.format("%.2f", amountReceived));
            txt_changee.setText(String.format("%.2f", amountChange));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_receiveActionPerformed

    private void btn_print_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_print_saveActionPerformed
        // Get selected payment method
        String paymentMethod = "";
        if (rbtn_cash.isSelected()) {
            paymentMethod = "Cash";
        } else if (rbtn_mobileBanking.isSelected()) {
            paymentMethod = "Mobile Banking";
        } else if (rbtn_creditCard.isSelected()) {
            paymentMethod = "Credit/Debit Card";
        } else {
            JOptionPane.showMessageDialog(this, "Please select a payment method!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Get total price details

            double vat = txt_vatt.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txt_vatt.getText().trim());
            double totalAfterTax = txt_totalAfterTaxx.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txt_totalAfterTaxx.getText().trim());

            // Get member ID and customer name
            String memberIdText = txt_mem_id.getText().trim();
            int memberId = memberIdText.isEmpty() ? 0 : Integer.parseInt(memberIdText);

            String packageIdText = txt_package_id.getText().trim();
            int packageId = packageIdText.isEmpty() ? 0 : Integer.parseInt(packageIdText);

            String customerName = txt_fname.getText().trim();
            double amountReceived = Double.parseDouble(txt_receivedd.getText().trim());
            double amountChange = Double.parseDouble(txt_changee.getText().trim());

            // Ensure valid input
            if (memberId == 0 || packageId == 0 || customerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // INSERT Query (Now includes amount_received & amount_change)
            String query = "INSERT INTO payment (member_id, package_id, payment_date, amount, amount_received, amount_change, payment_method, receipt_pdf_path) VALUES (?, ?, NOW(), ?, ?, ?, ?, ?)";

            try (java.sql.Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

                // Set values for the INSERT statement
                pstmt.setInt(1, memberId);
                pstmt.setInt(2, packageId);
                pstmt.setDouble(3, totalAfterTax);
                pstmt.setDouble(4, amountReceived);
                pstmt.setDouble(5, amountChange);
                pstmt.setString(6, paymentMethod);
                pstmt.setString(7, ""); // Temporary empty path

                // Execute the insert statement
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Payment insert failed, no rows affected.");
                }

                // Retrieve the latest payment_id using a SELECT query
                String selectQuery = "SELECT payment_id FROM payment WHERE member_id = ? AND package_id = ? ORDER BY payment_date DESC LIMIT 1";
                int paymentId = 0;

                try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                    selectStmt.setInt(1, memberId);
                    selectStmt.setInt(2, packageId);
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            paymentId = rs.getInt("payment_id");
                        } else {
                            throw new SQLException("Failed to retrieve payment_id.");
                        }
                    }
                }

                // Generate PDF receipt with the retrieved payment_id
                String pdfPath = generatePDFReceipt(paymentId, memberId, customerName, paymentMethod, amountReceived, amountChange);

                // Update the database with the PDF path
                String updateQuery = "UPDATE payment SET receipt_pdf_path = ? WHERE payment_id = ?";
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateQuery)) {
                    updatePstmt.setString(1, pdfPath);
                    updatePstmt.setInt(2, paymentId);
                    updatePstmt.executeUpdate();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing payment!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format! Please check input fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_print_saveActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
     
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void txt_mem_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_mem_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_mem_idActionPerformed

    private void rbtn_mobileBankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtn_mobileBankingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtn_mobileBankingActionPerformed

    private void btn_pro9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pro9ActionPerformed
        String productName = btn_pro9.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_pro9ActionPerformed

    private void btn_pro6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pro6ActionPerformed
        String productName = btn_pro6.getToolTipText();
        addProductToTable(productName);
    }//GEN-LAST:event_btn_pro6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_daily;
    private javax.swing.JButton btn_month;
    private javax.swing.JButton btn_print_save;
    private javax.swing.JButton btn_pro1;
    private javax.swing.JButton btn_pro2;
    private javax.swing.JButton btn_pro3;
    private javax.swing.JButton btn_pro4;
    private javax.swing.JButton btn_pro6;
    private javax.swing.JButton btn_pro7;
    private javax.swing.JButton btn_pro9;
    private javax.swing.JButton btn_receive;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_training;
    private javax.swing.JButton btn_year;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.raven.swing.PanelBorder panelBorder11;
    private com.raven.swing.PanelBorder panelBorder12;
    private com.raven.swing.PanelBorder panelBorder13;
    private com.raven.swing.PanelBorder panelBorder14;
    private com.raven.swing.PanelBorder panelBorder15;
    private com.raven.swing.PanelBorder panelBorder16;
    private com.raven.swing.PanelBorder panelBorder17;
    private com.raven.swing.PanelBorder panelBorder8;
    private com.raven.swing.PanelBorder panelBorder9;
    private javax.swing.JRadioButton rbtn_cash;
    private javax.swing.JRadioButton rbtn_creditCard;
    private javax.swing.JRadioButton rbtn_mobileBanking;
    private javax.swing.JTable tableMembers1;
    private javax.swing.JTable tb_product_list;
    private javax.swing.JLabel txt_changee;
    private javax.swing.JTextField txt_fname;
    private javax.swing.JTextField txt_mem_id;
    private javax.swing.JTextField txt_package_id;
    private javax.swing.JLabel txt_receivedd;
    private javax.swing.JLabel txt_totalAfterTaxx;
    private javax.swing.JLabel txt_vatt;
    // End of variables declaration//GEN-END:variables
}
