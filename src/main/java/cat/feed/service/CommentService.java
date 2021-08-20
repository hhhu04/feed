package cat.feed.service;

import cat.feed.entity.Comment;
import cat.feed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;


    public void newComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> allComment(List<Comment> list) {
        list = commentRepository.findAll();
        return list;
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment find(long id) {
        return commentRepository.findCommentById(id);
    }
}
