package se.mbaeumer.covid19vis.services;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

public class GitService {

    public static final String LOCAL_PATH = "/Users/martinbaumer/proj/gitrepo/covid-test-dir";
    public static final String DATA_PATH = "/csse_covid_19_data/csse_covid_19_daily_reports/";

    public void cloneRepository() throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI("https://github.com/CSSEGISandData/COVID-19")
                .setDirectory(new File(LOCAL_PATH))
                .call();

    }

    public void pull() throws IOException, GitAPIException {
        Git git = Git.open(new File(LOCAL_PATH));
        git.pull().call();
    }
}
