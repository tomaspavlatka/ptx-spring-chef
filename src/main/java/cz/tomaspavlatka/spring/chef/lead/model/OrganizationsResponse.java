package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record OrganizationsResponse(List<OrganizationProfile> rows, Integer totalPages) { }
