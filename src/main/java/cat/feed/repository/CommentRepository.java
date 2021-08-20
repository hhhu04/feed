package cat.feed.repository;

import cat.feed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Comment findCommentById(Long id);
}
