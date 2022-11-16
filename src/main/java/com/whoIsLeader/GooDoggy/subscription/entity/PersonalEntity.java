package com.whoIsLeader.GooDoggy.subscription.entity;

import com.whoIsLeader.GooDoggy.user.entity.UserEntity;
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
@Table(name = "personal")
@NoArgsConstructor
@DynamicInsert
public class PersonalEntity extends BaseEntity {

    //pSubscriptionIdx, userIdx, serviceName, planName, price, firstDayOfPayment, paymentCycle, category, account, status
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pSubscriptionIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private UserEntity userIdx;

    @Column(nullable = false, length = 20)
    private String serviceName;

    @Column(nullable = false, length = 20)
    private String planName;

    @Column(nullable = false)
    private Long price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate firstDayOfPayment;

    @Column(nullable = false)
    private Long paymentCycle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 30)
    private String account;

    @Column(columnDefinition = "varchar(10) default 'active'")
    private String status;

    @Builder
    public PersonalEntity(UserEntity userEntity, String serviceName, String planName,
                          Long price, LocalDate firstDayOfPayment, Long paymentCycle, Category category, String account, String status){
        this.userIdx = userEntity;
        this.serviceName = serviceName;
        this.planName = planName;
        this.price = price;
        this.firstDayOfPayment = firstDayOfPayment;
        this.paymentCycle = paymentCycle;
        this.category = category;
        this.account = account;
        this.status = status;
    }
}