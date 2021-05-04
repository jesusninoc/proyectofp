import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudentComponent } from '../list/student.component';
import { StudentDetailComponent } from '../detail/student-detail.component';
import { StudentUpdateComponent } from '../update/student-update.component';
import { StudentRoutingResolveService } from './student-routing-resolve.service';

import { Authority } from 'app/config/authority.constants';

const studentRoute: Routes = [
  {
    path: '',
    component: StudentComponent,
    data: {
      defaultSort: 'id,asc',
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StudentDetailComponent,
    resolve: {
      student: StudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: 'new',
    component: StudentUpdateComponent,
    resolve: {
      student: StudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: ':id/edit',
    component: StudentUpdateComponent,
    resolve: {
      student: StudentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(studentRoute)],
  exports: [RouterModule],
})
export class StudentRoutingModule {}
