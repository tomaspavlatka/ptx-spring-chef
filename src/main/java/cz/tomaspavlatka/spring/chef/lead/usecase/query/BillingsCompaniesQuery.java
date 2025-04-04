package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.CompanyBillingsSummary;
import cz.tomaspavlatka.spring.chef.lead.model.CompanyProfile;

@Service
public class BillingsCompaniesQuery {

  private final LeadEngineFacade leadEngineFacade;

  public BillingsCompaniesQuery(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  public List<CompanyBillingsSummary> getCompanies(Integer year, Integer month) {
    var companies = leadEngineFacade.getCompanies();

    return leadEngineFacade.forPeriod(year, month)
        .orgs()
        .stream()
        .map(org -> {
          var company = companies.stream().filter(comp -> comp.id().equals(org.id())).findFirst();
          var profile = new CompanyProfile(
              org.id(),
              org.auth0Id(),
              company.isPresent() ? company.get().name() : Optional.empty(),
              company.isPresent() ? company.get().displayName() : Optional.empty());
          var summary = new HashMap<String, Integer>();
          org.items().forEach(itm -> {
            var status = itm.status().toLowerCase();
            var current = summary.containsKey(status) ? summary.get(status) : 0;
            summary.put(status, ++current);
          });

          return new CompanyBillingsSummary(profile, summary);
        })
        .toList();
  }
}
