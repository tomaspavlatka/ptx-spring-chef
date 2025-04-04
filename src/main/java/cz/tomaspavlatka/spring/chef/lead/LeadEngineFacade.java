package cz.tomaspavlatka.spring.chef.lead;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import cz.tomaspavlatka.spring.chef.lead.model.BillingsReport;
import cz.tomaspavlatka.spring.chef.lead.model.CompanyProfile;

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

  public BillingsReport forPeriod(Integer year, Integer month) {
    return leadEngineAuthTokenClient.getBillingsReport(leadEngineProperties.getPartnerId(), year, month);
  }

  public List<CompanyProfile> getCompanies() {
    var companies = new ArrayList<CompanyProfile>();

    var page = 1;
    var organizations = leadEngineJwtTokenClient.getOrganizations(page);
    organizations.rows().forEach(org -> companies.add(new CompanyProfile(
        org.id(),
        org.auth0Id(),
        Optional.of(org.name()),
        org.displayName())));

    while (++page <= organizations.totalPages()) {
      leadEngineJwtTokenClient.getOrganizations(page)
        .rows()
        .forEach(org -> companies.add(new CompanyProfile(
          org.id(),
          org.auth0Id(),
          Optional.of(org.name()),
          org.displayName()))
        );
    }

    return companies;
  }
}
