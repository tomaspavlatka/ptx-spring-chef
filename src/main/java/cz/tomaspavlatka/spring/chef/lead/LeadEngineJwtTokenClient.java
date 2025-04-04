package cz.tomaspavlatka.spring.chef.lead;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import cz.tomaspavlatka.spring.chef.lead.model.OrganizationsResponse;

public interface LeadEngineJwtTokenClient {
  @GetExchange("/admin/orgs?page={page}")
  OrganizationsResponse getOrganizations(@PathVariable Integer page);
}

