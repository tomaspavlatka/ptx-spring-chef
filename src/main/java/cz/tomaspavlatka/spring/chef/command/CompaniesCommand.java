package cz.tomaspavlatka.spring.chef.command;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.company.usecase.query.GetCurrentRelationsQuery;
import cz.tomaspavlatka.spring.chef.company.usecase.query.GetIncorrectRelationsQuery;
import cz.tomaspavlatka.spring.chef.lead.LeadEngineFacade;

@Command(group = "Companies", command = "companies")
public class CompaniesCommand {
  @Autowired
  private Terminal terminal;

  private final GetCurrentRelationsQuery getCurrentRelationsQuery;
  private final LeadEngineFacade leadEngineFacade;
  private final GetIncorrectRelationsQuery getIncorrectRelationsQuery;

  public CompaniesCommand(
      GetIncorrectRelationsQuery getIncorrectRelationsQuery,
      LeadEngineFacade leadEngineFacade,
      GetCurrentRelationsQuery getCurrentRelationsQuery) {
    this.getIncorrectRelationsQuery = getIncorrectRelationsQuery;
    this.leadEngineFacade = leadEngineFacade;
    this.getCurrentRelationsQuery = getCurrentRelationsQuery;
  }

  @Command(command = "incorrect", description = "Find incorrectly linked companies")
  void companies(@Option() String filename) throws IOException {
    var current = getCurrentRelationsQuery.getRelations(filename);

    var incorrect = getIncorrectRelationsQuery.getIncorrectRelation(current);

    var companies = leadEngineFacade.getCompanies();

    incorrect.forEach(inc -> {
      var company = companies.stream()
          .filter(c -> c.auth0Id() != null) // we have some companies without auth0Id
          .filter(c -> c.auth0Id().equals(inc.companyId()))
          .findFirst();

      terminal.writer().println("Incorrect");
      terminal.writer().println("- Company: " + company.get().name().get());
      terminal.writer().println("- Company ID: " + inc.companyId());
      terminal.writer().println("- Easybill Number: " + inc.easybillNumber());
      terminal.writer().println("- Current: " + inc.currentId());
      terminal.writer().println("- Correct: " + inc.correctId());
      terminal.writer().println();
    });

  }
}
