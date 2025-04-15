package cz.tomaspavlatka.spring.chef.lead;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import cz.tomaspavlatka.spring.chef.lead.client.LeadEngineAuthTokenClient;
import cz.tomaspavlatka.spring.chef.lead.client.LeadEngineJwtTokenClient;
import cz.tomaspavlatka.spring.chef.lead.model.Billing;
import cz.tomaspavlatka.spring.chef.lead.model.Company;
import cz.tomaspavlatka.spring.chef.lead.model.Organization;

@Component
public class LeadEngineFacade {
  private final LeadEngineAuthTokenClient leadEngineAuthTokenClient;
  private final LeadEngineJwtTokenClient leadEngineJwtTokenClient;
  private final LeadEngineProperties leadEngineProperties;

  public LeadEngineFacade(
      LeadEngineAuthTokenClient leadEngineAuthTokenClient,
      LeadEngineJwtTokenClient leadEngineJwtTokenClient,
      LeadEngineProperties leadEngineProperties) {
    this.leadEngineProperties = leadEngineProperties;
    this.leadEngineJwtTokenClient = leadEngineJwtTokenClient;
    this.leadEngineAuthTokenClient = leadEngineAuthTokenClient;
  }

  public Billing forPeriod(Integer year, Integer month) {
    return leadEngineAuthTokenClient.getForPeriod(
        leadEngineProperties.getPartnerId(), year, month);
  }

  public List<Organization> getOrganizations() {
    var organizations = new ArrayList<Organization>();

    var page = 1;
    var response = leadEngineJwtTokenClient.getOrganizations(page);
    organizations.addAll(response.rows());

    while (++page <= response.totalPages()) {
      var resp = leadEngineJwtTokenClient.getOrganizations(page);
      organizations.addAll(resp.rows());
    }

    return organizations;
  }
}
