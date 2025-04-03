package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record PeriodReport(String periodStart, String periodEnd, List<CompanyReport> orgs) {
}
