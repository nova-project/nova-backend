package net.getnova.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

@Slf4j
class NovaCli {

    private final Options options;
    private CommandLine cmd;

    NovaCli(final String[] args) {
        this.options = this.generateOptions();
        try {
            this.cmd = new DefaultParser().parse(this.options, args);
        } catch (UnrecognizedOptionException e) {
            log.warn(e.getMessage());
        } catch (ParseException e) {
            log.error("Unable to parse commandline arguments.", e);
        }
    }

    private Options generateOptions() {
        final Options options = new Options();
        options.addOption("e", "env", false, "Disable the configfile"
                + " and reads the values from the environment variables.");
        options.addOption("h", "help", false, "prints this message");
        options.addOption("s", "service", true, "Class name to the service class.");
        return options;
    }

    boolean isHelp() {
        if (this.cmd != null && this.cmd.hasOption("h")) {
            new HelpFormatter().printHelp("./bin/nova-bootstrap", this.options);
            return true;
        } else return false;
    }

    boolean useEnvironment() {
        return this.cmd != null && this.cmd.hasOption("e");
    }

    String[] enabledServices() {
        if (this.cmd != null && this.cmd.hasOption("s")) return this.cmd.getOptionValues("s");
        else return new String[0];
    }
}
