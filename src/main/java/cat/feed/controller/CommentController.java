package cat.feed.controller;

import cat.feed.entity.Comment;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.CommentService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/comment/new")
    @ResponseBody
    public int newComment(@RequestBody Comment comment){
        try{
            System.out.println(comment);
            comment=comment.newComment(comment);
            commentService.newComment(comment);
            return 1;
        }catch (Exception e){
            return -1;
        }
    }

    @PostMapping("/all/comment")
    @ResponseBody
    public List<Comment> allComment(){
        List<Comment> list = new ArrayList<>();
        try{
            list = commentService.allComment(list);
            return list;
        }catch (Exception e) {
            return list;
        }
    }

    @PostMapping("/comment/delete/{num}")
    public String delete(@PathVariable(name = "num") long id,@CookieValue(value="token", required=false) Cookie cookie){
        try {
            long userId = userService.id(jwtTokenProvider.getUserPk(cookie.getValue()));
            Comment comment = commentService.find(id);
            if(comment.getUserId() == userId) {
                commentService.delete(comment);
                return "<script>history.back()</script>";
            }else return "<script>alert('로그인 정보를 확인하세요!.'); history.back();</script>";
        }catch (Exception e){
            return "<script>alert('잘못된 요청입니다!.'); history.back();</script>";
        }
    }


}
