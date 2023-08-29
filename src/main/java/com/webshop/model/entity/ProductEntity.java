package com.webshop.model.entity;

import com.webshop.model.enums.Categories;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//todo Serializable review

/**
 * Entity class representing items sold on the website.
 */
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "products")
@Entity
public class ProductEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 2000)
    private String description;
    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private ManufacturerEntity manufacturer;
    @Column(nullable = false)
    private String category;
    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.ALL)
    private ProductInventoryEntity inventory;
    /**
     * Image URL of the product.
     */
    private String image;
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Constructor to initialize a product with details.
     *
     * @param name           The name of the product.
     * @param description    The description of the product.
     * @param manufacturer   The manufacturer of the product.
     * @param category       The category of the product.
     * @param productInventory The inventory information of the product.
     * @param image          The image URL of the product.
     */
    public ProductEntity(String name, String description, ManufacturerEntity manufacturer, String category, ProductInventoryEntity productInventory, String image) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.category = category;
        this.inventory = productInventory;
        this.image = image;
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ManufacturerEntity getManufacturer() {
        return manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public ProductInventoryEntity getInventory() {
        return inventory;
    }

    public String getImage() {
        return image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(ManufacturerEntity manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setInventory(ProductInventoryEntity inventory) {
        this.inventory = inventory;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
