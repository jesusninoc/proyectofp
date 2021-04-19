import { ICourse } from 'app/entities/course/course.model';

export interface ILesson {
  id?: number;
  name?: string;
  description?: string;
  link?: string;
  course?: ICourse | null;
}

export class Lesson implements ILesson {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public link?: string,
    public course?: ICourse | null
  ) {}
}

export function getLessonIdentifier(lesson: ILesson): number | undefined {
  return lesson.id;
}
