package cz.tomaspavlatka.spring.chef.lead.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import cz.tomaspavlatka.spring.chef.lead.model.Billing;

public interface LeadEngineAuthTokenClient {
  @GetExchange("/billings/{partnerId}/leads?year={year}&month={month}")
  Billing getForPeriod(
      @PathVariable String partnerId,
      @PathVariable Integer year,
      @PathVariable Integer month);
}
