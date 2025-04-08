package cz.tomaspavlatka.spring.chef.easybill;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "easybill")
public class EasybillProperties {
  private String baseUrl;
  private String authToken;
}
