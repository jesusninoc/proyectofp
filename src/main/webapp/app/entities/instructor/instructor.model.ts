import { ICourse } from 'app/entities/course/course.model';

export interface IInstructor {
  id?: number;
  username?: string;
  password?: string;
  name?: string;
  lastname?: string;
  image?: string | null;
  courses?: ICourse[] | null;
}

export class Instructor implements IInstructor {
  constructor(
    public id?: number,
    public username?: string,
    public password?: string,
    public name?: string,
    public lastname?: string,
    public image?: string | null,
    public courses?: ICourse[] | null
  ) {}
}

export function getInstructorIdentifier(instructor: IInstructor): number | undefined {
  return instructor.id;
}
