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

import javax.servlet.http.Cookie;

@Controller
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @Value("${test.url}")
    private  String url;

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

    @GetMapping("/feed/{title}")
    public String feedDetail(@CookieValue(value="token", required=false) Cookie cookies,
                             @PathVariable(name = "title") String title,Model model){
        try {
            String id = cookies.getValue();
            Feed feed = new Feed();
            feed = feedService.feedDetail(title,feed);
            model.addAttribute(feed);
            String token = cookies.getValue();
            String user = jwtTokenProvider.getUserPk(token);
            String nickName = userService.nickName(user);
            model.addAttribute("user",nickName);
            model.addAttribute("id",userService.id(user));
            model.addAttribute("userId", user);
            System.out.println(feed);
            model.addAttribute("title",title);
            return "feedDetail";
        }catch (Exception e){
            return "login";
        }
    }



    @PostMapping("/user/feed")
    @ResponseBody
    public int newFeed(@RequestBody Feed feed,@CookieValue(value="token", required=false) Cookie cookies){
        try {
            String userId = jwtTokenProvider.getUserPk(cookies.getValue());
            feedService.save(feed, userId);
            return 1;
        }catch (Exception e){
            return -1;
        }

    }

    @GetMapping("/allFeed")
    @ResponseBody
    public Page<Feed> allFeed(Pageable pageable){
//        List<Feed> list = new ArrayList<>();
//        list = feedService.AllFeed(pageable);
//        PageRequest pageRequest = PageRequest.of(2,2, Sort.Direction.DESC);
        Page<Feed>  list= feedService.AllFeed(pageable);
        return list;
    }






}
