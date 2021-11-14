package cat.feed.controller;

import cat.feed.entity.Feed;
import cat.feed.entity.Item;
import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.StoreService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService itemService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Value("${test.path}")
    private String path;

    @GetMapping("/store")
    public String store(Model model, @CookieValue(value = "token", required = false) Cookie cookie) {
        try {
            List<Item> items = new ArrayList<>();
            String token = cookie.getValue();
            String user = jwtTokenProvider.getUserPk(token);
            items = itemService.findAll();
            model.addAttribute("item", items);

    }catch (Exception e){
        model.addAttribute("user","게스트");
    }
        return "store/mainStore";
    }

    @GetMapping("/allItem")
    @ResponseBody
    public Page<Item> allFeed(Pageable pageable) {
        Page<Item> list = itemService.AllFeed(pageable);
        return list;
    }

    @GetMapping("/item/{title}")
    public String itemDetail(@CookieValue(value = "token", required = false) Cookie cookies,
                             @PathVariable(name = "title") String title, Model model) {
        try {
            Item item = new Item();
            item = itemService.feedDetail(title, item);
            model.addAttribute("item",item);
            String token = cookies.getValue();
            String user = jwtTokenProvider.getUserPk(token);
            long userId = userService.id(user);
            String role = userService.getRole(userId);

            model.addAttribute("id",userId);
            model.addAttribute("role",role);

            return "store/itemDetail";
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }


    @GetMapping("/user/myBasket")
    public String myBasket(Model model,@CookieValue(value = "token", required = false) Cookie cookies){
        User user = new User();
        List<Item> item = new ArrayList<>();

        try{
            String token = cookies.getValue();
            String userId = jwtTokenProvider.getUserPk(token);

            user.setUserId(userId);
            user = userService.userInfo(user);

            item = itemService.myBox(item,user);
            int num = item.size();

            model.addAttribute("item",item);
            model.addAttribute("num",num);


        }catch (Exception e){
            e.printStackTrace();
        }

        return "store/myBox";
    }


    @GetMapping("/store/new")
    public String item(@CookieValue(value = "token", required = false) Cookie cookies){
        return "store/newItem";
    }

    @PostMapping("/newItem")
    @ResponseBody
    public int newItem(@CookieValue(value = "token", required = false) Cookie cookies,
                       @RequestParam(value = "title",required = false) String title,
                       @RequestParam(value = "number",required = false) int total,
                       @RequestParam(value = "price",required = false) int price,
                       @RequestParam(value = "img",required = false) MultipartFile img){

        try{
            Item item = new Item();
            String token = cookies.getValue();
            String id = jwtTokenProvider.getUserPk(token);
            String nickName = userService.nickName(id);
            long userKeyId = userService.id(id);

            item.setUserId(userKeyId);
            item.setName(title);
            item.setNickName(nickName);
            item.setTotal(total);
            item.setPrice(price);

            int num = itemService.newItem(item,img,path);



        }catch (Exception e){
            e.printStackTrace();
        }


        return 1;
    }



    @PostMapping("/store/insertBox")
    @ResponseBody
    public int insert(@CookieValue(value = "token", required = false) Cookie cookies, @RequestBody Item item){

        try{
            User user = new User();
            String token = cookies.getValue();
            String userId = jwtTokenProvider.getUserPk(token);
            item = itemService.insertBox(userId, item);
            userService.insertBox(userId,item.getId(),user);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        return 1;
    }


}