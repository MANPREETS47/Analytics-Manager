import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { emitEvent } from './utils/eventlogger';

const LandingPage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    emitEvent('LANDING_PAGE_VIEW');
  }, []);

  const handleBuyCourseClick = () => {
    emitEvent('BUY_COURSE_CLICKED');
    navigate('/course');
  };

  return (
    <div className="page">
      <div className="card">
        <h1>Welcome to our Course Platform</h1>
        <p>Learn new skills with industry-ready courses.</p>
        <button className="btn-primary" onClick={handleBuyCourseClick}>
          Buy a course
        </button>
      </div>
    </div>
  );
};

export default LandingPage;