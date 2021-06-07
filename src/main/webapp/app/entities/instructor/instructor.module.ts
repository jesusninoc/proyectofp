import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ListCoursesComponent } from './list/list-courses.component';
import { DetailComponent } from './detail/detail.component';
import { UpdateComponent } from './update/update.component';
import { DeleteComponent } from './delete/delete.component';
import { InstructorRoutingModule } from './route/instructor-routing.module';

@NgModule({
  imports: [SharedModule, InstructorRoutingModule],
  declarations: [ListCoursesComponent, DetailComponent, UpdateComponent, DeleteComponent],
  entryComponents: [DeleteComponent],
})
export class InstructorModule {}
