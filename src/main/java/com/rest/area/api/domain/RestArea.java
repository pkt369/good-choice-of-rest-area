package com.rest.area.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "rest_area")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rest_seq")
    private Long restAreaSeq;

    @Column(name = "rest_code")
    private String restCode;

    @Column(name = "rest_name")
    private String restName;

    @Column(name = "service_area_address")
    private String serviceAreaAddress;

    @Column(name = "route_code")
    private String routeCode;

    @Column(name = "route_name")
    private String routeName;
}
