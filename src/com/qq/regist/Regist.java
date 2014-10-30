package com.qq.regist;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Regist extends JDialog {

	private JLabel userId;
	private JLabel userPassword;
	private JLabel userPasswordConfirm;
	private JTextField inputId;
	private JPasswordField inputPassword;
	private JPasswordField inputPasswordConfirm;
	private JButton btSubmit;
	private JButton btCancel;

	public Regist() {
		// 初始化JLabel和JButton
		userId = new JLabel("帐号(昵称)");
		userPassword = new JLabel("密码");
		userPasswordConfirm = new JLabel("密码重复");
		inputId = new JTextField("可以是中文");
		inputPassword = new JPasswordField();
		inputPasswordConfirm = new JPasswordField();
		btSubmit = new JButton("提交");
		btCancel = new JButton("取消");

		// 设置此JFrame的属性
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int x = (int) screenSize.getWidth();
		int y = (int) screenSize.getHeight();
		this.setBounds((x - 240) / 2, (y - 600) / 2, 240, 600);
		this.setLayout(null);
		this.setModal(true);

		// 设置JLabel属性
		userId.setBounds(30, 100, 60, 20);
		userPassword.setBounds(30, 140, 40, 20);
		userPasswordConfirm.setBounds(30, 180, 60, 20);
		// 设置文本域属性
		inputId.setBounds(90, 100, 100, 20);
		inputPassword.setBounds(90, 140, 100, 20);
		inputPassword.setEchoChar('*');
		inputPasswordConfirm.setBounds(90, 180, 100, 20);
		inputPasswordConfirm.setEchoChar('*');
		// 设置JButton属性
		btSubmit.setBounds(50, 240, 60, 20);
		btCancel.setBounds(120, 240, 60, 20);

		// 添加inputId的监听器
		inputId.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				inputId.setText("");
			}
		});

		// 添加inputPassword的监听器
		inputPassword.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				inputPassword.setText("");
			}
		});
		inputPassword.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				if ((new String(inputPassword.getPassword())).length() < 6) {
					JOptionPane.showMessageDialog(null, "密码长度必须大于6");
					inputPassword.setText("");
				}
			}
		});
		// 注册inputPasswordConfirm的监听器
		inputPasswordConfirm.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				inputPasswordConfirm.setText("");
			}
		});

		// 注册《提交》按钮的监听器
		btSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName = inputId.getText();
				String userPassword = new String(inputPassword.getPassword());
				String userPasswordConfirm = new String(inputPasswordConfirm
						.getPassword());
				System.out.println("您点击了提交按钮");
				if (userName.equals("")) {
					JOptionPane.showMessageDialog(null, "用户名不能为空");
				} else if ("".equals(userPassword)
						|| "".equals(userPasswordConfirm)) {
					JOptionPane.showMessageDialog(null, "密码和密码重复都不能为空");
				} else if (!userPassword.equals(userPasswordConfirm)) {
					JOptionPane.showMessageDialog(null, "密码和密码重复不一致");
				} else {
					UserInformation user = new UserInformation();
					if (user.isExist(userName)) {
						JOptionPane.showMessageDialog(null, "此用户名已存在");
					} else {
						JOptionPane.showMessageDialog(null, "注册成功");
						user.insert(userName, userPassword);
						Regist.this.dispose();
					}

				}
			}
		});

		// 注册《取消》按钮的监听器
		btCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("您点击了取消按钮");
				Regist.this.dispose();
			}
		});

		this.add(userId);
		this.add(userPassword);
		this.add(userPasswordConfirm);
		this.add(inputId);
		this.add(inputPassword);
		this.add(inputPasswordConfirm);
		this.add(btSubmit);
		this.add(btCancel);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Regist();
	}

}
