package at.medunigraz.imi.bst.trec.search;

import java.io.Closeable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import at.medunigraz.imi.bst.config.TrecConfig;

public class ElasticClientFactory implements Closeable {

	private static Client client = null;

	public ElasticClientFactory() {
	}

	public static Client getClient() {
		if (client == null) {
			open();
		}
		return client;
	}

	@SuppressWarnings("resource")
	private static void open() {
		TransportAddress address;
		try {
			address = new InetSocketTransportAddress(InetAddress.getByName(TrecConfig.ELASTIC_HOSTNAME),
					TrecConfig.ELASTIC_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}

		client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(address);
	}

	public void close() {
		client.close();
		client = null;
	}

}
