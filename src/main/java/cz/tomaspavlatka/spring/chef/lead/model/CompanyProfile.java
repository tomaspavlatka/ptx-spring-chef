package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.Optional;

public record CompanyProfile(String id, String auth0Id, Optional<String> name, Optional<String> displayName) {}
