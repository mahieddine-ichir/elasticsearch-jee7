package com.michir.projects.elastic.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@ApplicationScoped
public class ElasticSearchSearcher {

	@Inject
	private Client client;
	
	@Inject
	private Logger logger;
	
	public Map<String, Float> search(MultivaluedMap<String,String> params) throws Exception {
		logger.info("searching "+params);
		
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		Arrays.asList(params.getFirst("q").split(" "))
			.forEach(e -> {
				String[] split = e.split(":");
				String name = split[0];
				String value = split[1];

				if (value.contains("*") || value.contains("?")) {
					boolQueryBuilder.should(QueryBuilders.wildcardQuery(name, value));
				} else {
					boolQueryBuilder.should(QueryBuilders.termQuery(name, value));
				}
				// boolQueryBuilder.must(QueryBuilders.termQuery(e.split(":")[0], e.split(":")[1]));
			});

		SearchResponse actionGet = client.prepareSearch(params.getFirst("index").split(" "))
			.setQuery(boolQueryBuilder).execute().actionGet();
		
		Map<String, Float> res = new HashMap<>();
		actionGet.getHits().forEach(h -> res.put(h.sourceAsString(), h.score()));
		return res;
	}
}
