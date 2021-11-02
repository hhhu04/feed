package cat.feed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StoreController {

    @GetMapping("/store")
    public String store(){
        return "store/mainStore";
    }


}
