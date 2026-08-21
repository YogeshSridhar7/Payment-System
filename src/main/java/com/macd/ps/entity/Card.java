package com.macd.ps.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "CARD_TABLE")
@SequenceGenerator(name = "CARD_SEQ", allocationSize = 1)
public class Card {
    @Id
    @Column(name = "ID")
    @GeneratedValue(
            generator = "CARD_SEQ",
            strategy = GenerationType.AUTO
    )
    private long id;
    private String cardHolderName;
    private long cardNumber;
    private String expiryDate;
}

