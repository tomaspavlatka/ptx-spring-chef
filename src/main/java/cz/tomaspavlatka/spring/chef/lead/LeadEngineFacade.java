package cz.tomaspavlatka.spring.chef.lead;

import org.springframework.stereotype.Component;

import cz.tomaspavlatka.spring.chef.lead.model.BillingsReport;

@Component
public class LeadEngineFacade {
  private final LeadEngineAuthTokenClient leadEngineAuthTokenClient;
  private final LeadEngineProperties leadEngineProperties;

  public LeadEngineFacade(
      LeadEngineAuthTokenClient leadEngineAuthTokenClient,
      LeadEngineProperties leadEngineProperties) {
    this.leadEngineProperties = leadEngineProperties;
    this.leadEngineAuthTokenClient = leadEngineAuthTokenClient;
  }

  public BillingsReport forPeriod(Integer year, Integer month) {
    return leadEngineAuthTokenClient.getBillingsReport(leadEngineProperties.getPartnerId(), year, month);
  }
}
