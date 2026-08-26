package javaapplication2;


import java.sql.Connection;
import javax.swing.JOptionPane;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Timer;


public class login extends javax.swing.JFrame {

    public login() {
        repaint();
        setUndecorated(true);

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);

        this.setOpacity(0f);

        btn_login.putClientProperty("JButton.buttonType", "roundRect");

        Timer timer = new Timer(30, new ActionListener() {
            private float opacity = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1f) {
                    ((Timer) e.getSource()).stop();
                    setOpacity(1f);
                }
                setOpacity(opacity);
            }
        });
        timer.start();

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to exit?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });
        setFocusable(true); 

        txt_name.setFocusable(true);
        txt_password.setFocusable(true);

     
        txt_name.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    btn_loginActionPerformed(null);
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to exit?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0); 
                    }
                }
            }
        });


        txt_password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    btn_loginActionPerformed(null);
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to exit?",
                            "Exit Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser2 = new javax.swing.JFileChooser();
        jLabel5 = new javax.swing.JLabel();
        panelBorder2 = new com.raven.swing.PanelBorder();
        panelBorder1 = new com.raven.swing.PanelBorder();
        txt_name = new javax.swing.JTextField();
        txt_password = new javax.swing.JPasswordField();
        btn_login = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelBorder2.setBackground(new java.awt.Color(252, 251, 252));

        panelBorder1.setBackground(new java.awt.Color(252, 251, 252));

        txt_name.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_name.setToolTipText("Username");
        txt_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nameActionPerformed(evt);
            }
        });

        txt_password.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_password.setToolTipText("Password");
        txt_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_passwordActionPerformed(evt);
            }
        });

        btn_login.setBackground(new java.awt.Color(51, 81, 201));
        btn_login.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        btn_login.setForeground(new java.awt.Color(255, 255, 255));
        btn_login.setText("LOGIN");
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });
        btn_login.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_loginKeyPressed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 81, 201));
        jLabel6.setText("Welcome"); // NOI18N

        jLabel4.setFont(new java.awt.Font("TH Sarabun New", 1, 36)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pic/icon.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel1.setText("Username");

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jLabel2.setText("Password");

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(69, 69, 69))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(49, Short.MAX_VALUE))))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(8, 8, 8)
                        .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(5, 5, 5)
                        .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel4)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelBorder2Layout = new javax.swing.GroupLayout(panelBorder2);
        panelBorder2.setLayout(panelBorder2Layout);
        panelBorder2Layout.setHorizontalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        panelBorder2Layout.setVerticalGroup(
            panelBorder2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorder2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nameActionPerformed

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passwordActionPerformed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_loginActionPerformed
        String username = txt_name.getText();
        String password = txt_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT * FROM user WHERE user_name = ? AND user_password = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                Main mainFrame = new Main();
                mainFrame.setOpacity(0f);
                mainFrame.setVisible(true);

                Timer timer = new Timer(30, new ActionListener() {
                    private float opacity = 0f;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        opacity += 0.05f;
                        if (opacity >= 1f) {
                            ((Timer) e.getSource()).stop();
                            mainFrame.setOpacity(1f);
                        }
                        mainFrame.setOpacity(opacity);
                    }
                });
                timer.start();

                this.dispose();
            } else {
                // If user does not exist
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btn_loginActionPerformed

    private void btn_loginKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_btn_loginKeyPressed
        // TODO add your handling code here:
    }// GEN-LAST:event_btn_loginKeyPressed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new login().setVisible(true);
            }

        });

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_login;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    private com.raven.swing.PanelBorder panelBorder1;
    private com.raven.swing.PanelBorder panelBorder2;
    public javax.swing.JTextField txt_name;
    public javax.swing.JPasswordField txt_password;
    // End of variables declaration//GEN-END:variables
}
