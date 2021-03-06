package cat.feed.controller;

import cat.feed.code.Res;
import cat.feed.code.ResponseMessage;
import cat.feed.code.StatusCode;
import cat.feed.entity.Comment;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.CommentService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/comment/new")
    public ResponseEntity newComment(@RequestBody Comment comment,@RequestHeader HttpHeaders headers){
        try{
            String token = headers.get("authorization").get(0);
            String email = jwtTokenProvider.getUserPk(token);
            comment.setUserId(userService.id(email));
            comment.setNickName(userService.nickName(email));
            comment=comment.newComment(comment);
            commentService.newComment(comment);
            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.SAVE_SUCCESS,comment), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.SAVE_FAIL), HttpStatus.OK);
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
            }else return "<script>alert('????????? ????????? ???????????????!.'); history.back();</script>";
        }catch (Exception e){
            return "<script>alert('????????? ???????????????!.'); history.back();</script>";
        }
    }


}
