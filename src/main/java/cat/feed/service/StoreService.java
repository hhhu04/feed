package cat.feed.service;

import cat.feed.dto.BuyDto;
import cat.feed.entity.Item;
import cat.feed.entity.User;
import cat.feed.repository.ItemRepository;
import cat.feed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Page<Item> AllFeed(Pageable pageable) {
        Page<Item> list = itemRepository.findAllByOrderByIdDesc(pageable);
        return list;
    }

    public Item feedDetail(long id, String path) throws IOException {
        return itemRepository.findById(id).get();
    }

    public int newItem(Item item, MultipartFile img, String path) throws Exception {
        item = item.newItem(item,img,path);
        itemRepository.save(item);
        return 1;
    }

    public Item insertBox(Item item) throws Exception{
        return itemRepository.findItemById(item.getId());
    }

    public List<Item> myBox(List<Item> item, User user) throws IOException {
        List<Long> arr = new ArrayList<>();
        arr = user.myBox(user);
        item = itemRepository.findAllById(arr);

        return item;
    }

    public List<String> buy(String userId, BuyDto dto,String path) throws Exception{
        return dto.buy(userId,dto, path);

    }

    public Map<String, String> buySuccess(String userId, String tradeNumber, BuyDto dto,String tid, String pg_token) {
        Map<String, String> map = new HashMap<>();

        map = dto.buySuccess(userId,tradeNumber,dto, tid,pg_token);
        return map;

    }

    public List<Item> idToName(BuyDto dto) {
        List<Long> items = new ArrayList<>();
        items = dto.setLong(dto,items);
         return itemRepository.findAllById(items);
    }


    public void quantity(BuyDto dto) {
        List<Item> list = new ArrayList<>();
        List<Long> items = new ArrayList<>();
        items = dto.setLong(dto,items);
        list = itemRepository.findAllById(items);
        dto.quantity(list);
        itemRepository.saveAll(list);

    }
}
