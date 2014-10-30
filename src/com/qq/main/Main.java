package com.qq.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.qq.regist.Regist;
import com.qq.regist.UserInformation;

/**
 * QQ的主界面
 * 
 * @param args
 */

public class Main extends JFrame {

	private JLabel userId;
	private JLabel userPassword;
	private JTextField inputId;
	private JPasswordField inputPassword;
	private JButton btLogin;
	private JButton btRegist;

	Main() {
		userId = new JLabel("帐号");
		userPassword = new JLabel("密码");
		inputId = new JTextField(6);// 疑问：？？
		inputPassword = new JPasswordField();
		btLogin = new JButton("登陆");
		btRegist = new JButton("注册");

		// 设置窗体属性
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int x = (int) screenSize.getWidth();
		int y = (int) screenSize.getHeight();
		this.setBounds((x - 240) / 2, (y - 600) / 2, 240, 600);
		this.setResizable(false);
		this.setLayout(null);

		this.setBackground(Color.BLACK);// 疑问？？
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 设置JLabel属性
		userId.setBounds(30, 160, 40, 20);
		userPassword.setBounds(30, 200, 40, 20);
		// 设置文本域属性
		inputId.setBounds(90, 160, 100, 20);
		inputPassword.setBounds(90, 200, 100, 20);
		inputPassword.setEchoChar('*');
		// 设置JButton属性
		btLogin.setBounds(50, 240, 60, 20);
		btRegist.setBounds(120, 240, 60, 20);

		// 注册《登陆》按钮监听器
		btLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserInformation user = new UserInformation();
				String userName = inputId.getText();
				String userPassword = new String(inputPassword.getPassword());
				if (userName.equals("")) {
					JOptionPane.showMessageDialog(null, "用户名不能为空");
				} else if ("".equals(userPassword)) {
					JOptionPane.showMessageDialog(null, "密码不能为空");
				} else if (user.isExist(userName)
						&& user.userInfomation.getProperty(userName).equals(
								userPassword)) {
					// 判断成功后new一个群聊窗口
					new AllTalkFrame(userName).setVisible(true);
					Main.this.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "此用户名不存在或者密码不正确");
				}
			}
		});

		// 注册《注册》按钮监听器
		btRegist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Regist();
			}
		});
		this.add(userId);
		this.add(userPassword);
		this.add(inputId);
		this.add(inputPassword);
		this.add(btLogin);
		this.add(btRegist);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
}
