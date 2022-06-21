package com.kamal.hiltontest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NamedQueries({
        @NamedQuery(name = "GeoLocationData.findByIp",
                query = "select g from GeoLocationData g "
                        + "where g.query = :ipAddress")
})
public class GeoLocationData {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String status;
    String country;
    String countryCode;
    String region;
    String regionName;
    String city;
    String zip;
    String lat;
    String lon;
    String timezone;
    String isp;
    String org;
    @Column(name = "as_number")
    String as;
    @Column(unique = true)
    String query;
    @JsonIgnore
    private Date created;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }


}
