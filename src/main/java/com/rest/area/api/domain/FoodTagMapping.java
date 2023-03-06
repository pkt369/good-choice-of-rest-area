package com.rest.area.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "food_tag_mapping")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodTagMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_tag_seq")
    private Long foodTagSeq;

    @JoinColumn(name = "food_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private Food foodSeq;

    @JoinColumn(name = "tag_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tagSeq;
}
