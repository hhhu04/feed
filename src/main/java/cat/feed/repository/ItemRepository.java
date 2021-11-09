package cat.feed.repository;

import cat.feed.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByOrderByIdDesc(Pageable pageable);

    Item findByName(String name);

    Item findItemById(Long id);
}
