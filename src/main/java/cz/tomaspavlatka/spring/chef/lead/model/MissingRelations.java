package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record MissingRelations(Integer year, Integer month, List<Company> missingCompanies) {
}
