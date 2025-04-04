package cz.tomaspavlatka.spring.chef.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineAuthTokenClient;
import cz.tomaspavlatka.spring.chef.lead.LeadEngineJwtTokenClient;
import cz.tomaspavlatka.spring.chef.lead.LeadEngineProperties;

@Configuration
public class ClientConfig {
  private final LeadEngineProperties leadEngineProperties;

  public ClientConfig(LeadEngineProperties leadEngineProperties) {
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
