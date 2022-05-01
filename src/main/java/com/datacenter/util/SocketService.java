package com.datacenter.util;

import java.io.*;
import java.net.Socket;

/**
 * 行为验证服务类
 * 
 * @author 闫帆
 * @version 1.0 2017-09-11
 */
public class SocketService {
	/**
	 * 经过行为验证Socket服务(Behavior socket service)
	 * 
	 * @param address
	 * @param port
	 * @param message
	 *            传入的json数据(transmission json data)
	 * @return 返回认证数据(return authentication data)
	 * @throws IOException
	 */
	public String useSocket(String address, int port, String message) throws IOException {
		String data = null;
		try {
			Socket socket = new Socket(address, port);
			// 获取Socket的字节输出流
			OutputStream out = socket.getOutputStream();
			// 将字符节输出流包装成字符打印输出流
//			PrintWriter pw = new PrintWriter(out);
//			pw.println(message);
			// 刷新输出流
//			pw.flush();
			
			OutputStreamWriter outSW = new OutputStreamWriter(out, "UTF-8");
			BufferedWriter bw = new BufferedWriter(outSW);
			bw.write(message);	//向客户端反馈消息，加上分行符以便客户端接收
			bw.flush();


			// 调用shutDown方法关闭Socket的输出流
			socket.shutdownOutput();
			// 接收服务器端发来的响应信息，获取字节输入流
			InputStream in = socket.getInputStream();
			// 将字节输入流包装成字符输入流
			InputStreamReader isr = new InputStreamReader(in);
			// 为字符输入流添加缓冲
			BufferedReader br = new BufferedReader(isr);
			// 读取字符输入流中的数据信息
			/**
			 * data: return result
			 */
			data = br.readLine();
			// 判断是否有返回值(服务是否启动)
			if (data == null || data.equals("")) {
				data = "n";
			}
			// 调用shutDown方法关闭Socket的输入流
			socket.shutdownInput();
			br.close();
			isr.close();
			in.close();
//			pw.close();
			bw.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			data = "n";
		}
		return data;
	}
}
