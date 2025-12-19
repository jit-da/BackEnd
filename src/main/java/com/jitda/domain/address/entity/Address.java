package com.jitda.domain.address.entity;

import com.jitda.domain.users.entity.User;
import com.jitda.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "addresses")
public class Address extends BaseEntity {

    @Id
    @Column(name = "address_id", length = 13)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "recipient", nullable = false, length = 100)
    private String recipient;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "zipcode", nullable = false, length = 10)
    private String zipcode;

    @Column(name = "address1", nullable = false)
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Lob
    @Column(name = "entry_instructions")
    private String entryInstructions;

    @Column(name = "is_default")
    private boolean isDefault;

    @Builder
    private Address(String id, User user, String recipient, String phone, String zipcode, String address1, String address2, String entryInstructions, boolean isDefault) {
        this.id = id;
        this.user = user;
        this.recipient = recipient;
        this.phone = phone;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
        this.entryInstructions = entryInstructions;
        this.isDefault = isDefault;
    }
}
