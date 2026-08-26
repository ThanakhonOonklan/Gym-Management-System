/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.form;

import java.util.Calendar;
import java.util.Locale;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.text.ParseException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaapplication2.DatabaseConnection;
import javax.swing.JOptionPane;

public class Member extends javax.swing.JPanel {

    private JDateChooser dateChooser;

    /**
     * Creates new form Form_1
     */
    public Member() {
        initComponents();
        refreshTable();
        
        txt_mem_id.setEditable(false); 

       
    }

    private void clearFields() {
        txt_mem_id.setText("");
        txt_fname.setText("");
        txt_lname.setText("");
        box_gender.setSelectedIndex(0);
        txt_phone.setText("");
        txt_email.setText("");
        date_join.setDate(null);
        date_exp.setDate(null);
        txt_package_id.setSelectedIndex(0);
        box_status.setSelectedIndex(0);
    }

    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Member().setVisible(true);

            }
        });
    }



    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) tableMembers.getModel();
        model.setRowCount(0); // Clear table

        String query = "SELECT m.member_id, m.firstname, m.lastname, m.gender, m.phone, m.email, "
                + "m.join_date, m.expire_date, p.package_name, m.status "
                + "FROM member m "
                + "LEFT JOIN package p ON m.package_id = p.package_id";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("member_id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("join_date"),
                    rs.getString("expire_date"),
                    rs.getString("package_name"),
                    rs.getString("status")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jToggleButton1 = new javax.swing.JToggleButton();
        panelBorder14 = new com.raven.swing.PanelBorder();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMembers = new javax.swing.JTable();
        txt_search = new javax.swing.JTextField();
        btn_search = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        panelBorder11 = new com.raven.swing.PanelBorder();
        txt_mem_id = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_fname = new javax.swing.JTextField();
        txt_lname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        box_gender = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txt_phone = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        panelBorder15 = new com.raven.swing.PanelBorder();
        txt_package_id = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        date_join = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        box_status = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        date_exp = new com.toedter.calendar.JDateChooser();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jToggleButton1.setText("jToggleButton1");

        setBackground(new java.awt.Color(237, 241, 245));

        panelBorder14.setBackground(new java.awt.Color(255, 255, 255));

        tableMembers.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tableMembers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Member ID", "Firstname", "Lastname ", "Gender ", "Phone ", "Email ", "Join Date", "Expire Date", "Package ID", "Status"
            }
        ));
        tableMembers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMembersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableMembers);

        btn_search.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btn_search.setText("Search");
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });

        btn_save.setBackground(new java.awt.Color(0, 153, 0));
        btn_save.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btn_save.setForeground(new java.awt.Color(255, 255, 255));
        btn_save.setText("SAVE");
        btn_save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_saveMouseClicked(evt);
            }
        });
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_delete.setBackground(new java.awt.Color(204, 0, 0));
        btn_delete.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_delete.setText("DELETE");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_update.setBackground(new java.awt.Color(51, 81, 201));
        btn_update.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btn_update.setForeground(new java.awt.Color(255, 255, 255));
        btn_update.setText("UPDATE");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBorder14Layout = new javax.swing.GroupLayout(panelBorder14);
        panelBorder14.setLayout(panelBorder14Layout);
        panelBorder14Layout.setHorizontalGroup(
            panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder14Layout.createSequentialGroup()
                .addGroup(panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(panelBorder14Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_search, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addContainerGap())
        );
        panelBorder14Layout.setVerticalGroup(
            panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelBorder11.setBackground(new java.awt.Color(255, 255, 255));

        txt_mem_id.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_mem_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_mem_idActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel3.setText("Member_ID");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel4.setText("Firstname");

        txt_fname.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        txt_lname.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel5.setText("\tLastname");

        box_gender.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        box_gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        box_gender.setToolTipText("");

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel7.setText("Gender");

        txt_phone.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_phoneActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel8.setText("Phone");

        txt_email.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel6.setText("Email");

        javax.swing.GroupLayout panelBorder11Layout = new javax.swing.GroupLayout(panelBorder11);
        panelBorder11.setLayout(panelBorder11Layout);
        panelBorder11Layout.setHorizontalGroup(
            panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder11Layout.createSequentialGroup()
                        .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_mem_id, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_lname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_fname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBorder11Layout.createSequentialGroup()
                        .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBorder11Layout.createSequentialGroup()
                                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txt_phone)
                            .addComponent(box_gender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(31, 31, 31))
        );
        panelBorder11Layout.setVerticalGroup(
            panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder11Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_mem_id, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_fname, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_lname, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(box_gender, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );

        panelBorder15.setBackground(new java.awt.Color(255, 255, 255));

        txt_package_id.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_package_id.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3" }));

        jLabel15.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel15.setText("Package_ID");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel10.setText("Expire Date");

        date_join.setDateFormatString("YYYY-MM-dd");

        jLabel14.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel14.setText("Status");

        box_status.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        box_status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Expired" }));

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel9.setText("Join Date");

        date_exp.setDateFormatString("YYYY-MM-dd");

        javax.swing.GroupLayout panelBorder15Layout = new javax.swing.GroupLayout(panelBorder15);
        panelBorder15.setLayout(panelBorder15Layout);
        panelBorder15Layout.setHorizontalGroup(
            panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder15Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelBorder15Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(date_join, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBorder15Layout.createSequentialGroup()
                        .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(box_status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_package_id, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder15Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(date_exp, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        panelBorder15Layout.setVerticalGroup(
            panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder15Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date_join, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_package_id, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(box_status, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(date_exp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelBorder11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(panelBorder15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 370, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBorder14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelBorder11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBorder15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(panelBorder14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_mem_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_mem_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_mem_idActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        String searchText = txt_search.getText().trim();

        // Check if input is empty
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a keyword to search!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // SQL query to search in multiple fields
        String query = "SELECT member_id, firstname, lastname, gender, phone, email, join_date, expire_date, package_id, status "
                + "FROM member "
                + "WHERE member_id LIKE ? OR firstname LIKE ? OR lastname LIKE ? OR phone LIKE ? OR email LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set wildcard search parameters
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, "%" + searchText + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            // Update table with search results
            DefaultTableModel model = (DefaultTableModel) tableMembers.getModel();
            model.setRowCount(0); // Clear existing table data

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("member_id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("gender"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("join_date"),
                    rs.getString("expire_date"),
                    rs.getString("package_id"),
                    rs.getString("status")
                };
                model.addRow(row);
            }

            // Check if no records found
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No matching results found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_searchActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed

        // Retrieve values from input fields
        String memberId = txt_mem_id.getText();
        String packageId = txt_package_id.getSelectedItem().toString();
        String firstName = txt_fname.getText();
        String lastName = txt_lname.getText();
        String gender = box_gender.getSelectedItem().toString();
        String phone = txt_phone.getText();
        String email = txt_email.getText();
        String status = box_status.getSelectedItem().toString();

        // Check if Member ID is provided
        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a member to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert dates from JDateChooser (handle null cases)
        java.sql.Date joinDate = null;
        java.sql.Date expireDate = null;

        if (date_join.getDate() != null) {
            joinDate = new java.sql.Date(date_join.getDate().getTime());
        }

        if (date_exp.getDate() != null) {
            expireDate = new java.sql.Date(date_exp.getDate().getTime());
        }

        // SQL Update Query
        String query = "UPDATE member SET firstname = ?, lastname = ?, gender = ?, phone = ?, email = ?, "
                + "join_date = ?, expire_date = ?, package_id = ?, status = ? WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, gender);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setDate(6, joinDate);  // Date format is handled properly
            pstmt.setDate(7, expireDate);
            pstmt.setString(8, packageId);
            pstmt.setString(9, status);
            pstmt.setInt(10, Integer.parseInt(memberId)); // WHERE condition

            // Execute update
            int updatedRows = pstmt.executeUpdate();
            refreshTable();
            clearFields();  // Clear input fields
            // Show result message
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update member data.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format in Package ID or Member ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        refreshTable();
        clearFields();
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_saveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_saveMouseClicked

    }//GEN-LAST:event_btn_saveMouseClicked

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        String memberId = txt_mem_id.getText();
        String packageId = txt_package_id.getSelectedItem().toString();
        String firstName = txt_fname.getText();
        String lastName = txt_lname.getText();
        String gender = box_gender.getSelectedItem().toString();
        String phone = txt_phone.getText();
        String email = txt_email.getText();
        String status = box_status.getSelectedItem().toString();

        // Convert date values from JDateChooser to MySQL format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String joinDate = sdf.format(date_join.getDate());
        String expireDate = sdf.format(date_exp.getDate());

        // SQL Insert Query
        String query = "INSERT INTO member ( firstname, lastname, gender, phone, email,  join_date, expire_date, package_id,status) "
                + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, gender);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setString(6, joinDate);
            pstmt.setString(7, expireDate);
            pstmt.setString(8, packageId);
            pstmt.setString(9, status);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Member added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add member.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        refreshTable();
        clearFields();  // Clear input fields
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // Get Member ID from the text field
        String memberId = txt_mem_id.getText();

        // Check if a member is selected
        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this member?", "Confirm Deletion",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM member WHERE member_id = ?";

            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, Integer.parseInt(memberId));

                int deletedRows = pstmt.executeUpdate();

                if (deletedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    clearFields();  // Clear input fields
                } else {
                    JOptionPane.showMessageDialog(this, "No member found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        refreshTable();
        clearFields();
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void tableMembersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMembersMouseClicked

        int selectedRow = tableMembers.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tableMembers.getModel();

        txt_mem_id.setText(model.getValueAt(selectedRow, 0).toString());
        txt_fname.setText(model.getValueAt(selectedRow, 1).toString());
        txt_lname.setText(model.getValueAt(selectedRow, 2).toString());
        box_gender.setSelectedItem(model.getValueAt(selectedRow, 3).toString());
        txt_phone.setText(model.getValueAt(selectedRow, 4).toString());
        txt_email.setText(model.getValueAt(selectedRow, 5).toString());

        try {

            java.util.Date joinDate = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(selectedRow, 6).toString());
            date_join.setDate(joinDate);

            java.util.Date expDate = new SimpleDateFormat("yyyy-MM-dd").parse(model.getValueAt(selectedRow, 7).toString());
            date_exp.setDate(expDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        txt_package_id.setSelectedItem(model.getValueAt(selectedRow, 8).toString());
        box_status.setSelectedItem(model.getValueAt(selectedRow, 9).toString());
    }//GEN-LAST:event_tableMembersMouseClicked

    private void txt_phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_phoneActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> box_gender;
    private javax.swing.JComboBox<String> box_status;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_update;
    private com.toedter.calendar.JDateChooser date_exp;
    private com.toedter.calendar.JDateChooser date_join;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private com.raven.swing.PanelBorder panelBorder11;
    private com.raven.swing.PanelBorder panelBorder14;
    private com.raven.swing.PanelBorder panelBorder15;
    private javax.swing.JTable tableMembers;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_fname;
    private javax.swing.JTextField txt_lname;
    private javax.swing.JTextField txt_mem_id;
    private javax.swing.JComboBox<String> txt_package_id;
    private javax.swing.JTextField txt_phone;
    private javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}
