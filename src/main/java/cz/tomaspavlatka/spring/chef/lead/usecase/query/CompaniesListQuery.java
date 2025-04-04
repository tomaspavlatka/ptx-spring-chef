package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.util.List;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.CompanyProfile;

@Service
public class CompaniesListQuery {
  private final LeadEngineFacade leadEngineFacade;

  public CompaniesListQuery(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  public List<CompanyProfile> getCompanies(Integer year, Integer month) {
    return leadEngineFacade.forPeriod(year, month)
        .orgs()
        .stream()
        .map(comp -> new CompanyProfile(comp.id(), comp.auth0Id())).toList();
  }
}
