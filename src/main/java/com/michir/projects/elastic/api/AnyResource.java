package com.michir.projects.elastic.api;

import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.michir.projects.elastic.engine.ElasticSearchSearcher;

@Path("/anys")
public class AnyResource {

	@Inject
	private Event<AnyDto> event;
	
	@Inject
	private ElasticSearchSearcher searcher;
	
	private Logger logger = Logger.getLogger("application");
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(AnyDto dto) {
		// save resource
		logger.info("Create resource "+dto.getUid());
		// fire
		event.fire(dto);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, Float> search(@Context UriInfo uriInfo) throws Exception {
		MultivaluedMap<String,String> params = uriInfo.getQueryParameters();
		return searcher.search(params);
	}
}
