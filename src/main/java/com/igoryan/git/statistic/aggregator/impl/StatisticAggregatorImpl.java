package com.igoryan.git.statistic.aggregator.impl;

import com.igoryan.git.statistic.aggregator.StatisticAggregator;
import com.igoryan.git.statistic.utils.GitStatisticUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

@Service
public class StatisticAggregatorImpl implements StatisticAggregator {

  @Override
  public List<String> aggregate(String repoUrl) throws IOException, GitAPIException {
    Git git = GitStatisticUtils.cloneRemoteRepository(repoUrl);
    List<RevCommit> aggregatedCommits = GitStatisticUtils.aggregateCommits(git);
    Map<String, Integer> collectedAuthors = GitStatisticUtils.collectAuthors(aggregatedCommits);
    return GitStatisticUtils.getSortedCommiters(collectedAuthors);
  }
}
