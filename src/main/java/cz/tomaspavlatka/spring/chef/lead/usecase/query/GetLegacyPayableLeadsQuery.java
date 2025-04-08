package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.model.LegacyPayableLead;

@Service
public class GetLegacyPayableLeadsQuery {

  private final GetPayableLeadsQuery getPayableLeadsQuery;

  public GetLegacyPayableLeadsQuery(GetPayableLeadsQuery getPayableLeadsQuery) {
    this.getPayableLeadsQuery = getPayableLeadsQuery;
  }

  public Map<String, List<LegacyPayableLead>> getPayableLeads(Integer month) throws IOException {
    var accepted = new HashMap<String, LegacyPayableLead>();
    getPayableLeadsQuery
        .getPayableLeads("legacy-bought-leads-" + month + ".csv", LegacyPayableLead.class)
        .forEach(lead -> {
          if (!accepted.containsKey(lead.getOfferId())) {
            accepted.put(lead.getOfferId(), lead);
          }
        });

    var reclaimed = new HashMap<String, LegacyPayableLead>();
    getPayableLeadsQuery
        .getPayableLeads("legacy-reclaimed-leads-" + month + ".csv", LegacyPayableLead.class)
        .forEach(lead -> {
          if (!reclaimed.containsKey(lead.getOfferId())) {
            reclaimed.put(lead.getOfferId(), lead);
          }
        });

    var toRemove = accepted.keySet().stream()
        .filter(reclaimed::containsKey)
        .toList();
    toRemove.forEach(key -> {
      accepted.remove(key);
      reclaimed.remove(key);
    });

    // We remove all reclaimed record which happend within first 3 days of the month
    // as they already belong to previous period
    var toRemoveAppliedReclaimed = reclaimed.entrySet().stream()
        .filter(entry -> {
          var day = entry.getValue().getCreatedAt().split(" ")[0].split("-")[2];
          return List.of("01", "02", "03").contains(day);
        })
        .map(Map.Entry::getKey)
        .toList();
    System.out.println(toRemoveAppliedReclaimed);
    toRemoveAppliedReclaimed.forEach(reclaimed::remove);

    // We remove all records where history status differs form offer status
    // as they already belong to previous period
    var toRemoveReclaimedStatus = reclaimed.entrySet().stream()
        .filter(entry -> !entry.getValue().getOfferStatus().equals(entry.getValue().getHistoryStatus()))
        .map(Map.Entry::getKey).toList();
    toRemoveReclaimedStatus.forEach(reclaimed::remove);

    var perCompany = new HashMap<String, List<LegacyPayableLead>>();
    accepted.values().forEach(lead -> {
      var current = perCompany.getOrDefault(lead.getAuth0Id(), new ArrayList<LegacyPayableLead>());
      current.add(lead);

      perCompany.put(lead.getAuth0Id(), current);
    });

    reclaimed.values().forEach(lead -> {
      var current = perCompany.getOrDefault(lead.getAuth0Id(), new ArrayList<LegacyPayableLead>());
      current.add(lead);

      perCompany.put(lead.getAuth0Id(), current);
    });

    return perCompany;
  }
}
