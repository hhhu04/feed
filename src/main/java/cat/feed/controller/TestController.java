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


    @GetMapping("/test")
    public String test(@CookieValue(value="token", required=false) Cookie cookies){
        return cookies.getValue();
    }

    @PostMapping("user/test")
    @ResponseBody
    public String feed(@RequestParam("title") String title,@RequestParam("body") String body,@RequestParam("img") MultipartFile img){
        return null;
    }

}
