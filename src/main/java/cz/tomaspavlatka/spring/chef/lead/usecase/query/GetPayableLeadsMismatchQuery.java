package cz.tomaspavlatka.spring.chef.lead.usecase.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cz.tomaspavlatka.spring.chef.lead.model.LegacyPayableLead;
import cz.tomaspavlatka.spring.chef.lead.model.PayableLead;
import cz.tomaspavlatka.spring.chef.lead.model.PayableLeadMismatch;

@Service
public class GetPayableLeadsMismatchQuery {

  private final GetCurrentPayableLeadsQuery getCurrentPayableLeadsQuery;
  private final GetLegacyPayableLeadsQuery getLegacyPayableLeadsQuery;

  public GetPayableLeadsMismatchQuery(
      GetLegacyPayableLeadsQuery getLegacyPayableLeadsQuery,
      GetCurrentPayableLeadsQuery getCurrentPayableLeadsQuery) {
    this.getCurrentPayableLeadsQuery = getCurrentPayableLeadsQuery;
    this.getLegacyPayableLeadsQuery = getLegacyPayableLeadsQuery;
  }

  public Map<String, List<PayableLeadMismatch>> getPayableLeads(Integer month) throws IOException {
    var mismatch = new HashMap<String, List<PayableLeadMismatch>>();

    var legacy = getLegacyPayableLeadsQuery.getPayableLeads(month);
    var current = getCurrentPayableLeadsQuery.getPayableLeads(month);

    legacy.keySet().forEach(key -> {
      var mis = mismatch.getOrDefault(key, new ArrayList<PayableLeadMismatch>());

      var partner = current.getOrDefault(key, new ArrayList<PayableLead>());
      legacy.get(key).forEach(l -> {
        var maybeFriend = partner.stream().filter(p -> p.getOfferId().equals(l.getOfferId())).findFirst();

        // We found the data
        if (maybeFriend.isPresent()) {
          var friend = maybeFriend.get();
          if (friend.getPrice() != l.getPrice()) {
            mis.add(PayableLeadMismatch
                .builder()
                .offerId(l.getOfferId())
                .price(l.getPrice())
                .offerStatus(l.getHistoryStatus())
                .createdAt(l.getCreatedAt())
                .reason("Price difference " + friend.getPrice())
                .build());
          }

          if (!friend.getOfferStatus().equals(l.getHistoryStatus())) {
            mis.add(PayableLeadMismatch
                .builder()
                .offerId(l.getOfferId())
                .price(l.getPrice())
                .offerStatus(l.getHistoryStatus())
                .createdAt(l.getCreatedAt())
                .reason("Status difference " + friend.getOfferStatus())
                .build());
          }
        } else {
          mis.add(PayableLeadMismatch
              .builder()
              .offerId(l.getOfferId())
              .price(l.getPrice())
              .offerStatus(l.getOfferStatus())
              .createdAt(l.getCreatedAt())
              .reason("Missing")
              .build());
        }
      });

      mismatch.put(key, mis);
    });

    current.keySet().forEach(key -> {
      var mis = mismatch.getOrDefault(key, new ArrayList<PayableLeadMismatch>());

      var partner = legacy.getOrDefault(key, new ArrayList<LegacyPayableLead>());
      legacy.get(key).forEach(l -> {
        var maybeFriend = partner.stream().filter(p -> p.getOfferId().equals(l.getOfferId())).findFirst();
        if (maybeFriend.isEmpty()) {
          mis.add(PayableLeadMismatch
              .builder()
              .offerId(l.getOfferId())
              .price(l.getPrice())
              .offerStatus(l.getOfferStatus())
              .createdAt(l.getCreatedAt())
              .reason("Added")
              .build());
        }
      });

      mismatch.put(key, mis);
    });

    return mismatch;
  }

}
