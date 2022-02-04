package cat.feed.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private String email;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private List<BuyLogs> buyLogs = new ArrayList<>();



    ///////////////////////////////////////////////////////



    public User userJoin(User user, PasswordEncoder passwordEncoder) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public User kakaoJoin(User user) throws Exception {
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setType("kakao");
        return user;
    }

    public User naverJoin(User user) throws Exception {
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setType("naver");
        return user;
    }


    public List<Long> myBox(User user) throws IOException {
        ArrayList<Long> arr = new ArrayList<>();
        String read = user.getBox();
        String[] sp = read.split("/");
        for(int i=1; i < sp.length; i++){
            long id = Long.parseLong(sp[i]);
            arr.add(id);
        }

        return arr;
    }


    ////JWT관련
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(this.getRoles()));
        return auth;
    }

    @Override
    public String getUsername() {
        return email;
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