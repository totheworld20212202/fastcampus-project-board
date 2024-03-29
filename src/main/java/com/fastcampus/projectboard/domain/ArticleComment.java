package com.fastcampus.projectboard.domain;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
}
)
//@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)          // optional = false : 필수값, cascade값은 기본값이 none.
    private Article article;
//    @Setter @ManyToOne(optional = false) private UserAccount userAccount; // 유저 정보 (ID)

    @Setter @ManyToOne(optional = false) @JoinColumn(name = "userId") private UserAccount userAccount; // 유저 정보 (ID)
    @Setter
    @Column(nullable = false, length = 500)
    private String content;

//    @CreatedDate
//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//    @CreatedBy
//    @Column(nullable = false, length = 100)
//    private String createdBy;
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime modifiedAt;
//    @LastModifiedBy
//    @Column(nullable = false, length = 100)
//    private String modifiedBy;

    protected ArticleComment() {}

    private ArticleComment(Article article, UserAccount userAccount, String content) {
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content) {
        return new ArticleComment(article, userAccount, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment)) return false;
        ArticleComment that = (ArticleComment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
