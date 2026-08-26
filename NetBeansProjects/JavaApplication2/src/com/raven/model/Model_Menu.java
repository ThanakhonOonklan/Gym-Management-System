package com.raven.model;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Model_Menu {

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;

        JLabel label = new JLabel();
        label.setIcon(toIcon()); 
        label.setPreferredSize(new Dimension(25, 25)); 

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public Model_Menu(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }

    public Model_Menu() {
    }

    private String icon;
    private String name;
    private MenuType type;

    public Icon toIcon() {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/raven/icon/" + icon + ".png"));

        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public static enum MenuType {
        TITLE, MENU, EMPTY
    }
}
