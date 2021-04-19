import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'admin',
        data: { pageTitle: 'Admins' },
        loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),
      },
      {
        path: 'instructor',
        data: { pageTitle: 'Instructors' },
        loadChildren: () => import('./instructor/instructor.module').then(m => m.InstructorModule),
      },
      {
        path: 'course',
        data: { pageTitle: 'Courses' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'lesson',
        data: { pageTitle: 'Lessons' },
        loadChildren: () => import('./lesson/lesson.module').then(m => m.LessonModule),
      },
      {
        path: 'student',
        data: { pageTitle: 'Students' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
