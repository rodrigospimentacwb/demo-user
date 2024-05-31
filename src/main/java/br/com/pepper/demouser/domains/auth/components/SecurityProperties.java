package br.com.pepper.demouser.domains.auth.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {
    private List<String> permittedPaths;
    private List<String> corsAllowedOrigins;
    private List<String> corsAllowedMethods;
    private List<String> corsAllowedHeaders;
    private List<String> corsAllowedPaths;
}

