import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InstructorComponent } from '../list/instructor.component';
import { InstructorDetailComponent } from '../detail/instructor-detail.component';
import { InstructorUpdateComponent } from '../update/instructor-update.component';
import { InstructorRoutingResolveService } from './instructor-routing-resolve.service';

import { Authority } from 'app/config/authority.constants';

const instructorRoute: Routes = [
  {
    path: '',
    component: InstructorComponent,
    data: {
      defaultSort: 'id,asc',
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InstructorDetailComponent,
    resolve: {
      instructor: InstructorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: 'new',
    component: InstructorUpdateComponent,
    resolve: {
      instructor: InstructorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: ':id/edit',
    component: InstructorUpdateComponent,
    resolve: {
      instructor: InstructorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(instructorRoute)],
  exports: [RouterModule],
})
export class InstructorRoutingModule {}
