package cat.feed.controller;

import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.OauthService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BaseController {

    private final JwtTokenProvider jwtTokenProvider;
    private final OauthService oauthService;
    private final UserService userService;
    @Value("${test.url}")
    private  String url;

    @GetMapping("/")
    public String main(Model model,@CookieValue(value="token", required=false) Cookie cookie){
        try{
            String token = cookie.getValue();
            String user = jwtTokenProvider.getUserPk(token);
            String nickName = userService.nickName(user);
            model.addAttribute("user",nickName+"님 환영합니다.");
            System.out.println(url);
            return "main";
        }catch (Exception e){
            model.addAttribute("user","게스트");
            return "main";
        }
    }

    @GetMapping("/login" )
    public String loginForm(@CookieValue(value="token", required=false) Cookie cookie){
        try{
            String token = cookie.getValue();
            return "logout";
        }catch (Exception e) {
            return "login";
        }
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @GetMapping("/kakao/join")
    public String kakaoJoin(){
        return "kakaoJoin";
    }



    @GetMapping("/logout/kakao")
    public String logoutKakao(HttpServletResponse response, @CookieValue(value="token", required=false) Cookie cookies){
        try {
            String coo = cookies.getValue();
            Cookie cookie = new Cookie("token", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            oauthService.kakaoLogout();
            return "redirect:http://"+url+":8080/";
        }catch (Exception e){
            return "redirect:http://"+url+":8080/";
        }
    }



}
