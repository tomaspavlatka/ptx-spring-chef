package cz.tomaspavlatka.spring.chef.lead;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import cz.tomaspavlatka.spring.chef.lead.model.BillingsReport;

public interface LeadEngineAuthTokenClient {
  @GetExchange("/billings/{partnerId}/leads?year={year}&month={month}")
  BillingsReport getBillingsReport(
      @PathVariable String partnerId,
      @PathVariable Integer year,
      @PathVariable Integer month);
}
