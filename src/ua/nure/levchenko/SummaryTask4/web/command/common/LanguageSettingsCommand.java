package ua.nure.levchenko.SummaryTask4.web.command.common;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LanguageSettingsCommand extends Command {

    private static final long serialVersionUID = 3398313222639147177L;

    private static final Logger LOG = Logger.getLogger(LanguageSettingsCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Command starts");
        LOG.debug("Command ends");
        return Path.PAGE_LANGUAGE_SETTINGS;
    }
}
