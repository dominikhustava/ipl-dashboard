import { React, useEffect, useState } from 'react'
import { MatchDetailCard } from '../components/MatchDetailCard'
import { MatchSmallCard } from '../components/MatchSmallCard'

export const TeamPage = () => {

  const [team, setTeam] = useState({});

  useEffect(
    () => {
        const fetchMatches = async () => {
            const response = await fetch('http://localhost:8080/team/Kings%20XI%20Punjab');
            const data = await response.json();
            console.log(data);
            setTeam(data);
        };
        fetchMatches();
    },[]
  );

  return (
    <div className="TeamPage">
      <h1>{team.teamName}</h1>
      <MatchDetailCard />
      {team.matches.map(match => <MatchSmallCard match={match} />)}
    </div>
  );
}