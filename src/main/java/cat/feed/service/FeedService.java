package cat.feed.service;

import cat.feed.entity.Feed;
import cat.feed.repository.FeedRepository;
import cat.feed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;


    public void save(Feed feed, String email, MultipartFile img,String path) throws Exception{
        feed = feed.upLoad(img,feed,userRepository.findByEmail(email).get().getId(),path);
        feed.setNickName(userRepository.findByEmail(email).get().getNickName());
        feedRepository.save(feed);
    }

    public void update(Feed feed, String email, MultipartFile img,String path) throws Exception{
        feed = feed.upLoad(img,feed,userRepository.findByEmail(email).get().getId(),path);
        feed.setNickName(userRepository.findByEmail(email).get().getNickName());
        feedRepository.save(feed);
    }


    public Page<Feed> AllFeed(Pageable pageable) {
        Page<Feed> list = feedRepository.findAllByOrderByIdDesc(pageable);
        return list;
    }

    public Feed feedDetail(String title, Feed list,Long id) {
        list = feedRepository.findFeedByTitleAndId(title,id);
        return list;
    }

    public void delete(Feed feed) {
        feed = feedRepository.findById(feed.getId()).get();
        feedRepository.delete(feed);
    }

    public Feed feed(long id) {
        return feedRepository.findById(id).get();
    }

    public Long userId(long id) {
        return feedRepository.findById(id).get().getUserId();
    }

    public long AllFeedAdmin() {
        return feedRepository.count();
    }

    public long count() {
        return feedRepository.count();
    }


}
