package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.BillingsSummary;

@Service
public class GetBillingsSummaryQuery {

  private final LeadEngineFacade leadEngineFacade;

  public GetBillingsSummaryQuery(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  public BillingsSummary execute(Integer year, Integer month) {
    var records = leadEngineFacade.forPeriod(year, month);
    var quantites = new HashMap<String, Integer>();
    var prices = new HashMap<String, Float>();
    var statusPrices = new HashMap<String, Map<Float, Integer>>();

    records.orgs().stream().forEach(org -> {
      org.items().stream().forEach(itm -> {
        var status = itm.status().toLowerCase();

        var qty = quantites.getOrDefault(status, 0);
        quantites.put(status, ++qty);

        var price = prices.getOrDefault(status, 0F);
        prices.put(status, (price + itm.price()));

        var statusPrice = statusPrices.getOrDefault(status, new HashMap<Float, Integer>());
        var priceQty = statusPrice.getOrDefault(itm.price(), 0);
        statusPrice.put(itm.price(), ++priceQty);
        statusPrices.put(status, statusPrice);
      });
    });

    return new BillingsSummary(year, month, quantites, prices, statusPrices);
  }
}
