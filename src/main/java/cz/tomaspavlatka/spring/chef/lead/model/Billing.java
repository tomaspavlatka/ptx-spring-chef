package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record Billing(String periodStart, String periodEnd, List<OrganizationBilling> orgs) { 
}

