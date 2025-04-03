package cz.tomaspavlatka.spring.chef.easybill;

import org.springframework.shell.command.annotation.Command;

@Command
public class EasybillCommand {
    @Command(command = "easybill")
    String hello() {
        return "world";
    }
}
