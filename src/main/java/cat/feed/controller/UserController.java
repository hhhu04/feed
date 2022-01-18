package cat.feed.controller;

import cat.feed.code.Res;
import cat.feed.code.ResponseMessage;
import cat.feed.code.StatusCode;
import cat.feed.entity.BuyLogs;
import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.OauthService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final OauthService oauthService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${test.url}")
    private  String url;

    @Value("localhost")
    private String vue;


    private String paths = "/img/";


    // 1:성공 -1:아디중복 -2:아디없음 -3그 밖의 에러  0 : 형식 틀림




    @PostMapping("/user/join")
    @ResponseBody
    public ResponseEntity join(@RequestBody User user){

        if(!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) return new ResponseEntity(Res.res(StatusCode.OK,
                ResponseMessage.CREATED_FAIL), HttpStatus.BAD_REQUEST);
        if(user.getPassword().length() < 6) return new ResponseEntity(Res.res(StatusCode.OK,
                ResponseMessage.CREATED_FAIL), HttpStatus.BAD_REQUEST);

        if(!userService.checkUser(user)) {
            try {
                userService.userJoin(user);
                return new ResponseEntity(Res.res(StatusCode.OK,
                        ResponseMessage.CREATED_USER), HttpStatus.OK);
            }catch (Exception e){
                e.printStackTrace();
                return new ResponseEntity(Res.res(StatusCode.INTERNAL_SERVER_ERROR,
                        ResponseMessage.CREATED_FAIL), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else return new ResponseEntity(Res.res(StatusCode.OK,
                ResponseMessage.CREATED_FAIL), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/user/join/{socialLoginType}")
    public int socialJoin(@RequestBody User user,@PathVariable(name = "socialLoginType") String socialLoginType){

        if(!userService.checkUser(user)) {
            try {
                if(socialLoginType.equals("kakao")) userService.kakaoJoin(user);
                else if(socialLoginType.equals("naver")) userService.naverJoin(user);
                else return -2;

                return 1;
            }catch (Exception e){
                return -2;
            }
        }
        else return -1;
    }

    @PostMapping("/user/login")
    public ResponseEntity login(@RequestBody User user, HttpServletResponse response){

        if(userService.checkUser(user)) {
            try{
                String token = userService.createToken(user);
                Cookie cookie = new Cookie("token",token);
                cookie.setPath("/");
                cookie.setMaxAge(30*60);
                response.addCookie(cookie);
                return new ResponseEntity(Res.res(StatusCode.OK,
                        ResponseMessage.LOGIN_SUCCESS,token), HttpStatus.OK);
            }catch (IllegalArgumentException e){
                return new ResponseEntity(Res.res(StatusCode.BAD_REQUEST,
                        ResponseMessage.LOGIN_FAIL), HttpStatus.OK);
            }
            catch (Exception e)
            {
                return new ResponseEntity(Res.res(StatusCode.OK,
                        ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity(Res.res(StatusCode.BAD_REQUEST,
                ResponseMessage.LOGIN_FAIL), HttpStatus.OK);
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
    public void logoutKakao(HttpServletResponse response){
        oauthService.kakaoLogout(url);
    }

    @GetMapping("/out/naver")
    public String logoutnaver(@CookieValue(value="token", required=false) Cookie cookie,HttpServletResponse response){
        String token = cookie.getValue();
        String result = oauthService.naverLogout(token);
        if(result.equals("no")){
            return "logout";
        }
        else {
            Cookie cookies = new Cookie("token", "123");
            cookies.setPath("/");
            cookies.setMaxAge(0);
            response.addCookie(cookies);
            return "<script>window.location = '/'</script>";
        }
    }

    @GetMapping(value = "/{socialLoginType}/login")
    public String socialLoginType(
            @PathVariable(name = "socialLoginType") String socialLoginType,@RequestHeader HttpHeaders headers) {
        System.out.println(socialLoginType);
            return oauthService.request(socialLoginType,url);
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
            return "<script> window.location = 'http://"+vue+":8000/social?token="+token+"'  </script>";
//            return "<script> window.opener.result("+token+");  </script>";
        } else {
            Cookie cookie = new Cookie("email",email);
            cookie.setPath("/");
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
            return "<script>alert('가입진행. '); window.location = 'http://"+vue+":8000/join/"+socialLoginType+"'</script>";
        }
    }

    @GetMapping("/user/info")
    public ResponseEntity info(@RequestHeader HttpHeaders headers, User user){
        try {
            String userId = jwtTokenProvider.getUserPk(Objects.requireNonNull(headers.get("authorization")).get(0));
            if (userId == null) throw new Exception();
            user.setEmail(userId);
            user = userService.userInfo(user);

            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.READ_USER,user), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.SERVICE_UNAVAILABLE,
                    ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/user/update")
    public int updateSve(@RequestBody User user,@CookieValue(value="token", required=false) Cookie cookie){
        try{
            System.out.println("put");
//            String userId = jwtTokenProvider.getUserPk(cookie.getValue());
//            if (userId == null) throw new Exception();
//            User user2 = new User();
//            user2.setEmail(userId);
//            user2 = userService.userInfo(user2);
//            user2.setPassword(user.getPassword());
//            user2.setNickName(user.getNickName());
//            user2.setUpdatedAt(LocalDateTime.now());
//            userService.update(user2);
        }catch (Exception e){

        }
        return 1;
    }



    @PostMapping("/user/myLogs")
    public List<BuyLogs> buyLogs(@CookieValue(value="token", required=false) Cookie cookie,User user){
        try {
            String token = cookie.getValue();
            String userId = jwtTokenProvider.getUserPk(token);
            user.setEmail(userId);
            user = userService.userInfo(user);


        }catch (Exception e){
            e.printStackTrace();
        }

        return user.getBuyLogs();
    }


    @PostMapping("user/rePass")
    @ResponseBody
    public int rePass(@CookieValue(value="token", required=false) Cookie cookie,@RequestBody User user){
        try{
            System.out.println(user.getPassword());
            String token = cookie.getValue();
            String userId = jwtTokenProvider.getUserPk(token);
            User user2 = new User();
            user2.setEmail(userId);
            user2 = userService.userInfo(user2);
            userService.rePass(user,user2);

        }catch (Exception e){
            e.printStackTrace();
        }


        return 1;
    }


}
