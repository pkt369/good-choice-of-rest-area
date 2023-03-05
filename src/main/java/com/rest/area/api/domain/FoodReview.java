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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FoodReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_seq")
    private Long reviewSeq;

    @Column(name = "food_seq")
    @ManyToOne(targetEntity = Food.class, fetch = FetchType.LAZY)
    private String foodSeq;

    private String rating;

    @Column(name = "food_comment")
    private String foodComment;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "created_datetime")
    private String createdDatetime;
}
