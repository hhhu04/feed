package cat.feed.controller;

import cat.feed.entity.Feed;
import cat.feed.repository.FeedRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final FeedRepository feedRepository;
//
//    @GetMapping("/page")
//    public Page<Feed> getFeed(){
//        PageRequest pageRequest = PageRequest.of(0,2);
//        return feedRepository.findAll(pageRequest);
//    }
//
//
//    @GetMapping("/pages")
//    public Page<Feed> getAllFeed(@RequestParam("page") Integer page,@RequestParam("size") Integer size){
//        PageRequest pageRequest = PageRequest.of(page,size);
//        return feedRepository.findAll(pageRequest);
//
//    }





    @PostConstruct
    public void initializing(){
        for(int i=0;i<100;i++){
            Feed feed = Feed.builder()
                    .title("title"+i)
                    .body("body"+i).build();
        }
    }



}
