package com.michir.projects.elastic.engine;

import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michir.projects.elastic.api.AnyDto;

@ApplicationScoped
public class ElasticSearchIndexer {

	@Inject
	private Client client;
	
	private Logger logger = Logger.getLogger("application");
	
	@Asynchronous
	public void observers(@Observes AnyDto anyDto) throws JsonProcessingException {
		logger.info("indexing "+anyDto.getUid());
		String id = index("anyindex", "anytype", anyDto);
		logger.info("index generated "+id);
	}
	
	public <T> String index(String indexName, String type, T object) throws JsonProcessingException {
		IndexResponse resp = client.prepareIndex(indexName, type)
			.setSource(new ObjectMapper().writeValueAsString(object)).get();
		return resp.getId();
	}
	
}
