package cz.tomaspavlatka.spring.chef.command;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.usecase.query.BillingsCompaniesQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.BillingsSummaryQuery;

@Command(group = "Leads", command = "leads")
public class LeadsCommand {
  @Autowired
  private Terminal terminal;

  private final BillingsSummaryQuery billingsSummaryQuery;
  private final BillingsCompaniesQuery billingsCompaniesQuery;

  public LeadsCommand(
      BillingsSummaryQuery billingsSummaryQuery,
      BillingsCompaniesQuery billingsCompaniesQuery) {
    this.billingsSummaryQuery = billingsSummaryQuery;
    this.billingsCompaniesQuery = billingsCompaniesQuery;
  }

  @Command(command = "companies", description = "Lists companies with at least 1 billings item for specific month")
  void companies(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var companies = billingsCompaniesQuery.getCompanies(year, month);

    terminal.writer().println("Leads Companies, Y:" + year + ", M:" + month);
    terminal.writer().println("=============================");

    companies.forEach(comp -> {
      terminal.writer().println("Company : " + comp.company().displayName().orElse(""));
      terminal.writer().println("Name    : " + comp.company().name().orElse(""));
      terminal.writer().println("ID      : " + comp.company().id());
      terminal.writer().println("Auth0ID : " + comp.company().auth0Id());

      comp.summary().forEach((k, v) -> {
        terminal.writer().println("- " + v + " x " + k);
      });

      terminal.writer().println("-------- ");
    });

    terminal.flush();
  }

  @Command(command = "summary", description = "Shows summary for specific month")
  void report(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var summary = billingsSummaryQuery.getSummary(year, month);

    terminal.writer().println("Leads Summary, Y:" + year + ", M:" + month);
    terminal.writer().println("===========================");

    summary.forEach((key, value) -> {
      terminal.writer().println("- " + key + ": " + value);
    });

    terminal.flush();
  }
}
