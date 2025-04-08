package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.model.PayableLead;

@Service
public class GetCurrentPayableLeadsQuery {

  private final GetPayableLeadsQuery getPayableLeadsQuery;

  public GetCurrentPayableLeadsQuery(GetPayableLeadsQuery getPayableLeadsQuery) {
    this.getPayableLeadsQuery = getPayableLeadsQuery;
  }

  public Map<String, List<PayableLead>> getPayableLeads(Integer month) throws IOException {
    var perCompany = new HashMap<String, List<PayableLead>>();
    getPayableLeadsQuery
        .getPayableLeads("current-bought-leads-" + month + ".csv", PayableLead.class)
        .forEach(lead -> {
          var current = perCompany.getOrDefault(lead.getAuth0Id(), new ArrayList<PayableLead>());
          current.add(lead);

          perCompany.put(lead.getAuth0Id(), current);
        });

    getPayableLeadsQuery
        .getPayableLeads("current-reclaimed-leads-" + month + ".csv", PayableLead.class)
        .forEach(lead -> {
          var current = perCompany.getOrDefault(lead.getAuth0Id(), new ArrayList<PayableLead>());
          current.add(lead);

          perCompany.put(lead.getAuth0Id(), current);
        });

    return perCompany;
  }
}
