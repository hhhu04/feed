package cat.feed.controller;

import cat.feed.entity.User;
import cat.feed.service.OauthService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OauthService oauthService;
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
        System.out.println(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login"+socialLoginType);
        oauthService.request(socialLoginType,url);
    }

    @GetMapping(value = "/{socialLoginType}/callback")
    public String callback(
            @PathVariable(name = "socialLoginType") String socialLoginType,
            @RequestParam(name = "code") String code, HttpServletResponse response,HttpServletRequest request) {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :: {}" + code);
        String email = oauthService.requestAccessToken(socialLoginType, code,url);

        if (userService.check(email, socialLoginType)) {
            String token = userService.login(email, socialLoginType);
            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
//            return "<script>alert('로그인');  window.location = 'http://localhost:8080/'</script>";
//            return "<script>alert('로그인');  window.location = 'http://118.67.133.148:8080/'</script>";
            return "<script>alert('로그인');  window.location = 'http://"+url+":8080/'</script>";
        } else {
            Cookie cookie = new Cookie("email",email);
            cookie.setPath("/");
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
//            return "<script>alert('가입진행. '); window.location = 'http://localhost:8080/kakao/join'</script>";
//            return "<script>alert('가입진행. '); window.location = 'http://118.67.133.148:8080/kakao/join'</script>";
            return "<script>alert('가입진행. '); window.location = 'http://"+url+":8080/kakao/join'</script>";
        }
    }

}
