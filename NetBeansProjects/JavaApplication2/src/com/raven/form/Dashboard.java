package com.raven.form;

import com.raven.model.Model_Card;
import com.raven.model.StatusType;
import com.raven.swing.ScrollBar;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javaapplication2.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Dashboard extends javax.swing.JPanel {

    public Dashboard() {
        initComponents();
        viewSalesChanged();
        loadMemberList();
        loadNewMembers();
        updateTotalAmount();
        updateTotalMembers();
        updateActiveMembers();
        updateCheckinCount();
        startTimeUpdate();
        startAutoUpdate();

        tb_member_list.setShowGrid(false);
        tb_member_list.setRowHeight(30);
        tb_member_list.setBackground(Color.WHITE);
        tb_member_list.setShowGrid(false);
        tb_member_list.setIntercellSpacing(new java.awt.Dimension(0, 10));

        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Firstname", "Join_date", "Package_ID"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tb_new_member.setRowHeight(30);
        tb_new_member.setBackground(Color.WHITE);

    }
    

    private void startTimeUpdate() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCurrentTime();
            }
        }, 0, 1000);
    }

    private void updateCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(new Date());
        txt_time.setText(currentTime);
    }

    private void viewSalesChanged() {
        String selected = (String) View_Sales.getSelectedItem();

        if (selected.equals("Daily Sales")) {
            View_txt.setText("Daily Sales");
            double dailySales = getDailySales();
            View_money.setText("B " + String.format("%.2f", dailySales));
        } else if (selected.equals("Monthly Sales")) {
            View_txt.setText("Monthly Sales");
            double monthlySales = getMonthlySales();
            View_money.setText("B " + String.format("%.2f", monthlySales));
        } else if (selected.equals("Yearly Sales")) {
            View_txt.setText("Yearly Sales");
            double yearlySales = getYearlySales();
            View_money.setText("B " + String.format("%.2f", yearlySales));
        }
    }

    private double getDailySales() {
        double total = 0;
        try {
            String sql = "SELECT SUM(amount) AS total FROM payment WHERE DATE(payment_date) = CURDATE()";
            java.sql.Connection con = DatabaseConnection.getConnection();

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private double getMonthlySales() {
        double total = 0;
        try {
            String sql = "SELECT SUM(amount) AS total FROM payment WHERE MONTH(payment_date) = MONTH(CURDATE()) AND YEAR(payment_date) = YEAR(CURDATE())";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private double getYearlySales() {
        double total = 0;
        try {
            String sql = "SELECT SUM(amount) AS total FROM payment WHERE YEAR(payment_date) = YEAR(CURDATE())";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private void loadMemberList() {
        DefaultTableModel model = (DefaultTableModel) tb_member_list.getModel();
        model.setRowCount(0); // ล้างข้อมูลก่อนทุกครั้ง

        try {
            String sql = "SELECT member_id, firstname, join_date, package_id FROM member";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("member_id");
                String fname = rs.getString("firstname");
                String join = rs.getString("join_date");
                int packageId = rs.getInt("package_id");

                model.addRow(new Object[]{id, fname, join, packageId});
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNewMembers() {
        DefaultTableModel model = (DefaultTableModel) tb_new_member.getModel();
        model.setRowCount(0); // ล้างข้อมูลก่อน

        try {
            String sql = "SELECT member_id, firstname, join_date FROM member WHERE join_date >= CURDATE() - INTERVAL 7 DAY";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("member_id");
                String fname = rs.getString("firstname");
                String join = rs.getString("join_date");

                model.addRow(new Object[]{id, fname, join});
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTotalAmount() {
        try {
            String sql = "SELECT SUM(amount) AS total FROM payment";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double total = rs.getDouble("total");
                txt_money.setText("B " + String.format("%.2f", total));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTotalMembers() {
        try {
            String sql = "SELECT COUNT(*) AS total FROM member";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                txt_member.setText(String.valueOf(total));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateActiveMembers() {
        try {
            String sql = "SELECT COUNT(*) AS total FROM member WHERE status = 'Active'";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                txt_Active.setText(String.valueOf(total));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCheckinCount() {
        try {
            String sql = "SELECT COUNT(*) AS total_checkins FROM checkin_log WHERE checkin_time IS NOT NULL AND checkout_time IS NULL";
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int totalCheckins = rs.getInt("total_checkins");
                txt_checkin_time.setText(String.valueOf(totalCheckins));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAutoUpdate() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCheckinCount();  // อัปเดตจำนวนคนเช็คอิน
                updateTotalAmount();    // อัปเดตยอดขาย
                updateTotalMembers();   // อัปเดตจำนวนสมาชิกทั้งหมด
                updateActiveMembers();  // อัปเดตจำนวนสมาชิกที่ยัง Active
            }
        }, 0, 2000);  // 2000 = 2 วินาที
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JLayeredPane();
        panelBorder7 = new com.raven.swing.PanelBorder();
        View_Sales = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        View_txt = new javax.swing.JLabel();
        View_money = new javax.swing.JLabel();
        panelBorder10 = new com.raven.swing.PanelBorder();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_member_list = new javax.swing.JTable();
        panelBorder11 = new com.raven.swing.PanelBorder();
        panelBorder15 = new com.raven.swing.PanelBorder();
        txt_checkin_time = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelBorder8 = new com.raven.swing.PanelBorder();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_new_member = new javax.swing.JTable();
        panelBorder14 = new com.raven.swing.PanelBorder();
        jLabel10 = new javax.swing.JLabel();
        txt_time = new javax.swing.JLabel();
        panelBorder12 = new com.raven.swing.PanelBorder();
        panelBorder17 = new com.raven.swing.PanelBorder();
        txt_member = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelBorder13 = new com.raven.swing.PanelBorder();
        panelBorder19 = new com.raven.swing.PanelBorder();
        txt_Active = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelBorder18 = new com.raven.swing.PanelBorder();
        panelBorder16 = new com.raven.swing.PanelBorder();
        txt_money = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(237, 241, 245));

        panel.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        panelBorder7.setBackground(new java.awt.Color(255, 255, 255));

        View_Sales.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        View_Sales.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily Sales", "Monthly Sales", "Yearly Sales" }));
        View_Sales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                View_SalesActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Sales Report");

        View_txt.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        View_txt.setForeground(new java.awt.Color(204, 204, 204));
        View_txt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        View_txt.setText("Daily Sales");

        View_money.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        View_money.setForeground(new java.awt.Color(14, 165, 233));
        View_money.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        View_money.setText("B 0.00");

        javax.swing.GroupLayout panelBorder7Layout = new javax.swing.GroupLayout(panelBorder7);
        panelBorder7.setLayout(panelBorder7Layout);
        panelBorder7Layout.setHorizontalGroup(
            panelBorder7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder7Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(panelBorder7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(View_Sales, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBorder7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(View_money, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(View_txt, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addGap(32, 32, 32))
        );
        panelBorder7Layout.setVerticalGroup(
            panelBorder7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panelBorder7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(View_txt))
                .addGap(18, 18, 18)
                .addGroup(panelBorder7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(View_money)
                    .addComponent(View_Sales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );

        panelBorder10.setBackground(new java.awt.Color(255, 255, 255));

        tb_member_list.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tb_member_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Firstname", "Join_data", "Package_ID"
            }
        ));
        jScrollPane1.setViewportView(tb_member_list);

        javax.swing.GroupLayout panelBorder10Layout = new javax.swing.GroupLayout(panelBorder10);
        panelBorder10.setLayout(panelBorder10Layout);
        panelBorder10Layout.setHorizontalGroup(
            panelBorder10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder10Layout.setVerticalGroup(
            panelBorder10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelBorder11.setBackground(new java.awt.Color(255, 255, 255));

        panelBorder15.setBackground(new java.awt.Color(245, 158, 11));

        javax.swing.GroupLayout panelBorder15Layout = new javax.swing.GroupLayout(panelBorder15);
        panelBorder15.setLayout(panelBorder15Layout);
        panelBorder15Layout.setHorizontalGroup(
            panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        panelBorder15Layout.setVerticalGroup(
            panelBorder15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txt_checkin_time.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txt_checkin_time.setForeground(new java.awt.Color(245, 158, 11));
        txt_checkin_time.setText("B 0.00");

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("Check-in");

        javax.swing.GroupLayout panelBorder11Layout = new javax.swing.GroupLayout(panelBorder11);
        panelBorder11.setLayout(panelBorder11Layout);
        panelBorder11Layout.setHorizontalGroup(
            panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder11Layout.createSequentialGroup()
                .addComponent(panelBorder15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBorder11Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txt_checkin_time)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        panelBorder11Layout.setVerticalGroup(
            panelBorder11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_checkin_time)
                .addGap(24, 24, 24))
            .addComponent(panelBorder15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelBorder8.setBackground(new java.awt.Color(255, 255, 255));

        tb_new_member.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tb_new_member.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "member_id", "firstname", "join_date"
            }
        ));
        jScrollPane2.setViewportView(tb_new_member);

        javax.swing.GroupLayout panelBorder8Layout = new javax.swing.GroupLayout(panelBorder8);
        panelBorder8.setLayout(panelBorder8Layout);
        panelBorder8Layout.setHorizontalGroup(
            panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        panelBorder8Layout.setVerticalGroup(
            panelBorder8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelBorder14.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 153, 153));
        jLabel10.setText("Time");

        txt_time.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        txt_time.setForeground(new java.awt.Color(102, 102, 102));
        txt_time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_time.setText("00.00.00");

        javax.swing.GroupLayout panelBorder14Layout = new javax.swing.GroupLayout(panelBorder14);
        panelBorder14.setLayout(panelBorder14Layout);
        panelBorder14Layout.setHorizontalGroup(
            panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder14Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_time, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder14Layout.setVerticalGroup(
            panelBorder14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder14Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel10)
                .addGap(32, 32, 32)
                .addComponent(txt_time)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBorder12.setBackground(new java.awt.Color(255, 255, 255));

        panelBorder17.setBackground(new java.awt.Color(14, 165, 233));

        javax.swing.GroupLayout panelBorder17Layout = new javax.swing.GroupLayout(panelBorder17);
        panelBorder17.setLayout(panelBorder17Layout);
        panelBorder17Layout.setHorizontalGroup(
            panelBorder17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        panelBorder17Layout.setVerticalGroup(
            panelBorder17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        txt_member.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txt_member.setForeground(new java.awt.Color(14, 165, 233));
        txt_member.setText("B 0.00");

        jLabel3.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("Member");

        javax.swing.GroupLayout panelBorder12Layout = new javax.swing.GroupLayout(panelBorder12);
        panelBorder12.setLayout(panelBorder12Layout);
        panelBorder12Layout.setHorizontalGroup(
            panelBorder12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder12Layout.createSequentialGroup()
                .addComponent(panelBorder17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelBorder12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder12Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 75, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_member, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelBorder12Layout.setVerticalGroup(
            panelBorder12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBorder17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelBorder12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_member)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBorder13.setBackground(new java.awt.Color(255, 255, 255));

        panelBorder19.setBackground(new java.awt.Color(239, 68, 68));

        javax.swing.GroupLayout panelBorder19Layout = new javax.swing.GroupLayout(panelBorder19);
        panelBorder19.setLayout(panelBorder19Layout);
        panelBorder19Layout.setHorizontalGroup(
            panelBorder19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        panelBorder19Layout.setVerticalGroup(
            panelBorder19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        txt_Active.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txt_Active.setForeground(new java.awt.Color(239, 68, 68));
        txt_Active.setText("B 0.00");

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("Member access");

        javax.swing.GroupLayout panelBorder13Layout = new javax.swing.GroupLayout(panelBorder13);
        panelBorder13.setLayout(panelBorder13Layout);
        panelBorder13Layout.setHorizontalGroup(
            panelBorder13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder13Layout.createSequentialGroup()
                .addComponent(panelBorder19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelBorder13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Active, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBorder13Layout.setVerticalGroup(
            panelBorder13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBorder19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelBorder13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_Active)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBorder18.setBackground(new java.awt.Color(255, 255, 255));

        panelBorder16.setBackground(new java.awt.Color(34, 197, 94));

        javax.swing.GroupLayout panelBorder16Layout = new javax.swing.GroupLayout(panelBorder16);
        panelBorder16.setLayout(panelBorder16Layout);
        panelBorder16Layout.setHorizontalGroup(
            panelBorder16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        panelBorder16Layout.setVerticalGroup(
            panelBorder16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        txt_money.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        txt_money.setForeground(new java.awt.Color(34, 197, 94));
        txt_money.setText("B 0.00");

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Sales");

        javax.swing.GroupLayout panelBorder18Layout = new javax.swing.GroupLayout(panelBorder18);
        panelBorder18.setLayout(panelBorder18Layout);
        panelBorder18Layout.setHorizontalGroup(
            panelBorder18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder18Layout.createSequentialGroup()
                .addComponent(panelBorder16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelBorder18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder18Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 75, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder18Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_money, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelBorder18Layout.setVerticalGroup(
            panelBorder18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panelBorder16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelBorder18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_money)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(panelBorder7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(panelBorder14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelBorder11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(panelBorder18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelBorder12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelBorder13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addComponent(panelBorder8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(panelBorder10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(panel)
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBorder10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelBorder13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBorder11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBorder12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBorder18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelBorder7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelBorder14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelBorder8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void View_SalesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_View_SalesActionPerformed
        viewSalesChanged();
    }// GEN-LAST:event_View_SalesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> View_Sales;
    private javax.swing.JLabel View_money;
    private javax.swing.JLabel View_txt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLayeredPane panel;
    private com.raven.swing.PanelBorder panelBorder10;
    private com.raven.swing.PanelBorder panelBorder11;
    private com.raven.swing.PanelBorder panelBorder12;
    private com.raven.swing.PanelBorder panelBorder13;
    private com.raven.swing.PanelBorder panelBorder14;
    private com.raven.swing.PanelBorder panelBorder15;
    private com.raven.swing.PanelBorder panelBorder16;
    private com.raven.swing.PanelBorder panelBorder17;
    private com.raven.swing.PanelBorder panelBorder18;
    private com.raven.swing.PanelBorder panelBorder19;
    private com.raven.swing.PanelBorder panelBorder7;
    private com.raven.swing.PanelBorder panelBorder8;
    private javax.swing.JTable tb_member_list;
    private javax.swing.JTable tb_new_member;
    private javax.swing.JLabel txt_Active;
    private javax.swing.JLabel txt_checkin_time;
    private javax.swing.JLabel txt_member;
    private javax.swing.JLabel txt_money;
    private javax.swing.JLabel txt_time;
    // End of variables declaration//GEN-END:variables
}
