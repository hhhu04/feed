package cat.feed.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String password;

    private String nickName;

    private String roles;

    private String grade;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String type;

    private String box;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<Item> items = new ArrayList<>();






    ///////////////////////////////////////////////////////



    public User userJoin(User user, PasswordEncoder passwordEncoder) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.createBox(user);
        return user;
    }

    public User kakaoJoin(User user) throws Exception {
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setType("kakao");
        user.createBox(user);
        return user;
    }

    public User naverJoin(User user) throws Exception {
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setType("naver");
        user.createBox(user);
        return user;
    }

    public String createBox(User user) throws Exception {
        String paths = "/img/basket";
//        String paths = "/home/cat/Desktop/basket/"+userId+".txt";
        OutputStream outputStream = new FileOutputStream(paths);
        outputStream.close();

        user.setBox(paths);
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(this.getRoles()));
        return auth;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}