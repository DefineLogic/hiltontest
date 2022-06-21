package com.kamal.hiltontest.controller;

import com.kamal.hiltontest.cache.GeoLocationDataCache;
import com.kamal.hiltontest.model.GeoLocationData;
import com.kamal.hiltontest.repository.GeoLocationDataDao;
import com.kamal.hiltontest.service.RestClient;
import com.kamal.hiltontest.utility.IpAddressValidatorPredicate;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Time;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

@Path("/ip")
@Produces(MediaType.APPLICATION_JSON)
public class GeoLocationDataResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationDataResource.class);

    private final Validator validator;

    private final GeoLocationDataCache geoLocationDataCache;

    private final GeoLocationDataDao geoLocationDataDao;

    private final RestClient restClient;

    public GeoLocationDataResource(Validator validator,
                                   GeoLocationDataCache geoLocationDataCache,
                                   GeoLocationDataDao geoLocationDataDao,
                                   RestClient restClient) {
        this.validator = validator;
        this.geoLocationDataCache = geoLocationDataCache;
        this.geoLocationDataDao = geoLocationDataDao;
        this.restClient = restClient;
    }

    Predicate<Date> isOldPredicate = createdDate ->   new Date().getTime() - createdDate.getTime()>= 5*60*1000;

    IpAddressValidatorPredicate ipAddressValidatorPredicate = new IpAddressValidatorPredicate();

    @GET
    @Path("/{ipAddress}")
    @UnitOfWork
    public Response getLocationDataByIpAddress(@PathParam("ipAddress") String ipAddress) throws ExecutionException {

        LOGGER.info("IpAddress in the request :"+ipAddress);

        if( !(ipAddressValidatorPredicate.test(ipAddress))){
            return Response.status(Response.Status.BAD_REQUEST).entity("Not a valid ip Address").build();
        }

        GeoLocationData result = null;
        result = geoLocationDataCache.getGeoLocationData(ipAddress);
            if (result == null || isOldPredicate.test(result.getCreated())) {
                result = restClient.getIpGeoLocationInformation(ipAddress);
                geoLocationDataDao.save(result);
            }
        return Response.ok(result).build();
    }



}

