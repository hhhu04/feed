package cat.feed.controller;

import cat.feed.entity.Feed;
import cat.feed.repository.FeedRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final FeedRepository feedRepository;

    @GetMapping("/down")
    public ResponseEntity<Object> download(){
        String path = "/home/cat/web/test/feed.sql";

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

//    @PostMapping(value = "/newFeed")
    @ResponseBody
    public int create(@RequestParam("img") MultipartFile img, HttpServletRequest dto, Model model,
                      ModelAndView mv, @CookieValue(value="id", required=false) Cookie cookie, HttpServletResponse response) {
        try {
            System.out.println(dto.toString());
//            Long num = userService.checkCookie(cookie.getValue());
//            String userImg = userService.findImg(cookie.getValue());
//            return communityService.create(img,model,mv,dto,community,num,userImg,response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("asd");
            return -1;
        }
        return 0;
    }







    @PostConstruct
    public void initializing(){
        for(int i=0;i<100;i++){
            Feed feed = Feed.builder()
                    .title("title"+i)
                    .body("body"+i).build();
        }
    }



}
