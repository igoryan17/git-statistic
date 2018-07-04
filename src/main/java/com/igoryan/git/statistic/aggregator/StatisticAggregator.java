package com.igoryan.git.statistic.aggregator;

import java.io.IOException;
import java.util.List;
import org.eclipse.jgit.api.errors.GitAPIException;

public interface StatisticAggregator {

  List<String> aggregate(String repoUrl) throws IOException, GitAPIException;
}
