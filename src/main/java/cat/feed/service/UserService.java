package cat.feed.service;

import cat.feed.dto.AllDto;
import cat.feed.dto.MailDto;
import cat.feed.entity.User;
import cat.feed.jwt.JwtTokenProvider;
import cat.feed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "ShinTest94@gmail.com";

    public boolean checkUser(User user){
        return userRepository.existsByEmail(user.getEmail());
    }

    public void userJoin(User user) throws Exception{
        user = user.userJoin(user,passwordEncoder);
        userRepository.save(user);

    }


    public String createToken(User user) throws IllegalArgumentException{
        Optional<User> user2 = userRepository.findByEmail(user.getEmail());
        String password =user2.get().getPassword();

        if(!password.equals("111111")) {
            if (!passwordEncoder.matches(user.getPassword(), password)) throw new IllegalArgumentException();
        }
        return jwtTokenProvider.createToken(user.getEmail(), user2.get().getRoles());
    }


    public boolean check(String email,String type) {
        return userRepository.existsByEmailAndType(email,type);
    }

    public String login(String email, String type) {
        User user = userRepository.findByEmailAndType(email,type);
        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }

    public void kakaoJoin(User user) throws Exception {
        user = user.kakaoJoin(user);
        userRepository.save(user);
    }

    public void naverJoin(User user) throws Exception {
        user = user.naverJoin(user);
        userRepository.save(user);
    }

    public String nickName(String user) {
        return userRepository.findByEmail(user).get().getNickName();
    }

    public long id(String userId){return userRepository.findByEmail(userId).get().getId();}

    public User userInfo(User user) {
        return userRepository.findByEmail(user.getEmail()).get();
    }

    public void update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public long key(String userPk) {
        return userRepository.findByEmail(userPk).get().getId();
    }

    public AllDto allUser(AllDto dto) {
        dto.setUserList(userRepository.findByRoles("ROLE_USER"));
        dto.setAdminList(userRepository.findByRoles("ROLE_ADMIN"));
        dto.setUserCount(userRepository.countByRoles("ROLE_USER"));
        dto.setAdminCount(userRepository.countByRoles("ROLE_ADMIN"));
        return dto;
    }

    public void adminCreate(User user) {
        userRepository.save(user);
    }

    public String getRole(long userKey) {
        return userRepository.findById(userKey).get().getRoles();
    }

    public void delete(User user) {
        user = userRepository.findByEmail(user.getEmail()).get();
        userRepository.delete(user);
    }

//장바구니에 물품 추가
    public void insertBox(String userId, Long id,User user) throws IOException {
        user = userRepository.findUserByEmail(userId);
        String box = user.getBox()+"/"+id;
        user.setBox(box);
        System.out.println(user.getBox());
        userRepository.save(user);
    }


    public User boxReload(String userId) throws Exception {
        User user = userRepository.findUserByEmail(userId);
        user.setBox("");
        userRepository.save(user);
        return user;
    }

    public void mailSend(MailDto email) throws Exception{
        email.setInfo(email,mailSender,FROM_ADDRESS);


    }

    public void tempPassword(User user) {
        userRepository.save(user);
    }

    public void rePass(User user, User user2) {
        user2.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user2);
    }
}
