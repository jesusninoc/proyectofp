import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LessonComponent } from '../list/lesson.component';
import { LessonDetailComponent } from '../detail/lesson-detail.component';
import { LessonUpdateComponent } from '../update/lesson-update.component';
import { LessonRoutingResolveService } from './lesson-routing-resolve.service';

import { Authority } from 'app/config/authority.constants';

const lessonRoute: Routes = [
  {
    path: '',
    component: LessonComponent,
    data: {
      defaultSort: 'id,asc',
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LessonDetailComponent,
    resolve: {
      lesson: LessonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: 'new',
    component: LessonUpdateComponent,
    resolve: {
      lesson: LessonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
  {
    path: ':id/edit',
    component: LessonUpdateComponent,
    resolve: {
      lesson: LessonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
    data: {
      authorities: [Authority.ADMIN],
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(lessonRoute)],
  exports: [RouterModule],
})
export class LessonRoutingModule {}
