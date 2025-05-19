package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record CompanyItemizedBillings(
    String id,
    String auth0Id,
    String name,
    String dispalyName,
    List<BillingItem> items) {
}
