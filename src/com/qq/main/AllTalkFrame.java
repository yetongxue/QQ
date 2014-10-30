package com.qq.main;

import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class AllTalkFrame extends JFrame {

	TextArea oldMessageTextArea;
	TextArea sendMessageTextArea;
	JList userList;
	JScrollPane userListPane;
	JButton btSend;
	JButton btClosed;
	JButton upLine;
	String doubleClickedName;
	Client client;
	String clientName;
	JLabel userlistTitle;
	Vector users;

	// 只要服务器端有消息，就将消息显示到oldMessageTextArea
	class showOldMessageThread implements Runnable {
		public void run() {
			boolean flag = true;
			while (flag) {
				try {
					// 接收群聊服务器端回发过来的消息
					String serverOutput = client.br.readLine() + "\r\n";
					if (!serverOutput.startsWith("私聊")
							&& !serverOutput.startsWith("*")
							&& !(serverOutput.substring(serverOutput
									.indexOf("：") + 1).equals("\r\n"))) {
						String s1 = serverOutput.replace('说', ' ');
						String s = s1.replaceAll("罎", "\r\n     ");
						oldMessageTextArea.append(s);
					}

					// 添加客户端的用户在线列表
					if (!serverOutput.startsWith("*")
							&& !serverOutput.startsWith("私聊")
							&& (serverOutput.indexOf("说") != -1)) {
						String listName = serverOutput.substring(0,
								serverOutput.indexOf('说'));
						// 如果JList中有相同名字的用户，则不添加，否则添加
						if (!users.contains(listName)) {
							System.out.println("用户" + listName + "上线了");
							users.add(listName);
							userList.setListData(users);
						}
					}

					// 判断服务器回发过来的消息是不是以"私聊"开头的，是的话就提取出这两个用户名
					if (serverOutput.startsWith("私聊")) {
						String siliaoName1 = serverOutput.substring(
								serverOutput.indexOf("*") + 1, serverOutput
										.indexOf("和"));
						String siliaoName2 = serverOutput.substring(
								serverOutput.indexOf("和") + 1, serverOutput
										.indexOf("\r"));
						String siliaoBenshen = "";
						String siliaoDuixiangName = "";
						if (siliaoName1.equals(clientName)) {
							siliaoBenshen = siliaoName1;
							siliaoDuixiangName = siliaoName2;
						} else {
							siliaoBenshen = siliaoName2;
							siliaoDuixiangName = siliaoName1;
						}
						// 判断这两个名字中是否有与自己同名的，有的话就弹出个私聊窗口
						if (siliaoName1.equals(clientName)
								|| siliaoName2.equals(clientName)) {
							new PointToPointTalkFrame(siliaoBenshen + "和"
									+ siliaoDuixiangName).setVisible(true);
						}
					}
				} catch (IOException e1) {
					System.out.println("读取服务器端消息出错");
				}
			}
		}
	}

	AllTalkFrame(final String clientName) {
		this.clientName = clientName;
		client = new Client(clientName);
		this.setTitle("欢迎您：" + clientName);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int x = (int) screenSize.getWidth();
		int y = (int) screenSize.getHeight();
		this.setBounds((x - 600) / 2, (y - 600) / 2, 600, 600);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 设置已经发出去的消息窗口的属性
		oldMessageTextArea = new TextArea();
		oldMessageTextArea.setBounds(0, 0, 390, 360);

		// 设置准备发送消息窗口的属性
		sendMessageTextArea = new TextArea(3, 3);
		sendMessageTextArea.setBounds(0, 380, 390, 140);
		sendMessageTextArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out
							.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				}
			}

		});

		// 设置<上线>按钮的属性
		upLine = new JButton("上线");
		upLine.setBounds(0, 530, 70, 30);
		
		// 注册<上线>按钮的点击事件
		upLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upLine.setEnabled(false);
				String s = sendMessageTextArea.getText();
				client.ps.println(clientName + "说：" + s);
				sendMessageTextArea.setText("");
			}
		});

		// 设置<发送>按钮的属性
		btSend = new JButton("发送");
		btSend.setBounds(240, 530, 70, 30);
		
		// 注册<发送>按钮的点击事件
		btSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upLine.setEnabled(false);
				String s1 = sendMessageTextArea.getText();
				String s = s1.replaceAll("\r\n", "罎");
				client.ps.println(clientName + "说：" + s);
				sendMessageTextArea.setText("");
			}
		});
		
		// 设置<关闭>按钮的属性
		btClosed = new JButton("关闭");
		btClosed.setBounds(320, 530, 70, 30);
		
		// 注册<关闭>按钮的点击事件
		btClosed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// AllTalkFrame.this.dispose();
				System.exit(0);
			}
		});

		// 设置用户列表的标题
		userlistTitle = new JLabel("当前在线用户列表,双击进行私聊");
		userlistTitle.setBounds(400, 0, 200, 20);
		
		// 设置用户列表JList属性
		userList = new JList();
		userList.setBounds(400, 20, 200, 600);
		users = new Vector();
		
		// 注册JList的点击事件
		userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (AllTalkFrame.this.userList.getSelectedValue()
							.toString().equals(clientName)) {
						JOptionPane.showMessageDialog(null, "不能和自己聊天");
					} else {
						String PToPMemberName = "私聊"
								+ "*"
								+ clientName
								+ "和"
								+ AllTalkFrame.this.userList.getSelectedValue()
										.toString();
						client.ps.println(PToPMemberName);
					}
				}
			}
		});

		// 设置用户列表JScrollPane的属性
		userListPane = new JScrollPane(userList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		userListPane.setBounds(400, 0, 200, 600);

		// new 一个AllTalkFrame时，此线程启动，获取服务器端回发的消息
		new Thread(new showOldMessageThread()).start();

		// 将所有组件添加到窗体上
		this.add(oldMessageTextArea);
		this.add(sendMessageTextArea);
		this.add(btSend);
		this.add(upLine);
		this.add(btClosed);
		this.add(userListPane);
		this.add(userlistTitle);
	}
}
