package com.example.microstone.domain;

import com.example.microstone.domain.Enum.ManagerTransferRequestStatus;
import jakarta.persistence.*;
import lombok.*;

// 관리자이양_민지
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManagerTransferRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroup group;

    @ManyToOne
    @JoinColumn(name = "current_manager_id", nullable = false)
    private User currentManager;

    @ManyToOne
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ManagerTransferRequestStatus status;
}