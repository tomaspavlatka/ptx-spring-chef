package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.List;

public record ItemizedBillingsPerCompany(
    Integer year,
    Integer month,
    List<CompanyItemizedBillings> companies) {
}
