package com.studyolle.modules.account;

import com.studyolle.modules.tag.Tag;
import com.studyolle.modules.zone.Zone;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id") // 연관관계에서 상호참조하면서 stackoverflow가 발생하는걸 방지한다.
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account { // 계정

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified; // 이메일 인증

    private String emailCheckToken; // 이메일 인증 토큰

    private LocalDateTime emailCheckTokenGeneratedAt;

    private LocalDateTime joinedAt; // 가입 날짜

    private String bio; // 소개글

    private String url; // URL

    private String occupation; // 직업

    private String location; // 지역

    @Lob  // 텍스트 타입 매핑
    @Basic(fetch = FetchType.EAGER) // 즉시 로딩
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb = true;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    public void generateEmailCheckToken() { // 이메일 인증 토큰 생성
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1)); // 이메일을 전송한지 한시간이 지났는지 확인
    }

}
