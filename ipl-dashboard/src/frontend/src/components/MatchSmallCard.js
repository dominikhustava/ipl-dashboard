import { React } from 'react'

export const MatchSmallCard = ({teamName, match}) => {
  const otherTeam = match.team1 === teamName ? match.team2 : match.team1;
  return (
    <div className="MatchSmallCard">
      <p>vs {otherTeam}</p>
    </div>
  );
}