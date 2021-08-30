package cat.feed.controller;

import cat.feed.dto.AllDto;
import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String main(@CookieValue(value="token", required=false) Cookie cookies){
        try {
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

    @PostMapping("/create")
    @ResponseBody
    public User create(@RequestBody User user){
        user=user.userJoin(user,passwordEncoder);
        user.setRoles("ROLE_ADMIN");
        userService.adminCreate(user);
        return user;
    }


}
