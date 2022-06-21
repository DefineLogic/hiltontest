package com.kamal.hiltontest;


import com.kamal.hiltontest.cache.GeoLocationDataCache;
import com.kamal.hiltontest.configuration.BasicConfiguration;
import com.kamal.hiltontest.controller.GeoLocationDataResource;
import com.kamal.hiltontest.healthcheck.AppHealthCheck;
import com.kamal.hiltontest.healthcheck.HealthCheckController;
import com.kamal.hiltontest.model.GeoLocationData;
import com.kamal.hiltontest.repository.GeoLocationDataDao;
import com.kamal.hiltontest.service.RestClient;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

public class MyApplication extends Application<BasicConfiguration> {

    private final HibernateBundle<BasicConfiguration> hibernateBundle = new HibernateBundle<BasicConfiguration>(GeoLocationData.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(BasicConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new MyApplication().run("server", "config.yml");
    }

    @Override
    public void run(final BasicConfiguration basicConfiguration, final Environment environment) {
        final Client client = new JerseyClientBuilder(environment).build("DemoRESTClient");
        final GeoLocationDataDao geoLocationDataDao
                = new GeoLocationDataDao(hibernateBundle.getSessionFactory());
        final GeoLocationDataResource ipRestResource = new GeoLocationDataResource(environment.getValidator(),
                new GeoLocationDataCache(geoLocationDataDao),geoLocationDataDao,new RestClient(client));

        environment.jersey().register(ipRestResource);

        // Application health check
        environment.healthChecks().register("APIHealthCheck", new AppHealthCheck(client));

        // Run multiple health checks
        environment.jersey().register(new HealthCheckController(environment.healthChecks()));
    }

    @Override
    public void initialize(final Bootstrap<BasicConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
        bootstrap.addBundle(hibernateBundle);
        super.initialize(bootstrap);
    }



//    private List<Brand> initBrands() {
//        final List<Brand> brands = new ArrayList<>();
//        brands.add(new Brand(1L, "Brand1"));
//        brands.add(new Brand(2L, "Brand2"));
//        brands.add(new Brand(3L, "Brand3"));
//
//        return brands;
//    }
}
