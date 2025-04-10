package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.BillingsSummary;

@Service
public class GetBillingsSummaryQuery {

  private final LeadEngineFacade leadEngineFacade;

  public GetBillingsSummaryQuery(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  public BillingsSummary getSummary(Integer year, Integer month) {
    var records = leadEngineFacade.forPeriod(year, month);
    var quantites = new HashMap<String, Integer>();
    var prices = new HashMap<String, Float>();

    records.orgs().stream().forEach(org -> {
      org.items().stream().forEach(itm -> {
        var status = itm.status().toLowerCase();

        var qty = quantites.containsKey(status) ? quantites.get(status) : 0;
        quantites.put(status, ++qty);

        var price = prices.containsKey(status) ? prices.get(status) : 0;
        prices.put(status, (price + itm.price()));
      });
    });

    return new BillingsSummary(year, month, quantites, prices);
  }
}
