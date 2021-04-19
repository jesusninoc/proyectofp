jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdmin, Admin } from '../admin.model';
import { AdminService } from '../service/admin.service';

import { AdminRoutingResolveService } from './admin-routing-resolve.service';

describe('Service Tests', () => {
  describe('Admin routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdminRoutingResolveService;
    let service: AdminService;
    let resultAdmin: IAdmin | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdminRoutingResolveService);
      service = TestBed.inject(AdminService);
      resultAdmin = undefined;
    });

    describe('resolve', () => {
      it('should return IAdmin returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdmin = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdmin).toEqual({ id: 123 });
      });

      it('should return new IAdmin if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdmin = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdmin).toEqual(new Admin());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdmin = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdmin).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
