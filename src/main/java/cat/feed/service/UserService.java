package cat.feed.service;

import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean checkUser(User user){
        System.out.println(userRepository.existsByUserId(user.getUserId()));
        return userRepository.existsByUserId(user.getUserId());
    }

    public void userJoin(User user) throws Exception{
        user = user.userJoin(user,passwordEncoder);
        userRepository.save(user);
    }


    public String createToken(User user) throws IllegalArgumentException{
        Optional<User> user2 = userRepository.findByUserId(user.getUserId());
        String password =user2.get().getPassword();
        if(!passwordEncoder.matches(user.getPassword(), password)) throw new IllegalArgumentException();
        return jwtTokenProvider.createToken(user.getUserId(), user2.get().getRoles());
    }


    public boolean check(String email,String type) {
        return userRepository.existsByUserIdAndType(email,type);
    }

    public String login(String email, String type) {
        User user = userRepository.findByUserIdAndType(email,type);
        return jwtTokenProvider.createToken(user.getUserId(), user.getRoles());
    }

    public void kakaoJoin(User user) {
        user = user.kakaoJoin(user);
        userRepository.save(user);
    }

    public String nickName(String user) {
        return userRepository.findByUserId(user).get().getNickName();
    }

    public long id(String userId){return userRepository.findByUserId(userId).get().getId();}
}
