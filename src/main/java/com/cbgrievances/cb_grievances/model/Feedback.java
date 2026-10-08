package com.cbgrievances.cb_grievances.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int starRating;       // 1 to 5
    private String comment;       // resident's remark on how the work was done

    @OneToOne
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;  // links to the complaint this feedback is about

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getStarRating() { return starRating; }
    public void setStarRating(int starRating) { this.starRating = starRating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
}