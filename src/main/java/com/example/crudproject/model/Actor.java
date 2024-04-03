package com.example.crudproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    private Long id;
    private String name;
    private String surname;
    private String role;
    private int movie_id;
}