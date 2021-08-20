package cat.feed.repository;

import cat.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

//public interface FeedRepository extends JpaRepository<Feed,Long> {
public interface FeedRepository extends PagingAndSortingRepository<Feed,Long> {

//    List<Feed> findAll();

//    Iterable<Feed> findAll(Sort sort);

//    Page<Feed> findAll(Pageable pageable);
    Page<Feed> findAllByOrderByIdDesc(Pageable pageable);

    Feed findFeedByTitle(String title);



}
