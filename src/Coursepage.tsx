import { useEffect } from 'react';
import { emitEvent } from './utils/eventlogger';
import './styles/main.css';

const CoursePage = () => {

  useEffect(() => {
    emitEvent('COURSE_PAGE_VIEW');
  }, []);

  return (
    <div className="page">
      <div className="card">
        <h1>Course Page</h1>
        <p>You are viewing a course. Start learning now!</p>
      </div>
    </div>
  );
};

export default CoursePage;