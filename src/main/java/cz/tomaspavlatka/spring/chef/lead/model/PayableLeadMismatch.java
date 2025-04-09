package cz.tomaspavlatka.spring.chef.lead.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PayableLeadMismatch {
  String offerId;
  double price;
  String offerStatus;
  String createdAt;
  String reason;
}
