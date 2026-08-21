package com.macd.ps.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@SequenceGenerator(name = "PAYMENT_SEQ", allocationSize = 1)
@Table(name = "PAYMENT_TABLE")
public class Payment {
    @Id
    @Column(name = "ID")
    @GeneratedValue(
            generator = "PAYMENT_SEQ",
            strategy = GenerationType.AUTO
    )
    private long id;
    private long orderNumber;
    private LocalDateTime paymentDate;
    private BigDecimal totalAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CARD_ID", referencedColumnName = "ID")
    private Card card;
}
