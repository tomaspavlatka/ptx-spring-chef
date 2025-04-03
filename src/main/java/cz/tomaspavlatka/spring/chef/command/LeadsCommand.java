package cz.tomaspavlatka.spring.chef.command;

import java.util.HashMap;
import java.util.Map;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.PeriodReport;

@Command(group = "Leads", command = "leads")
public class LeadsCommand {
  @Autowired
  private Terminal terminal;

  private final LeadEngineFacade leadEngineFacade;

  public LeadsCommand(LeadEngineFacade leadEngineFacade) {
    this.leadEngineFacade = leadEngineFacade;
  }

  @Command(command = "summary", description = "Shows summary for specific month")
  void report(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    PeriodReport response = leadEngineFacade.forPeriod(year, month);

    Map<String, Integer> summary = new HashMap<>();

    response.orgs().stream().forEach(org -> {
      org.items().stream().forEach(itm -> {
        int current = summary.containsKey(itm.status()) ? summary.get(itm.status()) : 0;
        summary.put(itm.status(), ++current);
      });
    });

    terminal.writer().println("Leads Summary, Y:" + year + ", M:" + month);
    terminal.writer().println("===========================");

    summary.forEach((key, value) -> {
      terminal.writer().println("- " + key + ": " + value);
    });

    terminal.flush();
  }
}
