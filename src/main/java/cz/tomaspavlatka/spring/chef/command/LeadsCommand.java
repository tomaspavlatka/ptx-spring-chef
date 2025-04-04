package cz.tomaspavlatka.spring.chef.command;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.usecase.query.BillingsSummaryQuery;

@Command(group = "Leads", command = "leads")
public class LeadsCommand {
  @Autowired
  private Terminal terminal;

  private final BillingsSummaryQuery billingsSummaryQuery;

  public LeadsCommand(BillingsSummaryQuery billingsSummaryQuery) {
    this.billingsSummaryQuery = billingsSummaryQuery;
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
