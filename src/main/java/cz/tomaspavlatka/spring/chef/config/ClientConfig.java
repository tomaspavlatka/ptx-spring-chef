package cz.tomaspavlatka.spring.chef.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineClient;

@Configuration
public class ClientConfig {
  @Value("${lead.engine.auth-token}")
  private String leadEngineAuthToken;

  @Value("${lead.engine.base-url}")
  private String leadEngineBaseUrl;

  @Bean
  LeadEngineClient leadEngineClient() {
    WebClient webClient = WebClient.builder()
        .baseUrl(leadEngineBaseUrl)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + leadEngineAuthToken)
        .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .build();

    return factory.createClient(LeadEngineClient.class);
  }
}
