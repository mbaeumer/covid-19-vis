package se.mbaeumer.covid19vis.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class GitService {

    public static final String DATA_PATH = "/csse_covid_19_data/csse_covid_19_daily_reports/";

    public void cloneRepository(final ConfigService configService) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI("https://github.com/CSSEGISandData/COVID-19")
                .setDirectory(new File(configService.getBaseDataFolder()))
                .call();

    }

    public void pull(final ConfigService configService) throws IOException, GitAPIException {
        Git git = Git.open(new File(configService.getBaseDataFolder()));
        git.pull().call();
    }
}
