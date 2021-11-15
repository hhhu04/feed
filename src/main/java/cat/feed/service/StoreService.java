package cat.feed.service;

import cat.feed.dto.BuyDto;
import cat.feed.entity.Feed;
import cat.feed.entity.Item;
import cat.feed.entity.User;
import cat.feed.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Page<Item> AllFeed(Pageable pageable) {
        Page<Item> list = itemRepository.findAllByOrderByIdDesc(pageable);
        return list;
    }

    public Item feedDetail(String title, Item item) {
        return itemRepository.findByName(title);
    }

    public int newItem(Item item, MultipartFile img, String path) throws IOException {
        item = item.newItem(item,img,path);
        itemRepository.save(item);
        return 1;
    }

    public Item insertBox(String userId, Item item) throws Exception{
        return itemRepository.findItemById(item.getId());
    }

    public List<Item> myBox(List<Item> item, User user) throws IOException {
        System.out.println("!23");
        List<Long> arr = new ArrayList<>();
        arr = user.myBox(user);
        item = itemRepository.findAllById(arr);

        return item;
    }

    public void buy(String userId, BuyDto dto,String path) throws Exception{
        dto.buy(userId,dto, path);

    }
}
