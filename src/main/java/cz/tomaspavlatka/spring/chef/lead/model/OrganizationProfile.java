package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.Optional;

public record OrganizationProfile(String id, String auth0Id, String name, Optional<String> displayName) {}
