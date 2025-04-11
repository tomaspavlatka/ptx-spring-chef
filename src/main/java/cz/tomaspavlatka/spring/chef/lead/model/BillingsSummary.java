package cz.tomaspavlatka.spring.chef.lead.model;

import java.util.Map;

public record BillingsSummary(
    Integer year,
    Integer month,
    Map<String, Integer> quantities,
    Map<String, Float> prices,
    Map<String, Map<Float, Integer>> statusPriceQuanties) {
}
