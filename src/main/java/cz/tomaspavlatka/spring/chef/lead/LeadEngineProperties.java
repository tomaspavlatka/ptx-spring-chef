package cz.tomaspavlatka.spring.chef.lead;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "lead-engine")
public class LeadEngineProperties {
  private String baseUrl;
  private String partnerId;
  private String authToken;
}
