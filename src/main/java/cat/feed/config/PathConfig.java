package cat.feed.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathConfig implements WebMvcConfigurer {

    @Value("${test.path}")
    private String path;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///"+path+"/");

        registry.addResourceHandler("/item/img/**")
                .addResourceLocations("file:///"+path+"/");



    }



}
