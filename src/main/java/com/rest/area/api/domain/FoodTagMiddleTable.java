package com.rest.area.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodTagMiddleTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_tag_seq")
    private Long foodTagSeq;

    @Column(name = "food_seq")
    @ManyToOne(targetEntity = Food.class, fetch = FetchType.LAZY)
    private Long foodSeq;

    @Column(name = "tag_seq")
    @ManyToOne(targetEntity = Tag.class, fetch = FetchType.LAZY)
    private Long tagSeq;
}
