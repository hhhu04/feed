package cat.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long feedId;

    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String nickName;


    public Comment newComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }

    public Comment updateComment(Comment comment) {
        comment.setUpdatedAt(LocalDateTime.now());
        return comment;
    }

}
