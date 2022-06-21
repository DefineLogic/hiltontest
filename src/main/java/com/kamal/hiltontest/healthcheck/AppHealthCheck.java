package com.kamal.hiltontest.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.kamal.hiltontest.model.GeoLocationData;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AppHealthCheck extends HealthCheck {
	private final Client client;

	public AppHealthCheck(Client client) {
		super();
		this.client = client;
	}

	@Override
	protected Result check() throws Exception {
		WebTarget webTarget = client.target("http://localhost:9000/ip/209.152.96.166");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		@SuppressWarnings("rawtypes")
		GeoLocationData geoLocationData = response.readEntity(GeoLocationData.class);
		if(geoLocationData !=null ){
			return Result.healthy();
		}
		return Result.unhealthy("API Failed");
	}
}