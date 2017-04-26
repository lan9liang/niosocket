package org.shirdrn.java.communications.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * NIO客户端
 * 
 */
public class NioTcpClient {

	private static final Logger log = Logger.getLogger(NioTcpClient.class.getName());
	private InetSocketAddress inetSocketAddress;
	
	public NioTcpClient(String hostname, int port) {
		inetSocketAddress = new InetSocketAddress(hostname, port);
	}
	
	/**
	 * 发送请求数据
	 * @param requestData
	 */
	public void send(String requestData) {
		try {
			SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
			socketChannel.configureBlocking(false);
			ByteBuffer byteBuffer = ByteBuffer.allocate(512);
			socketChannel.write(ByteBuffer.wrap(requestData.getBytes()));
			while (true) {
				byteBuffer.clear();
				int readBytes = socketChannel.read(byteBuffer);
				if (readBytes > 0) {
					byteBuffer.flip();
					log.info("客户端得到服务器端的消息:" + new String(byteBuffer.array(), 0, readBytes));
					socketChannel.close();
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String hostname = "192.168.1.231";
//		String hostname = "120.77.206.22";
		String requestData ="111111";
		int port = 1000;
		new NioTcpClient(hostname, port).send(requestData);
	}
}