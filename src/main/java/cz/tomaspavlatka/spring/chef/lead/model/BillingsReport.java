package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record BillingsReport(String periodStart, String periodEnd, List<BillingsOrganizationReport> orgs) {
}
