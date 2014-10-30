package com.qq.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	PrintStream ps;
	BufferedReader br;
	Socket clientSocket;

	public Client(String clientName) {
		try {
			// 创建客户端sSocket
			clientSocket = new Socket("127.0.0.1", 9999);
			System.out.println(clientName + "连接服务器成功");
			// 启用输出流
			OutputStream os = clientSocket.getOutputStream();
			ps = new PrintStream(os);

			// 启用输入流
			InputStream is = clientSocket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

		} catch (UnknownHostException e) {
			System.out.println("您未能连接上主机");
		} catch (IOException e) {
			System.out.println("您未能连接上主机");
		} finally {
			// clientSocket.close();
		}

	}
}
