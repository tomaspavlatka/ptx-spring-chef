package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.Map;

public record CompanyBillings(
    String id,
    String auth0Id,
    String name,
    String dispalyName,
    Map<String, Integer> quantityPerStatus,
    Map<String, Float> prices,
    Map<String, Map<Float, Integer>> statusPriceQuanties) {
}
