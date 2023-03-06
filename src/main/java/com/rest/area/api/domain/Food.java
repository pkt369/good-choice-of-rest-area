package com.rest.area.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_seq")
    private Long foodSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_seq")
    private RestArea restSeq;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "food_cost")
    private int foodCost;

    @Column(name = "detail_description")
    private String detailDescription;

    @Column(name = "food_material")
    private String foodMaterial;

    @Column(name = "created_id")
    private String createdId;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;
}
