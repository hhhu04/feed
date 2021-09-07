package cat.feed.controller;

import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.OauthService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OauthService oauthService;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${test.url}")
    private  String url;

    // 1:성공 -1:아디중복 -2:아디없음 -3그 밖의 에러

    @PostMapping("/user/join")
    @ResponseBody
    public int join(@RequestBody User user){

        if(!userService.checkUser(user)) {
            try {
                userService.userJoin(user);
                return 1;
            }catch (Exception e){
                return -2;
            }
        }
        else return -1;
    }

    @PostMapping("/user/join/kakao")
    @ResponseBody
    public int kakaoJoin(@RequestBody User user){

        if(!userService.checkUser(user)) {
            try {
                userService.kakaoJoin(user);
                return 1;
            }catch (Exception e){
                return -2;
            }
        }
        else return -1;
    }

    @PostMapping("/user/login")
    @ResponseBody
    public int login(@RequestBody User user, HttpServletResponse response){
        if(userService.checkUser(user)) {
            try{
                String token = userService.createToken(user);
                System.out.println(token);
                Cookie cookie = new Cookie("token",token);
                cookie.setPath("/");
                cookie.setMaxAge(30*60);
                response.addCookie(cookie);
                return 1;
            }catch (IllegalArgumentException e){
                return -1;
            }
            catch (Exception e)
            {
                return -3;
            }
        }
        return -2;

    }

    @PostMapping("/user/logout")
    @ResponseBody
    public int logout(HttpServletResponse response){
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return 1;
    }

    @GetMapping("/out/kakao")
    public void logoutKakao(){
        oauthService.kakaoLogout();
    }

    @GetMapping(value = "/{socialLoginType}/login")
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") String socialLoginType) {
        oauthService.request(socialLoginType,url);
    }

    @GetMapping(value = "/{socialLoginType}/callback")
    public String callback(
            @PathVariable(name = "socialLoginType") String socialLoginType,
            @RequestParam(name = "code") String code, HttpServletResponse response,HttpServletRequest request) {
        String email = oauthService.requestAccessToken(socialLoginType, code,url);

        if (userService.check(email, socialLoginType)) {
            String token = userService.login(email, socialLoginType);
            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
            return "<script>alert('로그인');  window.location = 'http://"+url+":8080/'</script>";
        } else {
            String emails = "hhhu04@gmail.com";
            Cookie cookie = new Cookie("email",emails);
            cookie.setPath("/");
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
            return "<script>alert('가입진행. '); window.location = 'http://"+url+":8080/kakao/join'</script>";
        }
    }

    @PostMapping("/user/info")
    @ResponseBody
    public User info(@CookieValue(value="token", required=false) Cookie cookie, User user){
        try {
            String userId = jwtTokenProvider.getUserPk(cookie.getValue());
            if (userId == null) throw new Exception();
            user.setUserId(userId);
            user = userService.userInfo(user);
            user.setPassword(null);
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return user;
        }
    }

    @PostMapping("/user/update/save")
    @ResponseBody
    public int updateSve(@RequestBody User user,@CookieValue(value="token", required=false) Cookie cookie){
        try{
            String userId = jwtTokenProvider.getUserPk(cookie.getValue());
            if (userId == null) throw new Exception();
            User user2 = new User();
            user2.setUserId(userId);
            user2 = userService.userInfo(user2);
            user2.setPassword(user.getPassword());
            user2.setNickName(user.getNickName());
            user2.setUpdatedAt(LocalDateTime.now());
            userService.update(user2);
        }catch (Exception e){

        }
        return 1;
    }



    ///admin



}
