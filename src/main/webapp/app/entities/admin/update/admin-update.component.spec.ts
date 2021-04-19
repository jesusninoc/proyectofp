jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AdminService } from '../service/admin.service';
import { IAdmin, Admin } from '../admin.model';

import { AdminUpdateComponent } from './admin-update.component';

describe('Component Tests', () => {
  describe('Admin Management Update Component', () => {
    let comp: AdminUpdateComponent;
    let fixture: ComponentFixture<AdminUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let adminService: AdminService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdminUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AdminUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdminUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      adminService = TestBed.inject(AdminService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const admin: IAdmin = { id: 456 };

        activatedRoute.data = of({ admin });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(admin));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const admin = { id: 123 };
        spyOn(adminService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ admin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: admin }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(adminService.update).toHaveBeenCalledWith(admin);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const admin = new Admin();
        spyOn(adminService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ admin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: admin }));
        saveSubject.complete();

        // THEN
        expect(adminService.create).toHaveBeenCalledWith(admin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const admin = { id: 123 };
        spyOn(adminService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ admin });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(adminService.update).toHaveBeenCalledWith(admin);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
