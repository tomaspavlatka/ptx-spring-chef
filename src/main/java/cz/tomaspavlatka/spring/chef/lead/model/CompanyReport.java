package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record CompanyReport(String id, String auth0Id, List<Billable> items) {
}
