package com.enoca.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    @Column(name = "totalPrice")
    private double totalPrice;

    @Column(name = "isPlaced")
    private boolean isPlaced;

    @Column(name = "date")
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "order")
    private List<OrderDetails> orderDetailsList;
}
