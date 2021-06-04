import { ILesson } from 'app/entities/lesson/lesson.model';
import { IUser } from '../user/user.model';

export interface ICourse {
  id?: number;
  name?: string;
  description?: string;
  image?: string;
  link?: string;
  lessons?: ILesson[] | null;
  instructor?: IUser | null;
  students?: IUser[] | null;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public image?: string,
    public link?: string,
    public lessons?: ILesson[] | null,
    public instructor?: IUser | null,
    public students?: IUser[] | null
  ) {}
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
