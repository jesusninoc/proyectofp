import { ILesson } from 'app/entities/lesson/lesson.model';
import { IInstructor } from 'app/entities/instructor/instructor.model';
import { IStudent } from 'app/entities/student/student.model';

export interface ICourse {
  id?: number;
  name?: string;
  description?: string;
  image?: string;
  lessons?: ILesson[] | null;
  instructor?: IInstructor | null;
  students?: IStudent[] | null;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public image?: string,
    public lessons?: ILesson[] | null,
    public instructor?: IInstructor | null,
    public students?: IStudent[] | null
  ) {}
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
