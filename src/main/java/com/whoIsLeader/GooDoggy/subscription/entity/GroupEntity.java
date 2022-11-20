package com.whoIsLeader.GooDoggy.subscription.entity;

import com.whoIsLeader.GooDoggy.util.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "groupSub")
@NoArgsConstructor
@DynamicInsert
public class GroupEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupIdx;

    @Column(nullable = false, length = 20)
    private String serviceName;

    @Column(nullable = false, length = 20)
    private String planName;

    @Column(nullable = false)
    private Long price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate firstDayOfPayment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate lastDayOfPayment;

    @Column(nullable = false)
    private Long paymentCycle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 30)
    private String account;

    @Column(nullable = false)
    private Long targetNum;

    @Column(nullable = false, length = 100)
    private String contents;

    @Column(columnDefinition = "varchar(12) default 'half-active'")
    private String status;

    @Builder
    public GroupEntity(String serviceName, String planName, Long price, LocalDate firstDayOfPayment, LocalDate lastDayOfPayment,
                        Long paymentCycle, Category category, String account, Long targetNum, String contents, String status){
        this.serviceName = serviceName;
        this.planName = planName;
        this.price = price;
        this.firstDayOfPayment = firstDayOfPayment;
        this.lastDayOfPayment = lastDayOfPayment;
        this.paymentCycle = paymentCycle;
        this.category = category;
        this.account = account;
        this.targetNum = targetNum;
        this.contents = contents;
        this.status = status;
    }

    public void changeStatus(String newStatus){
        this.status = newStatus;
    }
}