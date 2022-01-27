package cat.feed.controller;

import cat.feed.code.Res;
import cat.feed.code.ResponseMessage;
import cat.feed.code.StatusCode;
import cat.feed.entity.Feed;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.FeedService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class FeedController {

    private final FeedService feedService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    @Value("${test.url}")
    private  String url;
    @Value("${test.path}")
    private String path;


    //1:성공

    @GetMapping("favicon.ico") @ResponseBody public void returnNoFavicon() { }




    @GetMapping("/feed/{id}")
    public ResponseEntity feedDetail(@RequestHeader HttpHeaders headers, @PathVariable(name = "id") Long ids ){
        try {
            Feed feed = new Feed();
            String token = headers.get("authorization").get(0);
            feed = feedService.feedDetail(feed,ids);
            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.READ_USER,feed), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.READ_USER), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/feed/img",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> feedImg(@RequestParam(name = "image") String img){
        String url = path;
        try {
            File file = new File(url);
            BufferedImage image = ImageIO.read(new File(url));
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            ImageIO.write(image,"jpg",ba);
            ba.flush();
            HttpHeaders headers = new HttpHeaders();
            byte[] media = ba.toByteArray();
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());

            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode(media);

            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(encodedBytes, headers, HttpStatus.OK);
            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/feed/update/{id}")
    public String updateFeed(Model model, @PathVariable long id,@CookieValue(value="token") Cookie cookies){
       try {
           Long userId = feedService.userId(id);
           String userPk = jwtTokenProvider.getUserPk(cookies.getValue());
           long userKey = userService.key(userPk);
           model.addAttribute("id", id);
           if (userId == userKey) return "updateFeed";
           else return "error/403";
       }catch (Exception e){
           return "login";
       }
    }


    @PostMapping("/user/feed/update")
    @ResponseBody
    public int update(@CookieValue(value="token") Cookie cookies,
                       @RequestParam(value = "title") String title,
                       @RequestParam(value = "body") String body,
                       @RequestParam(value = "id") long id,
                       @RequestParam(value = "img") MultipartFile img){
        Feed feed = new Feed();
        feed = feedService.feed(id);
        feed.setTitle(title);
        feed.setBody(body);
        try {
            String userId = jwtTokenProvider.getUserPk(cookies.getValue());
            feed.setUpdatedAt(LocalDateTime.now());
            feedService.update(feed, userId,img,path);
            return 1;
        }catch (NullPointerException e){
            return -1;
        }
        catch (MultipartException e){
            return -2;
        }
        catch (Exception e){
            return -3;
        }

    }


    @PostMapping("/feed/new")
    public ResponseEntity newFeed(@RequestHeader HttpHeaders headers,
                          @RequestParam(value = "title",required = false) String title,
                          @RequestParam(value = "body",required = false) String body,
                          @RequestParam(value = "img",required = false) MultipartFile img){
        Feed feed = new Feed();
        feed.setTitle(title);
        feed.setBody(body);

        try {
            String userId = jwtTokenProvider.getUserPk(headers.get("authorization").get(0));
            feed.setCreatedAt(LocalDateTime.now());
            feedService.save(feed, userId,img,path);
            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.SAVE_SUCCESS), HttpStatus.OK);
        }catch (NullPointerException e){
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.SAVE_FAIL), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/all_feed")
    @ResponseBody
    public Page<Feed> allFeed(Pageable pageable){
        Page<Feed>  list= feedService.AllFeed(pageable);
        return list;
    }


    @PostMapping("/feed/delete")
    @ResponseBody
    public int delete(@RequestBody Feed feed,@CookieValue(value="token", required=false) Cookie cookies){
        try {
            String userPk = jwtTokenProvider.getUserPk(cookies.getValue());
            long userKey = userService.key(userPk);
            if(userKey == feed.getUserId()) {
                feedService.delete(feed);
                return 1;
            }
            else return -1;
        }catch (Exception e){
            return -2;
        }
    }




}
