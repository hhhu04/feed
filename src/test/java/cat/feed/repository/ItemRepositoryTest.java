package cat.feed.repository;

import cat.feed.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void save(){
        Item item = new Item();
        item.setCreatedAt(LocalDateTime.now());
        item.setTotal(50);
        item.setUserId(32);
        item.setName("배");
        itemRepository.save(item);
    }

}