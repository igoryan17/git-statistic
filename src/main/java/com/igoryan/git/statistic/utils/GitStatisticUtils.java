package com.igoryan.git.statistic.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitStatisticUtils {

  private static final String TEMP_DIR_PREFIX = "temp";
  private static final Calendar SIX_MONTH_AGO;

  static {
    SIX_MONTH_AGO = Calendar.getInstance();
    SIX_MONTH_AGO.add(Calendar.MONTH, -3);
  }

  private GitStatisticUtils() {
  }

  public static Git cloneRemoteRepository(String url) throws IOException, GitAPIException {
    Path dir = Files.createTempDirectory(TEMP_DIR_PREFIX);
    try (Git result = Git.cloneRepository()
        .setURI(url)
        .setDirectory(dir.toFile())
        .call()
    ) {
      return result;
    }
  }

  public static List<RevCommit> aggregateCommits(Git git) throws GitAPIException, IOException {
    return StreamSupport.stream(git.log().all().call().spliterator(), false)
        .filter(revCommit -> {
          PersonIdent personIdent = revCommit.getAuthorIdent();
          Date date = personIdent.getWhen();
          Calendar calendar = Calendar.getInstance(personIdent.getTimeZone());
          calendar.setTime(date);
          SIX_MONTH_AGO.setTimeZone(personIdent.getTimeZone());
          return calendar.after(SIX_MONTH_AGO) && (
              calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                  || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
        })
        .collect(Collectors.toList());
  }

  public static Map<String, Integer> collectAuthors(List<RevCommit> commits) {
    Map<String, Integer> result = new HashMap<>();
    commits.forEach(revCommit -> {
      String commiterName = revCommit.getAuthorIdent().getName();
      result.compute(commiterName, (name, value) -> {
        if (value == null) {
          return 1;
        } else {
          return value + 1;
        }
      });
    });
    return result;
  }

  public static List<String> getSortedCommiters(Map<String, Integer> collected) {
    return collected.entrySet().stream()
        .sorted(Comparator.comparing(Entry::getValue))
        .map(Entry::getKey)
        .collect(Collectors.toList());
  }
}
