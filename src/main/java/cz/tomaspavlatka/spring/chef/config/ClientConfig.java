package cz.tomaspavlatka.spring.chef.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import cz.tomaspavlatka.spring.chef.easybill.EasybillClient;
import cz.tomaspavlatka.spring.chef.easybill.EasybillProperties;
import cz.tomaspavlatka.spring.chef.lead.LeadEngineAuthTokenClient;
import cz.tomaspavlatka.spring.chef.lead.LeadEngineJwtTokenClient;
import cz.tomaspavlatka.spring.chef.lead.LeadEngineProperties;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ClientConfig {
  private final LeadEngineProperties leadEngineProperties;
  private final EasybillProperties easybillProperties;

  public ClientConfig(
      LeadEngineProperties leadEngineProperties,
      EasybillProperties easybillProperties) {
    this.leadEngineProperties = leadEngineProperties;
    this.easybillProperties = easybillProperties;
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

  @Bean
  EasybillClient easybillClient() {
    HttpClient httpClient = HttpClient.create()
        .httpResponseDecoder(spec -> spec.maxHeaderSize(16 * 1024)); // Increas

    WebClient webClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .baseUrl(easybillProperties.getBaseUrl())
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + easybillProperties.getAuthToken())
        .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
        .builderFor(WebClientAdapter.create(webClient))
        .build();

    return factory.createClient(EasybillClient.class);
  }
}
