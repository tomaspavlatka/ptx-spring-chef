package cz.tomaspavlatka.spring.chef.command;

import java.util.Map;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetBillingsPerCompanyQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetBillingsSummaryQuery;
import lombok.RequiredArgsConstructor;

@Command(group = "Leads", command = "leads")
@RequiredArgsConstructor
public class LeadBillingsCommand {
  @Autowired
  private Terminal terminal;

  private final GetBillingsSummaryQuery getBillingsSummaryQuery;
  private final GetBillingsPerCompanyQuery getBillingspBillingsPerCompanyQuery;

  @Command(command = "billing-summary", description = "Shows lead billing summary for specific month")
  void summary(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var summary = getBillingsSummaryQuery.getSummary(year, month);

    terminal.writer().println("-----------------");
    terminal.writer().println("| Lead Billings |");
    terminal.writer().println("-----------------");
    terminal.writer().println("SUMMARY, Y:" + year + ", M:" + month);

    terminal.writer().println("- Quantities");
    summary.quantities().forEach((key, value) -> {
      terminal.writer().println("-- " + key + ": " + value);
    });

    terminal.writer().println("- Prices");
    summary.prices().forEach((key, value) -> {
      terminal.writer().println("-- " + key + ": " + value);
    });

    terminal.writer().println("- Detailed");
    summary.statusPriceQuanties().forEach((status, priceQuantity) -> {
      terminal.writer().println("-- " + status);
      priceQuantity
          .entrySet()
          .stream()
          .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
          .toList()
          .forEach(entry -> {
            terminal.writer().println("--- p:" + entry.getKey() + ", q:" + entry.getValue());
          });

    });

    terminal.flush();
  }

  @Command(command = "billing-per-company", description = "Shows lead billing summary based on comapnies for specific month")
  void perCompany(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var summary = getBillingspBillingsPerCompanyQuery.getPerCompany(year, month);

    terminal.writer().println("-----------------");
    terminal.writer().println("| Lead Billings |");
    terminal.writer().println("-----------------");
    terminal.writer().println("SUMMARY, Y:" + year + ", M:" + month);

    summary.companies().forEach(comp -> {
      terminal.writer().println("- Name              : " + comp.name());
      terminal.writer().println("-- Display name     : " + comp.dispalyName());
      terminal.writer().println("-- ID               : " + comp.id());
      terminal.writer().println("-- Auth0ID          : " + comp.auth0Id());
      // Qty per status
      var showQtyLegend = true;
      for (Map.Entry<String, Integer> entry : comp.quantityPerStatus().entrySet()) {
        if (showQtyLegend) {
          terminal.writer().println("-- Qty per status   : " + entry.getKey() + ": " + entry.getValue());
          showQtyLegend = false;
        } else {
          terminal.writer().println("--                  : " + entry.getKey() + ": " + entry.getValue());
        }
      }
      
      // Price
      var showPriceLegend = true;
      for (Map.Entry<String, Float> entry : comp.prices().entrySet()) {
        if (showPriceLegend) {
          terminal.writer().println("-- Prices           : " + entry.getKey() + ": " + entry.getValue());
          showPriceLegend = false;
        } else {
          terminal.writer().println("--                  : " + entry.getKey() + ": " + entry.getValue());
        }
      }
      
      // Qty pre price ans status
      var showPriceStatusLegend = true;
      for (Map.Entry<String, Map<Float, Integer>> entry : comp.statusPriceQuanties().entrySet()) {
        if (showPriceStatusLegend) {
          terminal.writer().println("-- Qty status/price : " + entry.getKey());
          showPriceStatusLegend = false;
        } else {
          terminal.writer().println("--                  : " + entry.getKey());
        }

        entry.getValue().forEach((price, qty) -> {
          terminal.writer().println("--                  : p: " + price + ", q: " + qty);
        });
      }

      terminal.writer().println("---------");
    });

    terminal.flush();
  }
}
