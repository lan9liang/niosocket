package com.xy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class TCPServer {
	// 缓冲区大小
	private static final int BufferSize = 1024;
	// 超时时间，单位毫秒
	private static final int TimeOut = 3000;
	// 本地监听端口
	private static final int ListenPort = 1978;
	public static void main(String[] args) throws IOException {
		// 创建选择器
		Selector selector = Selector.open();
		// 打开监听信道
		ServerSocketChannel listenerChannel = ServerSocketChannel.open();
		// 与本地端口绑定
		listenerChannel.socket().bind(new InetSocketAddress("192.168.0.102",ListenPort));
		// 设置为非阻塞模式
		listenerChannel.configureBlocking(false);
		// 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
		listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
		// 创建一个处理协议的实现类,由它来具体操作
		TCPProtocol protocol = new TCPProtocolImpl(BufferSize);

		// 反复循环,等待IO
		while (true) {
			// 等待某信道就绪(或超时)
			if (selector.select(TimeOut) == 0) {// 监听注册通道，当其中有注册的 IO
												// 操作可以进行时，该函数返回，并将对应的
												// SelectionKey 加入 selected-key
												// set
				System.out.print("独自等待.");
				continue;
			}
			// 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
			// Selected-key Iterator 代表了所有通过 select() 方法监测到可以进行 IO 操作的 channel
			// ，这个集合可以通过 selectedKeys() 拿到
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();
				SelectionKey key1;
				if (keyIter.hasNext()) {
					key1 = keyIter.next();
				}
				try {
					if (key.isAcceptable()) {
						// 有客户端连接请求时
						protocol.handleAccept(key);
					}
					if (key.isReadable()) {// 判断是否有数据发送过来
						// 从客户端读取数据
						protocol.handleRead(key);
					}
					if (key.isValid() && key.isWritable()) {// 判断是否有效及可以发送给客户端
						// 客户端可写时
						protocol.handleWrite(key);
					}
				} catch (IOException ex) {
					// 出现IO异常（如客户端断开连接）时移除处理过的键
					keyIter.remove();
					continue;
				}
				// 移除处理过的键
				keyIter.remove();
			}
		}
	}
}