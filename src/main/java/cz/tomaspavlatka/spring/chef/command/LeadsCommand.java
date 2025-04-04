package cz.tomaspavlatka.spring.chef.command;

import java.util.HashMap;
import java.util.Map;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;
import cz.tomaspavlatka.spring.chef.lead.model.PeriodReport;
import cz.tomaspavlatka.spring.chef.lead.usecase.query.CompaniesListQuery;

@Command(group = "Leads", command = "leads")
public class LeadsCommand {
  @Autowired
  private Terminal terminal;

  private final LeadEngineFacade leadEngineFacade;
  private final CompaniesListQuery companiesListQuery;

  public LeadsCommand(
      LeadEngineFacade leadEngineFacade,
      CompaniesListQuery companiesListQuery) {
    this.leadEngineFacade = leadEngineFacade;
    this.companiesListQuery = companiesListQuery;
  }

  @Command(command = "companies", description = "Shows list of companies with billing info for specific month")
  void companies(@Option(required = true) Integer year, @Option(required = true) Integer month) {
    var companies = companiesListQuery.getCompanies(year, month);

    terminal.writer().println("Companies, Y:" + year + ", M:" + month);
    terminal.writer().println("===========================");

    companies.stream().forEach(comp -> {
      terminal.writer().println("- " + comp.auth0Id());
    });

    terminal.flush();
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
