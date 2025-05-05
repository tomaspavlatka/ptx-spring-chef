package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.BillingsPerCompany;
import cz.tomaspavlatka.spring.chef.lead.model.CompanyBillings;
import cz.tomaspavlatka.spring.chef.lead.model.Organization;

@Service
public class GetBillingsPerCompanyQuery {

  private final LeadEngineFacade leadEngineFacade;

  public GetBillingsPerCompanyQuery(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  public BillingsPerCompany execute(Integer year, Integer month) {
    var organizations = this.leadEngineFacade.getOrganizations();
    var records = leadEngineFacade.forPeriod(year, month);
    var companies = records.orgs().stream().map(org -> {
      var maybeCompany = organizations.stream()
          .filter(comp -> comp.id().equals(org.id()))
          .findFirst();

      var quantityPerStatus = new HashMap<String, Integer>();
      var prices = new HashMap<String, Float>();
      var quantityPerStatusAndPrice = new HashMap<String, Map<Float, Integer>>();

      org.items().forEach(itm -> {
        var key = itm.status().toLowerCase();
        var currentQty = quantityPerStatus.getOrDefault(key, 0);
        quantityPerStatus.put(key, ++currentQty);

        var currentPrice = prices.getOrDefault(key, 0F);
        prices.put(key, currentPrice + itm.price());

        var currentSP = quantityPerStatusAndPrice.getOrDefault(key, new HashMap<Float, Integer>());
        var currentSPQ = currentSP.getOrDefault(itm.price(), 0);
        currentSP.put(itm.price(), ++currentSPQ);
        quantityPerStatusAndPrice.put(key, currentSP);
      });

      return new CompanyBillings(
          org.id(),
          org.auth0Id(),
          maybeCompany.map(Organization::name).orElse("n/a"),
          maybeCompany.map(Organization::displayName).orElse("n/a"),
          quantityPerStatus,
          prices,
          quantityPerStatusAndPrice);
    }).toList();

    return new BillingsPerCompany(year, month, companies);
  }
}
