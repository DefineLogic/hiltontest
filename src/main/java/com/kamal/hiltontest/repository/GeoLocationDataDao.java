package com.kamal.hiltontest.repository;

import com.kamal.hiltontest.model.GeoLocationData;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class GeoLocationDataDao extends AbstractDAO<GeoLocationData> {

    /**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory.
     */
    public GeoLocationDataDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public GeoLocationData save(GeoLocationData geoLocationData){
        return persist(geoLocationData);
    }

    public GeoLocationData findByIp(String ipAddress){
        Query<GeoLocationData> query = (Query<GeoLocationData>)namedQuery("GeoLocationData.findByIp");
        query.setParameter("ipAddress",ipAddress);
        return uniqueResult(query);

    }

}