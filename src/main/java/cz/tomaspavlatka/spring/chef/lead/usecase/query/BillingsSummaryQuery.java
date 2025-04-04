package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;

@Service
public class BillingsSummaryQuery {

  private final LeadEngineFacade leadEngineFacade;

  public BillingsSummaryQuery(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  public Map<String, Integer> getSummary(Integer year, Integer month) {
    var records = leadEngineFacade.forPeriod(year, month);
    var summary = new HashMap<String, Integer>();

    records.orgs().stream().forEach(org -> {
      org.items().stream().forEach(itm -> {
        var status = itm.status().toLowerCase();
        var current = summary.containsKey(status) ? summary.get(status) : 0;

        summary.put(status, ++current);
      });
    });

    return summary;
  }
}
