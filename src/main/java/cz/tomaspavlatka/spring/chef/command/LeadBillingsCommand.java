package cz.tomaspavlatka.spring.chef.command;

import java.io.IOException;
import java.util.Map;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetBillingsPerCompanyQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetBillingsSummaryQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetItemizedBillingsPerCompanyQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetMissingRelationsQuery;
import lombok.RequiredArgsConstructor;

@Command(group = "Leads", command = "leads")
@RequiredArgsConstructor
public class LeadBillingsCommand {
  @Autowired
  private Terminal terminal;

  private final GetBillingsSummaryQuery getBillingsSummaryQuery;
  private final GetBillingsPerCompanyQuery getBillingspBillingsPerCompanyQuery;
  private final GetMissingRelationsQuery getMissingRelationsQuery;
  private final GetItemizedBillingsPerCompanyQuery getItemizedBillingsPerCompanyQuery;

  @Command(command = "billing-summary", description = "Shows lead billing summary for specific month")
  void summary(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var summary = getBillingsSummaryQuery.execute(year, month);

    terminal.writer().println("-----------------");
    terminal.writer().println("| Lead Billings |");
    terminal.writer().println("-----------------");
    terminal.writer().println("SUMMARY, Y:" + year + ", M:" + month);

    // Qty per status
    var showQtyLegend = true;
    for (Map.Entry<String, Integer> entry : summary.quantities().entrySet()) {
      if (showQtyLegend) {
        terminal.writer().println("-- Qty per status   : " + entry.getKey() + ": " + entry.getValue());
        showQtyLegend = false;
      } else {
        terminal.writer().println("--                  : " + entry.getKey() + ": " + entry.getValue());
      }
    }

    // Price
    var showPriceLegend = true;
    for (Map.Entry<String, Float> entry : summary.prices().entrySet()) {
      if (showPriceLegend) {
        terminal.writer().println("-- Prices           : " + entry.getKey() + ": " + entry.getValue());
        showPriceLegend = false;
      } else {
        terminal.writer().println("--                  : " + entry.getKey() + ": " + entry.getValue());
      }
    }

    // Qty pre price ans status
    var showPriceStatusLegend = true;
    for (Map.Entry<String, Map<Float, Integer>> entry : summary.statusPriceQuanties().entrySet()) {
      if (showPriceStatusLegend) {
        terminal.writer().println("-- Qty status/price : " + entry.getKey());
        showPriceStatusLegend = false;
      } else {
        terminal.writer().println("--                  : " + entry.getKey());
      }

      entry.getValue().forEach((price, qty) -> {
        terminal.writer().println("--                  : - p: " + price + ", q: " + qty);
      });
    }

    terminal.flush();
  }

  @Command(command = "billing-per-company", description = "Shows lead billing summary based on comapnies for specific month")
  void perCompany(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var summary = getBillingspBillingsPerCompanyQuery.execute(year, month);

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
          terminal.writer().println("--                  : - p: " + price + ", q: " + qty);
        });
      }

      terminal.writer().println("---------");
    });

    terminal.flush();
  }

  @Command(
    command = "itemized-billing-per-company",
    description = "Shows itemised lead billing summary based on comapnies for specific month"
  )
  void itemizedPerCompany(
      @Option(required = true) Integer year,
      @Option(required = true) Integer month,
      @Option(required = false) String search) {
    var summary = getItemizedBillingsPerCompanyQuery.execute(year, month, search);

    terminal.writer().println("---------------------");
    terminal.writer().println("| Itemized Billings |");
    terminal.writer().println("---------------------");
    terminal.writer().println("SUMMARY, Y:" + year + ", M:" + month +", S:" + search);

    summary.companies().forEach(comp -> {
      terminal.writer().println("- Name              : " + comp.name());
      terminal.writer().println("-- Display name     : " + comp.dispalyName());
      terminal.writer().println("-- ID               : " + comp.id());
      terminal.writer().println("-- Auth0ID          : " + comp.auth0Id());

      comp.items().forEach(itm -> {
        var date = itm.createdAt().split("T")[0].split("-");
        terminal.writer().println("--- Anfrage: " + itm.id().split("-")[0] + " von " + date[2] + "." + date[1] + ". ("
            + itm.lastName() + "), PRICE: " + itm.price() + " EUR");
      });

      terminal.writer().println("date,id,status,lastName,price");
      comp.items().forEach(itm -> {
        terminal.writer().println(
            itm.createdAt().split("T")[0] + "," +
                itm.id() + "," +
                itm.status().toLowerCase() + ","
                + itm.lastName() + ","
                + itm.price());
      });

      terminal.writer().println("---------");
    });

    terminal.flush();
  }

  @Command(command = "missing-relations", description = "Shows missing relations from the specified period")
  void missingRelations(
      @Option(required = true) Integer year,
      @Option(required = true) Integer month,
      @Option(required = true) String relationsFile) throws IOException {
    var missing = getMissingRelationsQuery.execute(year, month, relationsFile);

    terminal.writer().println("---------------------");
    terminal.writer().println("| Missing Relations |");
    terminal.writer().println("---------------------");
    terminal.writer().println("SUMMARY, Y:" + year + ", M:" + month + ", C:" + missing.missingCompanies().size());

    missing.missingCompanies().forEach(comp -> {
      terminal.writer().println("- Name              : " + comp.name());
      terminal.writer().println("-- ID               : " + comp.id());
      terminal.writer().println("-- Auth0ID          : " + comp.auth0Id());
    });

    terminal.flush();
  }
}
