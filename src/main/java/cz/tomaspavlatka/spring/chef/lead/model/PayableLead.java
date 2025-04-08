package cz.tomaspavlatka.spring.chef.lead.model;

import lombok.Getter;

@Getter
public class PayableLead {
  String historyStatus;
  String createdAt;
  String offerId;
  String offerStatus;
  double price;
  String orgId;
  String auth0Id;
}
