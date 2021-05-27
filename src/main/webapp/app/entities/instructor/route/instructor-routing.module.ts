import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ListCoursesComponent } from '../list/list-courses.component';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetailComponent } from '../detail/detail.component';
import { CourseRoutingResolveService } from 'app/entities/course/route/course-routing-resolve.service';
import { UpdateComponent } from '../update/update.component';

const instructorRoute: Routes = [
  {
    path: '',
    component: ListCoursesComponent,
    data: {
      defaultSort: 'id,asc',
      authorities: [Authority.INSTRUCTOR],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetailComponent,
    resolve: {
      lesson: CourseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: 'new',
    component: UpdateComponent,
    resolve: {
      lesson: CourseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: ':id/edit',
    component: UpdateComponent,
    resolve: {
      lesson: CourseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(instructorRoute)],
  exports: [RouterModule],
})
export class InstructorRoutingModule {}
