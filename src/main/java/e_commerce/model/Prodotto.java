package e_commerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String category;

    private List<String> tags;

    private String description;

    @Lob
    @Column(name = "image", columnDefinition="MEDIUMTEXT")
    private String image;

    private double price;
}
