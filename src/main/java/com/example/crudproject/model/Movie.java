package com.example.crudproject.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
   private Long id;
   private String title;
   private String description;
   private Integer release;
   private Double rating;
   private String genre;
   private List<Actor> actors;
}
