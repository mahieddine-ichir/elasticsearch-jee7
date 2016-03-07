package com.michir.projects.elastic.engine;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class ElasticSearchFactory {

	@Produces
	public Client client() throws UnknownHostException {
		//return TransportClient.builder().build();//.settings
		return TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	}
	
	public void close(@Disposes Client client) {
		client.close();
	}
	
	@Produces
	public Logger logger(InjectionPoint ip) {
		return Logger.getLogger(ip.getMember().getDeclaringClass().getName());
	}
	
	@Produces
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
