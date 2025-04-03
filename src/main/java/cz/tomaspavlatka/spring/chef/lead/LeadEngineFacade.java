package cz.tomaspavlatka.spring.chef.lead;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cz.tomaspavlatka.spring.chef.lead.model.PeriodReport;

@Component
public class LeadEngineFacade {
  @Value("${lead.engine.partner-id}")
  private String partnerId;

  private final LeadEngineClient leadEngineClient;

  public LeadEngineFacade(LeadEngineClient leadEngineClient) {
    this.leadEngineClient = leadEngineClient;
  }

  public PeriodReport forPeriod(Integer year, Integer month) {
    return leadEngineClient.getReports(partnerId, year, month);
  }
}
