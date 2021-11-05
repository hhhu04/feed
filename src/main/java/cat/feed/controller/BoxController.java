package cat.feed.controller;

import cat.feed.entity.Item;
import cat.feed.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoxController {

    private final StoreService itemService;

    @GetMapping("/box")
    public String myBox(Model model, Item item){


        return "store/myBox";
    }



}
