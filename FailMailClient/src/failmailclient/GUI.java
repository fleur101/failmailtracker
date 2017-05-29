/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package failmailclient;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 *
 * @author mirka
 */
public class GUI extends Frame implements ActionListener {
    TextField tf;
    private Socket tracker;
    
    
    public GUI(){
        tf=new TextField();  
        tf.setBounds(60,50,170,20);  
        Button b=new Button("click me");  
        b.setBounds(100,120,80,30);  
        b.addActionListener(this);//passing current instance  
        super.add(b); super.add(tf);  //adding button into frame  
        super.setSize(300,300);//frame size 300 width and 300 height  
        super.setLayout(null);//no layout manager  
        super.setVisible(true);//now frame will be visible, by default not visible  
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(tf.getText());
        System.out.println(e);
    }
}
