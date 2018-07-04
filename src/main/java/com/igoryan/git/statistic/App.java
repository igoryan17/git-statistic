package com.igoryan.git.statistic;

import com.igoryan.git.statistic.aggregator.StatisticAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(@Autowired StatisticAggregator statisticAggregator) {
    return args -> System.out
        .println(statisticAggregator.aggregate("https://github.com/centic9/jgit-cookbook.git"));
  }
}
