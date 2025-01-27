package com.coupon.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "review_like")
public class ReviewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @Enumerated(EnumType.STRING) // Store the enum as a string in the database
    @Column(name = "action", nullable = false)
    private ReviewAction action;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {this.id = id;}

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ReviewEntity getReview() {
        return review;
    }

    public void setReview(ReviewEntity review) {
        this.review = review;
    }

    public ReviewAction getAction() {return action;}

    public void setAction(ReviewAction action) {this.action = action;}


}
