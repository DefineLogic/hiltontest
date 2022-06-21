package com.kamal.hiltontest.service;
 

import com.kamal.hiltontest.model.GeoLocationData;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient
{
    private Client client;
 
    public RestClient(Client client) {
        this.client = client;
    }

    public GeoLocationData getIpGeoLocationInformation(String ipAddress)
    {
        WebTarget webTarget = client.target("http://ip-api.com/json/"+ipAddress);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        GeoLocationData geoLocationData = response.readEntity(GeoLocationData.class);
        System.out.println(geoLocationData.toString());
        return geoLocationData;
    }
}