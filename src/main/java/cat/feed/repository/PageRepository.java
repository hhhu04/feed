package cat.feed.repository;

import cat.feed.entity.Feed;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PageRepository extends PagingAndSortingRepository<Feed,Long> {
}
