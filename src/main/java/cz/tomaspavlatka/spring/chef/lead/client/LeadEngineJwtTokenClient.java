package cz.tomaspavlatka.spring.chef.lead.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import cz.tomaspavlatka.spring.chef.lead.model.Organizations;

public interface LeadEngineJwtTokenClient {
  @GetExchange("/admin/orgs?page={page}")
  Organizations getOrganizations(@PathVariable Integer page);
}

