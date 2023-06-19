package com.example.tp_resto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * id, orderItems, ordertime
 */
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "facture_id"))
@Entity
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // MappedBy: Specifie le nom du champ de l'autre coté
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "commande",
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    //@JsonBackReference
    //@JoinColumn(name = "commande_id") quand on utilise mappedBy, on ne met pas cette annotation
    private List<CommandeItem> orderItems = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "facture_id")
    private Facture facture;

    public Facture getFacture() {
        return facture;
    }
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime orderTime;

    public Commande() {
        orderTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public Commande(LocalDateTime orderTime) {
        this.orderTime = orderTime.truncatedTo(ChronoUnit.SECONDS);
    }


    public void removeFacture() {
        facture.setCommande(null);
        this.facture = null;
    }
    public void setFactureBidirection(Facture facture) {
        this.facture = facture;
        facture.setCommande(this);
    }



    @Transactional
    public void addItem(CommandeItem item) {
        if(orderItems == null) {
            orderItems = new ArrayList<>();
        }

        if(this.hasItem(item.getMenuItem())) {
            this.incrementItem(item, item.getQuantity());
        } else {
            this.orderItems.add(item);
            item.setCommande(this);

        }
    }
    public boolean hasItem(MenuItem item) {
        return this.orderItems.stream()
                .anyMatch(i -> i.getMenuItem().equals(item));
    }

    public void incrementItem(CommandeItem item, int quantity) {
        this.orderItems.stream()
                .filter(i -> i.getMenuItem().equals(item.getMenuItem()))
                .findFirst()
                .ifPresent(i -> i.setQuantity(i.getQuantity() + quantity));
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CommandeItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CommandeItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", orderItems=" + orderItems +
                ", orderTime=" + orderTime +
                '}';
    }
}