package cat.feed.service;

import cat.feed.entity.Feed;
import cat.feed.entity.Item;
import cat.feed.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
