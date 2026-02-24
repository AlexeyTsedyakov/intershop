package org.intershop.mvc.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebFluxConfigurer {
    private final String uploadImagePath;

    public WebConfig(@Value("${upload.image.path}") String uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/")
                .addResourceLocations("file:" + Path.of(uploadImagePath).toAbsolutePath() + "/");
    }
}
