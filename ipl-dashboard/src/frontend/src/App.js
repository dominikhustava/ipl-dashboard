import './App.css';
import {BrowserRouter as Router} from 'react-router-dom';
import { TeamPage } from './pages/TeamPage';

function App() {
  return (
    <div className="App">
      <Router>
        <TeamPage />
      </Router>
    </div>
  );
}

export default App;
