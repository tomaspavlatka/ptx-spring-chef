package cz.tomaspavlatka.spring.chef.easybill.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import cz.tomaspavlatka.spring.chef.easybill.EasybillClient;
import cz.tomaspavlatka.spring.chef.easybill.EasybillProperties;
import reactor.netty.http.client.HttpClient;

@Configuration
public class EasybillClientConfig {
  private final EasybillProperties easybillProperties;

  public EasybillClientConfig(
      EasybillProperties easybillProperties) {
    this.easybillProperties = easybillProperties;
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
