package io.hustava.ipldashboard.repository;

import io.hustava.ipldashboard.model.Match;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {
    List<Match> getByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2);
}
