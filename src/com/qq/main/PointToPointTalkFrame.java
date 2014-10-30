package com.qq.main;

import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PointToPointTalkFrame extends JFrame {

	TextArea oldMessageTextArea;
	TextArea sendMessageTextArea;
	JButton btSend;
	JButton btClosed;
	JButton sendFile;
	JLabel upShow;
	JLabel downShow;
	Client client;
	String clientName;

	// 线程：只要服务器端有消息，就将消息显示到oldMessageTextArea
	class showOldMessageThread implements Runnable {
		public void run() {
			boolean flag = true;
			while (flag) {
				try {
					// 接收服务器端回发过来的消息
					String serverOutput = client.br.readLine() + "\r\n";
					System.out.println("私聊服务器发过来的消息：" + serverOutput);
					// 将消息中的两个用户名提取出来
					String s1 = "";
					if (serverOutput.startsWith("私聊")) {
						String[] s;
						if (serverOutput.startsWith("私聊*")) {
							s = serverOutput.substring(3,
									serverOutput.indexOf("\r")).split("和");
						} else {
							s = serverOutput.substring(2,
									serverOutput.indexOf("说")).split("和");
						}
						for (int i = 0; i < s.length; i++) {
							s1 = s1 + s[i];
						}
					}
					// 将标题中的两个用户名提取出来,有两种顺序
					String[] title = clientName.split("和");
					String s2 = "";
					for (int i = 0; i < title.length; i++) {
						s2 = s2 + title[i];
					}
					String s3 = "";
					for (int i = title.length - 1; i >= 0; i--) {
						s3 = s3 + title[i];
					}
					// 判断服务器端返回消息中的两个用户名等于客户端的两个用户名（忽略顺序），则将消息显示出来（也就是显示在私聊窗口上）
					if (s1.equals(s2) || s1.equals(s3)) {
						String ss1 = serverOutput.substring(2, serverOutput
								.indexOf("和"));
						String ss2 = serverOutput.substring(serverOutput
								.indexOf("："));
						// 将私聊客户端返回来的一行字符串按"砳"进行拆分成多行，显示在私聊的oldMessageArea中
						if (ss2.indexOf("砳") != -1) {
							ss2 = ss2.replaceAll("砳", "\r\n     ");
						}
						oldMessageTextArea.append(ss1 + ss2);
					}
				} catch (IOException e1) {
					System.out.println("读取服务器端消息出错");
				}
			}
		}
	}

	PointToPointTalkFrame(final String clientName) {
		// 这里的clientName是XXX和XXX的形式
		this.clientName = clientName;
		client = new Client(clientName);
		this.setTitle("与【" + clientName.substring(clientName.indexOf("和") + 1)
				+ "】私聊中");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int x = (int) screenSize.getWidth();
		int y = (int) screenSize.getHeight();
		this.setBounds((x - 600) / 2, (y - 600) / 2, 600, 600);
		this.setResizable(false);
		this.setLayout(null);
		this.setState(JFrame.EXIT_ON_CLOSE);

		// 设置已经发出去的消息窗口的属性
		oldMessageTextArea = new TextArea();
		oldMessageTextArea.setBounds(0, 0, 390, 360);

		// 设置准备发送消息窗口的属性
		sendMessageTextArea = new TextArea(3, 3);
		sendMessageTextArea.setBounds(0, 380, 390, 140);

		// 设置个人形象Label
		upShow = new JLabel(new ImageIcon(this.getClass().getClassLoader()
				.getResource("images/企鹅.JPG")));
		upShow.setBounds(400, 0, 180, 300);
		downShow = new JLabel(new ImageIcon(this.getClass().getClassLoader()
				.getResource("images/会员.gif")));
		downShow.setBounds(390, 280, 200, 300);

		// 设置<发送>按钮的属性
		btSend = new JButton("发送");
		btSend.setBounds(240, 530, 70, 30);

		// 注册<发送>按钮的点击事件
		btSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// String s = sendMessageTextArea.getText();
				String s1 = sendMessageTextArea.getText();
				// 将要发送的多行文本中的"回车"替换成"砳",形成一行字符串发送到服务器端
				String s = s1.replaceAll("\r\n", "砳");
				client.ps.println("私聊" + PointToPointTalkFrame.this.clientName
						+ "说：" + s);
				sendMessageTextArea.setText("");
			}
		});

		// 设置<关闭>按钮的属性
		btClosed = new JButton("关闭");
		btClosed.setBounds(320, 530, 70, 30);

		// 注册<关闭>按钮的点击事件
		btClosed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PointToPointTalkFrame.this.dispose();
			}
		});

		// 设置<传输文件>按钮的属性
		/*
		 * sendFile = new JButton("传输文件"); sendFile.setBounds(0, 530, 70, 30);
		 */

		// 构造一个PointToPointTalkFrame窗体时，启动此线程，来获取服务器端的消息
		new Thread(new showOldMessageThread()).start();

		this.add(oldMessageTextArea);
		this.add(sendMessageTextArea);
		this.add(upShow);
		this.add(downShow);
		// this.add(sendFile);
		this.add(btSend);
		this.add(btClosed);

	}
}
