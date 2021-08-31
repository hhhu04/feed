package cat.feed.controller;

import cat.feed.dto.AllDto;
import cat.feed.entity.Comment;
import cat.feed.entity.Feed;
import cat.feed.entity.User;
import cat.feed.exception.ForbiddenException;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.CommentService;
import cat.feed.service.FeedService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final FeedService feedService;
    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String main(@CookieValue(value="token", required=false) Cookie cookies){
        try {
            String token = jwtTokenProvider.getUserPk(cookies.getValue());
            String role = userService.getRole(userService.key(token));
            if(role.equals("ROLE_USER")) throw new ForbiddenException("관리자 페이지입니다.");
            return "admin/admin";
        }catch (Exception e){
            return "login";
        }
    }

    @PostMapping("/api/all")
    @ResponseBody
    public AllDto all(){
        AllDto dto = new AllDto();
        dto = userService.allUser(dto);
        dto.setFeedCount(feedService.count());
        return dto;
    }

    @GetMapping("/new")
    public String create(@CookieValue(value="token", required=false) Cookie cookies){
        try {
            String token = jwtTokenProvider.getUserPk(cookies.getValue());
            String role = userService.getRole(userService.key(token));
            if (role.equals("ROLE_USER")) throw new ForbiddenException("마스터 페이지입니다.");
            if (role.equals("ROLE_ADMIN")) throw new ForbiddenException("마스터 페이지입니다.");
            return "admin/create";
        }catch (Exception e){
            e.printStackTrace();
            return "admin/admin";
        }
    }

    @PostMapping("/master/create")
    @ResponseBody
    public int create(@RequestBody User user){
        try {
            user = user.userJoin(user, passwordEncoder);
            user.setRoles("ROLE_ADMIN");
//        user.setRoles("ROLE_MASTER");
            userService.adminCreate(user);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @GetMapping("/del")
    public String del(@CookieValue(value="token", required=false) Cookie cookies){
        try {
            String token = jwtTokenProvider.getUserPk(cookies.getValue());
            String role = userService.getRole(userService.key(token));
            if (role.equals("ROLE_USER")) throw new ForbiddenException("마스터 페이지입니다.");
            if (role.equals("ROLE_ADMIN")) throw new ForbiddenException("마스터 페이지입니다.");
            return "admin/del";
        }catch (Exception e){
            e.printStackTrace();
            return "admin/admin";
        }
    }

    @PostMapping("/master/del")
    @ResponseBody
    public int delAdmin(@RequestBody User user){
        try{
            userService.delete(user);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    @PostMapping("/api/feed/delete")
    @ResponseBody
    public int feedDelete(@RequestBody Feed feed){
        try{
            feedService.delete(feed);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @PostMapping("/api/comment/delete/{num}")
    @ResponseBody
    public int commentDelete(@RequestBody Comment comment,@PathVariable(name = "num") long id){
        try{
            comment.setId(id);
            commentService.delete(comment);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }



}
