package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record BillingsOrganizationReport(String id, String auth0Id, List<BillingsItem> items) {
}
