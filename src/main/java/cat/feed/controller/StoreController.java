package cat.feed.controller;

import cat.feed.code.Res;
import cat.feed.code.ResponseMessage;
import cat.feed.code.StatusCode;
import cat.feed.dto.BuyDto;
import cat.feed.entity.BuyLogs;
import cat.feed.entity.Feed;
import cat.feed.entity.Item;
import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.service.BuyLogService;
import cat.feed.service.StoreService;
import cat.feed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class StoreController {

    private final StoreService itemService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final BuyLogService buyLogService;

    @Value("${test.url}")
    private String url;

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

    @GetMapping("/all_item")
    @ResponseBody
    public ResponseEntity allFeed(Pageable pageable) {
        Page<Item> list = itemService.AllFeed(pageable);
        return new ResponseEntity(Res.res(StatusCode.OK,
                ResponseMessage.CREATED_ITEM,list), HttpStatus.OK);
    }

    @GetMapping("/store/{id}")
    public ResponseEntity itemDetail(@RequestHeader HttpHeaders header,
                             @PathVariable(name = "id") Long id, Model model) {
        try {
            Item item;
            item = itemService.feedDetail(id);
            String token = header.get("authorization").get(0);
            String user = jwtTokenProvider.getUserPk(token);
            long userId = userService.id(user);

            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.READ_ITEM,item), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/myBasket")
    public String myBasket(Model model,@CookieValue(value = "token", required = false) Cookie cookies){
        User user = new User();
        List<Item> item = new ArrayList<>();

        try{
            String token = cookies.getValue();
            String userId = jwtTokenProvider.getUserPk(token);

            user.setEmail(userId);
            user = userService.userInfo(user);

            item = itemService.myBox(item,user);
            int num = item.size();

            List<Long> buyList = new ArrayList<>();
            int totalPrice = 0;

            for(int i=0 ; i<item.size(); i++){
                buyList.add(item.get(i).getId());
                totalPrice = totalPrice + item.get(i).getPrice();
            }

            model.addAttribute("item",item);
            model.addAttribute("num",num);
            model.addAttribute("list",buyList);
            model.addAttribute("totalPrice",totalPrice);


        }catch (Exception e){
            e.printStackTrace();
        }

        return "store/myBox";
    }

//
//    @GetMapping("/store/new")
//    public String item(@CookieValue(value = "token", required = false) Cookie cookies){
//        return "store/newItem";
//    }

    @PostMapping("/store/new")
    @ResponseBody
    public ResponseEntity newItem(@RequestHeader HttpHeaders header,
                                  @RequestParam(value = "title",required = false) String title,
                                  @RequestParam(value = "amount",required = false) int total,
                                  @RequestParam(value = "price",required = false) int price,
                                  @RequestParam(value = "img",required = false) MultipartFile img){

        try{
            Item item = new Item();
            String token = header.get("authorization").get(0);
            String id = jwtTokenProvider.getUserPk(token);
            String nickName = userService.nickName(id);
            long userKeyId = userService.id(id);

            item.setUserId(userKeyId);
            item.setName(title);
            item.setNickName(nickName);
            item.setTotal(total);
            item.setPrice(price);

            int num = itemService.newItem(item,img,path);

            return new ResponseEntity(Res.res(StatusCode.OK,
                    ResponseMessage.CREATED_ITEM), HttpStatus.OK);
        }
        catch (SQLException e){
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.DB_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(Res.res(StatusCode.INTERNAL_SERVER_ERROR,
                    ResponseMessage.CREATED_ITEM), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @PostMapping("/store/insertBox")
    @ResponseBody
    public int insert(@CookieValue(value = "token", required = false) Cookie cookies, @RequestBody Item item){

        try{
            User user = new User();
            String token = cookies.getValue();
            String userId = jwtTokenProvider.getUserPk(token);
            if(item.getTotal() == 0) return -1;
            item = itemService.insertBox(userId, item);
            userService.insertBox(userId,item.getId(),user);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return 1;
    }


    @PostMapping("/store/payment")
    @ResponseBody
    public List<String> payment(@CookieValue(value = "token", required = false) Cookie cookies, @RequestBody BuyDto dto, HttpSession session){
        List<String> list = new ArrayList<>();
        try {
            String token = cookies.getValue();
            String userId = jwtTokenProvider.getUserPk(token);

            list = itemService.buy(userId,dto,url);
            String tradeNumber = list.get(1);
            list.remove(1);

            String tid = list.get(1);
            list.remove(1);

            session.setAttribute("trade",tradeNumber);

            session.setAttribute("tid",tid);

            session.setAttribute("dto",dto);

            return list;


        }catch (Exception e){
            e.printStackTrace();
        }


        return list;
    }

    @GetMapping("/store/ok")
    public String ok(@CookieValue(value = "token", required = false) Cookie cookies, BuyDto dto,
                     @RequestParam String pg_token, Model model,HttpSession session){

        Map<String, String> map = new HashMap<>();

        try{

            String token = cookies.getValue();
            String userId = jwtTokenProvider.getUserPk(token);
            String tradeNumber = (String) session.getAttribute("trade");
            String tids = (String) session.getAttribute("tid");
            dto = (BuyDto) session.getAttribute("dto");

            session.invalidate();

            map = itemService.buySuccess(userId,tradeNumber,dto, tids,pg_token);

            User user = new User();
            BuyLogs buyLogs = new BuyLogs();
            List<Item> items = new ArrayList<>();
            List<String> itemNames = new ArrayList<>();

            user = userService.boxReload(userId);
            items = itemService.idToName(dto,items);
            itemNames = dto.idToName(items,itemNames);

            buyLogService.save(map,itemNames,user,buyLogs);
            itemService.quantity(dto);


        }catch (Exception e){
            e.printStackTrace();
        }


        return "sample/ok";
    }

    @GetMapping("/store/cencel")
    public String cencel(){
        return "sample/cancel";
    }

    @GetMapping("/store/fail")
    public String fail(){
        return "sample/fail";
    }


    @GetMapping("/allLog")
    @ResponseBody
    public Page<BuyLogs> allFeed(Pageable pageable,@CookieValue(value = "token", required = false) Cookie cookies){
        Page<BuyLogs>  list;
        try{
            String token = cookies.getValue();
            String userName = jwtTokenProvider.getUserPk(token);
            long userId = userService.id(userName);
            list= buyLogService.AllLog(pageable,userId);

        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }
        return list;
    }

}