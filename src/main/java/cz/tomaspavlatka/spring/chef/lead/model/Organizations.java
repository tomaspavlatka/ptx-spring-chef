package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record Organizations(List<Organization> rows, Integer totalPages) { 
}

