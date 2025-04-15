package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record BillingsPerCompany(
    Integer year,
    Integer month,
    List<CompanyBillings> companies) {
}
