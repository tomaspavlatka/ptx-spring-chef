package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record OrganizationBilling(String id, String auth0Id, String name, List<BillingItem> items) { }

