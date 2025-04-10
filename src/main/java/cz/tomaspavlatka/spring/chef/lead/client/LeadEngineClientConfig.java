package cz.tomaspavlatka.spring.chef.lead.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineProperties;

@Configuration
public class LeadEngineClientConfig {
  private final LeadEngineProperties leadEngineProperties;

  public LeadEngineClientConfig(
      LeadEngineProperties leadEngineProperties) {
    this.leadEngineProperties = leadEngineProperties;
  }

  @Bean
  LeadEngineJwtTokenClient leadEngineJwtTokenClient() {
    WebClient webClient = WebClient.builder()
        .baseUrl(leadEngineProperties.getBaseUrl())
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + leadEngineProperties.getJwtToken())
        .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .build();

    return factory.createClient(LeadEngineJwtTokenClient.class);
  }

  @Bean
  LeadEngineAuthTokenClient leadEngineAuthTokenClient() {
    WebClient webClient = WebClient.builder()
        .baseUrl(leadEngineProperties.getBaseUrl())
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + leadEngineProperties.getAuthToken())
        .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .build();

    return factory.createClient(LeadEngineAuthTokenClient.class);
  }
}
