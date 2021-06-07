import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
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
        path: 'instructor-panel',
        data: { pageTitle: 'Instructor Panel' },
        loadChildren: () => import('./instructor/instructor.module').then(m => m.InstructorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
