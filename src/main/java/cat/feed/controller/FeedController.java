package cat.feed.controller;

import cat.feed.entity.Feed;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.FeedService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @Value("${test.url}")
    private  String url;
    @Value("${test.path}")
    private String path;

    @GetMapping("favicon.ico") @ResponseBody public void returnNoFavicon() { }


    @GetMapping("/feed")
    public String feed(Model model, @CookieValue(value="token", required=false) Cookie cookie,Pageable pageable){
        try{
            String token = cookie.getValue();
            String user = jwtTokenProvider.getUserPk(token);
            String nickName = userService.nickName(user);
            }catch (Exception e){
            model.addAttribute("user","게스트");
        }
        return "feed";
    }

    @GetMapping("/feed/new")
    public String newFeed(@CookieValue(value="token", required=false) Cookie cookies){
        try {
            String id = cookies.getValue();
            return "newFeed";
        }catch (Exception e){
            return "login";
        }
    }

    @GetMapping("/feed/{title}/{id}")
    public String feedDetail(@CookieValue(value="token", required=false) Cookie cookies,
                             @PathVariable(name = "title") String title,Model model,
                             @PathVariable(name = "id") Long ids ){
        try {
            String id = cookies.getValue();
            Feed feed = new Feed();
            feed = feedService.feedDetail(title,feed,ids);
            model.addAttribute(feed);
            String token = cookies.getValue();
            String user = jwtTokenProvider.getUserPk(token);
            String nickName = userService.nickName(user);
            model.addAttribute("user",nickName);
            model.addAttribute("id",userService.id(user));
            model.addAttribute("userId", user);
            model.addAttribute("img","../../"+feed.getImg());
            System.out.println(feed);
            model.addAttribute("title",title);
            return "feedDetail";
        }catch (Exception e){
            return "login";
        }
    }



    @PostMapping("/user/feed")
    @ResponseBody
    public String newFeed(@CookieValue(value="token", required=false) Cookie cookies,
                       @RequestParam("img")  MultipartFile img, Model model, ModelAndView mv, HttpServletRequest file,  HttpServletResponse response){
        Feed feed = new Feed();
        try {
            String userId = jwtTokenProvider.getUserPk(cookies.getValue());
            feedService.save(feed, userId,img,model,mv,file,response,path);
            return "<script> window.location = 'http://"+url+":8080/feed'</script>";
        }catch (NullPointerException e){
            return "<script>alert('login! '); window.location = 'http://"+url+":8080/login'</script>";
        }
        catch (Exception e){
            return "<script>alert('빈칸을 채우세요 '); history.back()</script>";
        }

    }

    @GetMapping("/allFeed")
    @ResponseBody
    public Page<Feed> allFeed(Pageable pageable){
        Page<Feed>  list= feedService.AllFeed(pageable);
        return list;
    }


    @PostMapping("/feed/delete")
    @ResponseBody
    public int delete(@RequestBody Feed feed){
        feedService.delete(feed);
        return 1;
    }




}
