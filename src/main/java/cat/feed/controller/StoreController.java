package cat.feed.controller;

import cat.feed.entity.Feed;
import cat.feed.entity.Item;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService itemService;
    private final JwtTokenProvider jwtTokenProvider;

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

            return "store/itemDetail";
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }






}