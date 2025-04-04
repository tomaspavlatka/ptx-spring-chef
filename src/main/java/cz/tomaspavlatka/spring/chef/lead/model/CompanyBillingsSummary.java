package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.Map;

public record CompanyBillingsSummary(CompanyProfile company, Map<String, Integer> summary) {
}
