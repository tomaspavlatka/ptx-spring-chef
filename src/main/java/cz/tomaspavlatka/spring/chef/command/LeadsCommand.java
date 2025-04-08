package cz.tomaspavlatka.spring.chef.command;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.usecase.query.BillingsCompaniesQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.BillingsSummaryQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetCurrentPayableLeadsQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetLegacyPayableLeadsQuery;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetPayableLeadsQuery;

@Command(group = "Leads", command = "leads")
public class LeadsCommand {
  @Autowired
  private Terminal terminal;

  private final BillingsSummaryQuery billingsSummaryQuery;
  private final BillingsCompaniesQuery billingsCompaniesQuery;
  private final GetLegacyPayableLeadsQuery getLegacyPayableLeadsQuery;
  private final GetCurrentPayableLeadsQuery getCurrentPayableLeadsQuery;

  public LeadsCommand(
      BillingsSummaryQuery billingsSummaryQuery,
      GetLegacyPayableLeadsQuery getLegacyPayableLeadsQuery,
      GetCurrentPayableLeadsQuery getCurrentPayableLeadsQuery,
      BillingsCompaniesQuery billingsCompaniesQuery) {
    this.billingsSummaryQuery = billingsSummaryQuery;
    this.getCurrentPayableLeadsQuery = getCurrentPayableLeadsQuery;
    this.getLegacyPayableLeadsQuery = getLegacyPayableLeadsQuery;
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

  @Command(command = "payable-leads", description = "Get payable leads for specific month")
  void payableLeads(@Option(required = true) Integer month) throws IOException {
    var leads = getCurrentPayableLeadsQuery.getPayableLeads(month);

    terminal.writer().println("Payable Leads, M:" + month);
    terminal.writer().println("===========================");

    leads.forEach((key, value) -> {
      terminal.writer().println("- Company ID: " + key);
      value.forEach(lead -> {
        terminal.writer().println("-- " + lead.getOfferStatus() + ": " + lead.getPrice());
      });
    });

    terminal.flush();
  }

  @Command(command = "legacy-payable-leads", description = "Get legacy payable leads for specific month")
  void legacyPayableLeads(@Option(required = true) Integer month) throws IOException {
    var leads = getLegacyPayableLeadsQuery.getPayableLeads(month);

    terminal.writer().println("Legacy Payable Leads, M:" + month);
    terminal.writer().println("===========================");

    leads.forEach((key, value) -> {
      terminal.writer().println("- Company ID:" + key);
      value.forEach(lead -> {
        terminal.writer().println("-- " + lead.getHistoryStatus() + ": " + lead.getPrice());
      });
    });

    terminal.flush();
  }
}
