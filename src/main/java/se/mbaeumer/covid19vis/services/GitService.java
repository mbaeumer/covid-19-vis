package se.mbaeumer.covid19vis.services;

import javafx.scene.control.Label;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class GitService {

    private Git git;

    private CloneCommand cloneCommand;

    public static final String DATA_PATH = "/csse_covid_19_data/csse_covid_19_daily_reports/";

    public void cloneRepository(final ConfigService configService, Label label) throws GitAPIException {
        cloneCommand = Git.cloneRepository().setURI("https://github.com/CSSEGISandData/COVID-19")
                .setDirectory(new File(configService.getBaseDataFolder()));
        Git git = cloneCommand
                .setProgressMonitor(new CustomMonitor(label))
                .call();
    }

    public void pull(final ConfigService configService) throws IOException, GitAPIException {
        git = Git.open(new File(configService.getBaseDataFolder()));
        git.pull().call();
    }
}
