package com.raven.form;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javaapplication2.DatabaseConnection;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author RAVEN
 */
public class Setting extends javax.swing.JPanel {

    /**
     * Creates new form Form_1
     */
    public Setting() {
        initComponents();
        refreshGymSettingTable();
        updateCurrentCapacity();
    }

    public void updateCurrentCapacity() {
    
    String countQuery = """
        SELECT COUNT(*) AS active_count 
        FROM checkin_log 
        WHERE DATE(date) = CURDATE() 
        AND checkin_time IS NOT NULL 
        AND (checkout_time IS NULL OR checkout_time = '00:00:00')
    """;

    String updateQuery = "UPDATE gym_setting SET current_capacity = ? WHERE setting_id = 1";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement countStmt = conn.prepareStatement(countQuery);
         ResultSet rs = countStmt.executeQuery()) {

        int activeCount = 0;
        if (rs.next()) {
            activeCount = rs.getInt("active_count");
        }

        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, activeCount);
            updateStmt.executeUpdate();
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error updating current capacity: " + e.getMessage());
    }
}
    
       private void refreshGymSettingTable() { 
    // Get the table model from tb_gymsetting
    DefaultTableModel model = (DefaultTableModel) tb_gymsetting.getModel();
    model.setRowCount(0); // Clear existing table data

    // SQL query to fetch data from gym_setting table
    String query = "SELECT setting_id, max_capacity, current_capacity FROM gym_setting";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {
        // Loop through the result set and add each row to the JTable
        while (rs.next()) {
            Object[] row = {
                rs.getInt("setting_id"),
                rs.getInt("max_capacity"),
                rs.getObject("current_capacity") != null ? rs.getInt("current_capacity") : "N/A" // Handle null current_capacity
            };
            model.addRow(row); // Add row to table
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading gym setting data: " + e.getMessage());
    }
   }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_gymsetting = new javax.swing.JTable();
        btn_re = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel1.setText("Capacity for Gym Usage");

        tb_gymsetting.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        tb_gymsetting.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Setting_ID", "Max Capacity", "Current Capacity"
            }
        ));
        jScrollPane1.setViewportView(tb_gymsetting);

        btn_re.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        btn_re.setText("REFRESH");
        btn_re.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_reMouseClicked(evt);
            }
        });
        btn_re.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_re)
                        .addGap(334, 334, 334)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 448, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_re, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_reMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_reMouseClicked

    private void btn_reActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reActionPerformed
        refreshGymSettingTable();
        updateCurrentCapacity();
    }//GEN-LAST:event_btn_reActionPerformed

        public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Setting().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_re;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_gymsetting;
    // End of variables declaration//GEN-END:variables
}
