package at.medunigraz.imi.bst.trec.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import at.medunigraz.imi.bst.config.TrecConfig;

public class ConnectionUtils {
	/**
	 * Checks whether a given port is open on a server
	 * 
	 * @param hostname
	 * @param port
	 * @return
	 */
	public static boolean checkOpenPort(String hostname, int port) {		
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(hostname, port), 1000);
			return true;
		} catch (IOException ignored) {
			return false;
		}
	}

	public static boolean checkElasticOpenPort() {
		return ConnectionUtils.checkOpenPort(TrecConfig.ELASTIC_HOSTNAME, TrecConfig.ELASTIC_PORT);
	}
}
