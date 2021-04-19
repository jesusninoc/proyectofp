import { ICourse } from 'app/entities/course/course.model';

export interface IStudent {
  id?: number;
  username?: string;
  password?: string;
  name?: string;
  lastname?: string;
  image?: string | null;
  courses?: ICourse[] | null;
}

export class Student implements IStudent {
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

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
