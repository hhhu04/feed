package cat.feed.controller;

import cat.feed.dto.MailDto;
import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.OauthService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class BaseController {

    private final JwtTokenProvider jwtTokenProvider;
    private final OauthService oauthService;
    private final UserService userService;
    @Value("${test.url}")
    private  String url;

    private String paths = "/img/";

    @GetMapping("/")
    public String main(Model model,@CookieValue(value="token", required=false) Cookie cookie){
        try{
            String token = cookie.getValue();
            String user = jwtTokenProvider.getUserPk(token);
            String nickName = userService.nickName(user);
            model.addAttribute("user",nickName+"님 환영합니다.");
            return "main";
        }catch (Exception e){
            model.addAttribute("user","게스트");
            return "main";
        }
    }

    @GetMapping("/user/myPage")
    public String myPage(Model model, @CookieValue(value="token", required=false) Cookie cookie, User user){
        try{
            String token = cookie.getValue();
            String userId = jwtTokenProvider.getUserPk(token);
            if(userId == null) throw new Exception();
            model.addAttribute("user");
            user.setUserId(userId);
            user = userService.userInfo(user);
            if(!user.getPassword().equals("111111")) user.setPassword(null);
            model.addAttribute("info",user);
            return "myPage";
        }catch (Exception e){
            return "main";
        }
    }

    @GetMapping("/user/update")
    public String userUpdate(@CookieValue(value="token", required=false) Cookie cookie,Model model,User user){
        try{
            String token = cookie.getValue();
            String userId = jwtTokenProvider.getUserPk(token);
            if(userId == null) throw new Exception();
            user.setUserId(userId);
            user = userService.userInfo(user);
            user.setPassword(null);
            model.addAttribute("user",user);
            return "userUpdate";
        } catch (Exception e) {
            e.printStackTrace();
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

    @GetMapping("/{socialLoginType}/join")
    public String socialJoin(@PathVariable(name = "socialLoginType") String socialLoginType){
        if(socialLoginType.equals("naver")) return "naverJoin";
        else return "kakaoJoin";
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

    @GetMapping("/myBuyLogs")
    public String myBuyLogs(@CookieValue(value="token", required=false) Cookie cookies){

        try {
            String token = cookies.getValue();


        }catch (Exception e){
            e.printStackTrace();
        }

        return "/myLogs";
    }





    //////파일다운


    @GetMapping("/down/dogram")
    public ResponseEntity<Object> download(){
        String path = paths+"dogram-master.zip";

        try{
            Path fileFath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(fileFath));

            File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());
            return new ResponseEntity<Object>(resource,headers, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<Object>(null,HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/down/dogram2")
    public ResponseEntity<Object> download2(){
        String path = paths+"dogram2-0.0.1-SNAPSHOT.jar";

        try{
            Path fileFath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(fileFath));

            File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());
            return new ResponseEntity<Object>(resource,headers, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<Object>(null,HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/down/park")
    public ResponseEntity<Object> download3(){
        String path = paths+"study-master.zip";

        try{
            Path fileFath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(fileFath));

            File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());
            return new ResponseEntity<Object>(resource,headers, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<Object>(null,HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/down/newPark")
    public ResponseEntity<Object> download4(){
        String path = paths+"newPark-0.0.1-SNAPSHOT.jar";

        try{
            Path fileFath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(fileFath));

            File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());
            return new ResponseEntity<Object>(resource,headers, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<Object>(null,HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/mail")
    public String mail(){
        return "mail";
    }

    @PostMapping("/mail")
    @ResponseBody
    public int sendMail(@RequestBody MailDto email){
        try {
            User user = new User();
            user.setUserId(email.getAddress());
            if(userService.checkUser(user)) {
                user = userService.userInfo(user);
                user.setPassword("111111");
                userService.tempPassword(user);
                System.out.println(userService.userInfo(user));


//                userService.mailSend(email);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    @GetMapping("/rePass")
    public String rePass(){
        return "rePass";
    }







}
