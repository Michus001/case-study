package cz.casestudy.interview.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "application.configuration", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Long bookingExpirationInMillis;
}
