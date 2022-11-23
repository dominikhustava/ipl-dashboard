package io.hustava.ipldashboard.data;

import io.hustava.ipldashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager entityManager;

    @Autowired
    public JobCompletionNotificationListener(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("!!! JOB FINISHED! Time to verify the results");
        }

        //storage for teamnames to Team instances with populated total matches
        Map<String, Team> teamData = new HashMap<>();

        //select distinct team1 from Match m union select distinct team2 from Match m - union does not work in JPA(em)
        List<Object[]> results = entityManager.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
                .getResultList();

        results.stream()
                .map(e -> new Team((String) e[0], (long) e[1]))
                .forEach(team -> teamData.put(team.getTeamName(), team));

        entityManager.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
                .getResultList()
                .stream()
                .forEach(e -> {
                    Team team = teamData.get((String) e[0]);
                    team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
                });

        entityManager.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
                .getResultList()
                .stream()
                .forEach(e -> {
                    Team team = teamData.get((String) e[0]);
                    team.setTotalWins((long) e[1]);
                });

        teamData.values().forEach(team -> {entityManager.persist(team);});
        teamData.values().forEach(team -> System.out.println(team));

    }

}
