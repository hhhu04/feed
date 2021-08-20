package cat.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    private String body;

    private String img;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String nickName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private List<Comment> comments = new ArrayList<>();

    public Feed newFeed(Feed feed, long  id){
        feed.setCreatedAt(LocalDateTime.now());
        feed.setUserId(id);
        return feed;
    }

}
