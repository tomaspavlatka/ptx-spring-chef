package cz.tomaspavlatka.spring.chef.command;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.usecase.query.GetBillingsSummaryQuery;

@Command(group = "Billings", command = "billings")
public class BillingsCommand {
  @Autowired
  private Terminal terminal;

  private final GetBillingsSummaryQuery getBillingsSummaryQuery;

  public BillingsCommand(
      GetBillingsSummaryQuery getBillingsSummaryQuery) {
    this.getBillingsSummaryQuery = getBillingsSummaryQuery;
  }

  @Command(command = "summary", description = "Shows summary for specific month")
  void summary(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var summary = getBillingsSummaryQuery.getSummary(year, month);

    terminal.writer().println("Billings Summary, Y:" + year + ", M:" + month);
    terminal.writer().println("===============================");

    terminal.writer().println("- Quantities");
    summary.quantities().forEach((key, value) -> {
      terminal.writer().println("-- " + key + ": " + value);
    });

    terminal.writer().println("- Prices");
    summary.prices().forEach((key, value) -> {
      terminal.writer().println("-- " + key + ": " + value);
    });

    terminal.flush();
  }
}
