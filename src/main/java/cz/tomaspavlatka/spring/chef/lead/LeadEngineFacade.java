package cz.tomaspavlatka.spring.chef.lead;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cz.tomaspavlatka.spring.chef.lead.model.BillingsReport;

@Component
public class LeadEngineFacade {
  @Value("${lead.engine.partner-id}")
  private String partnerId;

  private final LeadEngineClient leadEngineClient;

  public LeadEngineFacade(LeadEngineClient leadEngineClient) {
    this.leadEngineClient = leadEngineClient;
  }

  public BillingsReport forPeriod(Integer year, Integer month) {
    return leadEngineClient.getBillingsReport(partnerId, year, month);
  }
}
