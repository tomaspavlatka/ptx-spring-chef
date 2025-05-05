package cz.tomaspavlatka.spring.chef.command;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import cz.tomaspavlatka.spring.chef.company.usecase.query.GetMismatchedRelationsQuery;
import lombok.RequiredArgsConstructor;

@Command(group = "Easybill", command = "easybill")
@RequiredArgsConstructor
public class EasybillCommand {
  @Autowired
  private Terminal terminal;

  private final GetMismatchedRelationsQuery getMismatchedRelationsQuery;

  @Command(command = "mismatched-relations", description = "Shows mismatched relations on easybill")
  void summary(@Option() String relationFile) throws IOException {
    var mismatches = getMismatchedRelationsQuery.execute(relationFile);

    terminal.writer().println("-----------------------");
    terminal.writer().println("| Easybill Mismatches |");
    terminal.writer().println("-----------------------");

    mismatches.forEach(mismatch -> {
      terminal.writer().println("- ID          : " + mismatch.getCompanyId());
      terminal.writer().println("-- Number     : " + mismatch.getEasybillNumber());
      terminal.writer().println("-- Current ID : " + mismatch.getCurrentCustomerId());
      terminal.writer().println("-- Real ID    : " + mismatch.getRealCustomerId());
    });

    terminal.flush();
  }
}
