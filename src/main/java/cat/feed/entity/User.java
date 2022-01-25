package cat.feed.entity;

import cat.feed.dto.BuyDto;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Map;

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
//        user.createBox(user);
        return user;
    }

    public User kakaoJoin(User user) throws Exception {
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setType("kakao");
//        user.createBox(user);
        return user;
    }

    public User naverJoin(User user) throws Exception {
        user.setRoles("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setType("naver");
//        user.createBox(user);
        return user;
    }

    private String path(User user){
        String paths = "/img/basket/"+user.getEmail()+".txt";
//        String paths = "/home/cat/Desktop/basket/"+user.getUserId()+".txt";
//        String paths = "/home/cat/img/basket/"+user.getEmail()+".txt";
        return paths;
    }

    public void createBox(User user) throws Exception {
        String paths = user.path(user);

        OutputStream outputStream = new FileOutputStream(paths);
        outputStream.close();

        user.setBox(paths);
    }

    public void readBox(User user, long itemId) throws IOException {
        String paths = user.path(user);
        BufferedReader read = new BufferedReader(new FileReader(paths));
        ArrayList<Long> arr = new ArrayList<>();

        while(true){
            String temp = read.readLine();
            System.out.println(temp);
            if(temp == null) break;
            long id = Long.parseLong(temp);
            arr.add(id);
        }
        arr.add(itemId);

        writeBox(user,arr);

    }

    public List<Long> myBox(User user) throws IOException {
        String paths = user.path(user);
        BufferedReader read = new BufferedReader(new FileReader(paths));
        ArrayList<Long> arr = new ArrayList<>();

        while(true){
            String temp = read.readLine();
            System.out.println(temp);
            if(temp == null) break;
            long id = Long.parseLong(temp);
            arr.add(id);
        }

        return arr;
    }


    public void writeBox(User user,ArrayList<Long> arr) throws IOException {
        String paths = user.path(user);

        BufferedWriter write = new BufferedWriter(new FileWriter(paths));

        for(long i:arr){
            write.write(i+"\n");
        }
        write.flush();
        write.close();

    }

    public void boxReload(User user) throws Exception {
        String paths = user.path(user);

        BufferedWriter write = new BufferedWriter(new FileWriter(paths));
        write.write("");
        write.flush();
        write.close();

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