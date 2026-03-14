import { BrowserRouter, Routes, Route } from 'react-router-dom';
import LandingPage from './Landingpage';
import CoursePage from './Coursepage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/course" element={<CoursePage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;