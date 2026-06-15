package cl.aduana.ms_travel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Le decimos a Spring: Cuando el Gateway nos mande una petición con esta URL larga...
        registry.addResourceHandler("/api/v1/tramites/openapi/**")
                // ...tú ignora la ruta larga y busca el archivo directamente en la carpeta limpia:
                .addResourceLocations("classpath:/static/openapi/");
    }
}