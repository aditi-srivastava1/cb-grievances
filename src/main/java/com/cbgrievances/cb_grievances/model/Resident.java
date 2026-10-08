package com.cbgrievances.cb_grievances.model;

import jakarta.persistence.*;

@Entity
@Table(name = "residents")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String jnuId;

    private String name;
    private String password;
    private String wing;       // "Boys" or "Girls"
    private String roomNumber;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJnuId() { return jnuId; }
    public void setJnuId(String jnuId) { this.jnuId = jnuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getWing() { return wing; }
    public void setWing(String wing) { this.wing = wing; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}