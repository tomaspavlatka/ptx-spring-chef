package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.BillingItem;
import cz.tomaspavlatka.spring.chef.lead.model.CompanyItemizedBillings;
import cz.tomaspavlatka.spring.chef.lead.model.ItemizedBillingsPerCompany;
import cz.tomaspavlatka.spring.chef.lead.model.Organization;

@Service
public class GetItemizedBillingsPerCompanyQuery {

  private final LeadEngineFacade leadEngineFacade;

  public GetItemizedBillingsPerCompanyQuery(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  public ItemizedBillingsPerCompany execute(Integer year, Integer month, String search) {
    var organizations = this.leadEngineFacade.getOrganizations();
    var records = leadEngineFacade.forPeriod(year, month);
    var companies = records.orgs().stream()
        .filter(org -> search == null || org.name().contains(search.trim()))
        .map(org -> {
          var maybeCompany = organizations.stream()
              .filter(comp -> comp.id().equals(org.id()))
              .findFirst();

          // We need to ensure that price is negative for invalid_approved data
          var items = org.items()
              .stream()
              .sorted(Comparator.comparing(BillingItem::createdAt))
              .map(itm -> itm.status().toLowerCase().equals("invalid_approved")
                  ? new BillingItem(itm.id(), itm.price() * -1, itm.status(), itm.createdAt(), itm.lastName())
                  : itm)
              .toList();

          return new CompanyItemizedBillings(
              org.id(),
              org.auth0Id(),
              maybeCompany.map(Organization::name).orElse("n/a"),
              maybeCompany.map(Organization::displayName).orElse("n/a"),
              items);
        }).toList();

    return new ItemizedBillingsPerCompany(year, month, companies);
  }
}
