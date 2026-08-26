package com.raven.form;


import com.raven.event.EventTimePicker;
import com.raven.swing.TimePicker;
import com.sun.jdi.connect.spi.Connection;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Date;
import javaapplication2.DatabaseConnection;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Check_IN_Check_Out extends javax.swing.JPanel {

    public Check_IN_Check_Out() {
        initComponents();
        refreshLogTable();

     
       
        tb_log.setRowHeight(30);


        timePicker_in.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Time selected: " + timePicker_in.getSelectedTime()); // Debugging
                String query = "INSERT INTO checkin_log (member_id, member_name,checkin_time, date) VALUES (?, ?,?, ?)";

                // Retrieve member ID
                String memberIdText = txt_mem_id.getText().trim();
                String membernameText = txt_mem_name.getText().trim();
                int memberId;

                // Validate and parse member ID
                if (memberIdText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Member ID cannot be empty!");
                    return;
                }
                try {
                    memberId = Integer.parseInt(memberIdText);
                    System.out.println("Member ID: " + memberId);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid Member ID! Please enter numbers only.");
                    return;
                }

                // Retrieve selected time from the time picker
                String timeText = timePicker_in.getSelectedTime().trim();
                

                if (timeText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a valid check-in time!");
                    return;
                }

                // Convert time string to LocalTime
                LocalTime checkInTime;
                try {
                    checkInTime = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("hh:mm a")); // Adjust format if necessary
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid time format! Use HH:mm AM/PM.");
                    return;
                }

                LocalDateTime currentDate = LocalDateTime.now(); // Store current date

                try (java.sql.Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setInt(1, memberId); // Set Member ID as an Integer
                    pstmt.setString(2, membernameText);
                    pstmt.setTime(3, java.sql.Time.valueOf(checkInTime)); // Set Check-in Time
                    pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(currentDate)); // Set Current Date

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "Data added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add data.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
                }
                refreshLogTable();
            }
        });

        timePicker_out.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Time selected: " + timePicker_out.getSelectedTime()); // Debugging

                // 1️⃣ Check if user selected a row from tb_log first
                int selectedRow = tb_log.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a record (row) to checkout!");
                    return;
                }

                // 2️⃣ Retrieve log_id directly from selected row (column 0 should be log_id)
                int logId = Integer.parseInt(tb_log.getValueAt(selectedRow, 0).toString());
                System.out.println("Selected Log ID: " + logId);

                // 3️⃣ Retrieve selected check-out time from time picker
                String timeText = timePicker_out.getSelectedTime();

                if (timeText == null || timeText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a valid check-out time!");
                    return;
                }

                // 4️⃣ Convert time string to LocalTime
                LocalTime checkOutTime;
                try {
                    checkOutTime = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("hh:mm a"));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid time format! Use HH:mm AM/PM.");
                    return;
                }

                // 5️⃣ Optional: Validate Check-Out time is after Check-In time (by loading check-in time from table or DB)
                LocalTime checkInTime = null;
                try {
                    String checkInTimeStr = tb_log.getValueAt(selectedRow, 3).toString();
                    checkInTime = LocalTime.parse(checkInTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error reading check-in time from table!");
                    return;
                }

                if (checkOutTime.isBefore(checkInTime)) {
                    JOptionPane.showMessageDialog(null, "Check-Out time must be after Check-In time!");
                    return;
                }

                // 6️ Prepare SQL UPDATE using log_id
                String query = "UPDATE checkin_log SET checkout_time = ? WHERE log_id = ?";

                try (java.sql.Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setTime(1, java.sql.Time.valueOf(checkOutTime));
                    pstmt.setInt(2, logId);

                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Check-Out updated successfully!");
                        refreshLogTable(); // Reload table after successful update
                    } else {
                        JOptionPane.showMessageDialog(null, "No matching record found (log_id=" + logId + ")");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
                }
            }
        });
    }

    private void refreshLogTable() {
        // Get the table model from tb_log
        DefaultTableModel model = (DefaultTableModel) tb_log.getModel();
        model.setRowCount(0); // Clear existing table data

        // SQL query to fetch log details with latest entries first
        String query = "SELECT log_id, member_id, member_name,checkin_time, checkout_time, date FROM checkin_log ORDER BY date DESC";

        try (java.sql.Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            // Loop through the result set and add each row to the JTable
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("log_id"),
                    rs.getInt("member_id"),
                    rs.getString("member_name"),
                    rs.getString("checkin_time"), // Fetch check-in time
                    rs.getString("checkout_time") != null ? rs.getString("checkout_time") : "N/A", // Handle null check-out time
                    rs.getString("date") // Fetch log date
                };
                model.addRow(row); // Add row to table
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading log data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_log = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btn_re = new javax.swing.JButton();
        panelBorder4 = new com.raven.swing.PanelBorder();
        txt_datetime = new javax.swing.JLabel();
        txt_time_in = new javax.swing.JTextField();
        txt_time_out = new javax.swing.JTextField();
        txt_mem_name = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_mem_id = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelBorder3 = new com.raven.swing.PanelBorder();
        timePicker_in = new com.raven.swing.TimePicker();
        panelBorder5 = new com.raven.swing.PanelBorder();
        timePicker_out = new com.raven.swing.TimePicker();

        setBackground(new java.awt.Color(237, 241, 245));

        tb_log.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tb_log.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Log_ID", "Member ID", "Member Name", "CheckIn Time", "CheckOut Time", "Date"
            }
        ));
        tb_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_logMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_log);

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel9.setText("Check OUT");

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 24)); // NOI18N
        jLabel10.setText("Check IN");

        btn_re.setBackground(new java.awt.Color(237, 241, 245));
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

        panelBorder4.setBackground(new java.awt.Color(255, 255, 255));

        txt_datetime.setText("DateTime");
        txt_datetime.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                txt_datetimeMouseMoved(evt);
            }
        });

        txt_time_in.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_time_in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_time_inActionPerformed(evt);
            }
        });

        txt_time_out.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_time_out.setToolTipText("");
        txt_time_out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_time_outActionPerformed(evt);
            }
        });

        txt_mem_name.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_mem_name.setText("-----");

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel1.setText("Time-IN :");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel2.setText("ID :");

        txt_mem_id.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        txt_mem_id.setText("-----");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel5.setText("Name :");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jLabel6.setText("Time-Out :");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("Member Check-IN Check-Out");

        javax.swing.GroupLayout panelBorder4Layout = new javax.swing.GroupLayout(panelBorder4);
        panelBorder4.setLayout(panelBorder4Layout);
        panelBorder4Layout.setHorizontalGroup(
            panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txt_mem_name, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_time_out, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_time_in, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))
                    .addGroup(panelBorder4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_mem_id)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_datetime, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelBorder4Layout.setVerticalGroup(
            panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder4Layout.createSequentialGroup()
                        .addComponent(txt_datetime, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)))
                .addGroup(panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_time_in, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(txt_mem_id))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_time_out, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(txt_mem_name, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        panelBorder3.setBackground(new java.awt.Color(255, 255, 255));

        timePicker_in.setDisplayText(txt_time_in);

        javax.swing.GroupLayout panelBorder3Layout = new javax.swing.GroupLayout(panelBorder3);
        panelBorder3.setLayout(panelBorder3Layout);
        panelBorder3Layout.setHorizontalGroup(
            panelBorder3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(timePicker_in, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        panelBorder3Layout.setVerticalGroup(
            panelBorder3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(timePicker_in, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelBorder5.setBackground(new java.awt.Color(255, 255, 255));

        timePicker_out.setForeground(new java.awt.Color(204, 0, 0));
        timePicker_out.setDisplayText(txt_time_out);

        javax.swing.GroupLayout panelBorder5Layout = new javax.swing.GroupLayout(panelBorder5);
        panelBorder5.setLayout(panelBorder5Layout);
        panelBorder5Layout.setHorizontalGroup(
            panelBorder5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(timePicker_out, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        panelBorder5Layout.setVerticalGroup(
            panelBorder5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(timePicker_out, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btn_re, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(panelBorder3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(87, 87, 87)
                                        .addComponent(jLabel10)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(67, 67, 67)
                                        .addComponent(panelBorder5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9)
                                        .addGap(56, 56, 56)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelBorder4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_re, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelBorder4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelBorder3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBorder5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tb_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_logMouseClicked
        int selectedRow = tb_log.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) tb_log.getModel();

        txt_mem_id.setText(model.getValueAt(selectedRow, 1).toString());
        txt_mem_name.setText(model.getValueAt(selectedRow, 2).toString());
        try {
            String checkInStr = model.getValueAt(selectedRow, 2).toString(); // Check-In
            String checkOutStr = model.getValueAt(selectedRow, 3).toString(); // Check-Out

            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");

            if (!checkInStr.isEmpty()) {
                Date checkInDate = format.parse(checkInStr);
                timePicker_in.setSelectedTime(checkInDate);
            } else {
                timePicker_in.setSelectedTime(null);
            }

            if (!checkOutStr.isEmpty()) {
                Date checkOutDate = format.parse(checkOutStr);
                timePicker_out.setSelectedTime(checkOutDate);
            } else {
                timePicker_out.setSelectedTime(null);
            }

            // Set Date
            java.util.Date logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(model.getValueAt(selectedRow, 4).toString());
            refreshLogTable();

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_tb_logMouseClicked

    private void txt_time_outActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_time_outActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_time_outActionPerformed

    private void txt_time_inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_time_inActionPerformed

    }//GEN-LAST:event_txt_time_inActionPerformed

    private void txt_datetimeMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_datetimeMouseMoved
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        txt_datetime.setText(now.format(formatter));
    }//GEN-LAST:event_txt_datetimeMouseMoved

    private void btn_reActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_reActionPerformed

    private void btn_reMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_reMouseClicked
        refreshLogTable();
        clearLogFields();
        LocalDateTime.now();
    }//GEN-LAST:event_btn_reMouseClicked

    private void clearLogFields() {
        txt_mem_id.setText("");
        txt_mem_name.setText("");
        timePicker_in.setSelectedTime(null);
        timePicker_out.setSelectedTime(null);
    }


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Check_IN_Check_Out().setVisible(true);
            }
        });

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_re;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private com.raven.swing.PanelBorder panelBorder3;
    private com.raven.swing.PanelBorder panelBorder4;
    private com.raven.swing.PanelBorder panelBorder5;
    private javax.swing.JTable tb_log;
    private com.raven.swing.TimePicker timePicker_in;
    private com.raven.swing.TimePicker timePicker_out;
    private javax.swing.JLabel txt_datetime;
    private javax.swing.JLabel txt_mem_id;
    private javax.swing.JLabel txt_mem_name;
    private javax.swing.JTextField txt_time_in;
    private javax.swing.JTextField txt_time_out;
    // End of variables declaration//GEN-END:variables


}
