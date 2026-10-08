package com.cbgrievances.cb_grievances.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "complaints")
public class Complaint {
    public String getPhotoFileName() { return photoFileName; }
    public void setPhotoFileName(String photoFileName) { this.photoFileName = photoFileName; }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;      // Water, Electrical, Mess, Furniture, etc.
    private String description;
    private String wing;          // "Boys" or "Girls"
    private LocalDate dateFiled;
    private String status;        // "In Process" or "Completed"

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;    // links back to who filed it

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    private String photoFileName;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWing() { return wing; }
    public void setWing(String wing) { this.wing = wing; }

    public LocalDate getDateFiled() { return dateFiled; }
    public void setDateFiled(LocalDate dateFiled) { this.dateFiled = dateFiled; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
}