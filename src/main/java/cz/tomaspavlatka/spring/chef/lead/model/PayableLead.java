package cz.tomaspavlatka.spring.chef.lead.model;

import lombok.Getter;

@Getter
public class PayableLead {
  String offerId;
  double price;
  String offerStatus;
  String auth0Id;
  String createdAt;
}
