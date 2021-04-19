jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CourseService } from '../service/course.service';
import { ICourse, Course } from '../course.model';
import { IInstructor } from 'app/entities/instructor/instructor.model';
import { InstructorService } from 'app/entities/instructor/service/instructor.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Component Tests', () => {
  describe('Course Management Update Component', () => {
    let comp: CourseUpdateComponent;
    let fixture: ComponentFixture<CourseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let courseService: CourseService;
    let instructorService: InstructorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CourseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CourseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      courseService = TestBed.inject(CourseService);
      instructorService = TestBed.inject(InstructorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Instructor query and add missing value', () => {
        const course: ICourse = { id: 456 };
        const instructor: IInstructor = { id: 70166 };
        course.instructor = instructor;

        const instructorCollection: IInstructor[] = [{ id: 42237 }];
        spyOn(instructorService, 'query').and.returnValue(of(new HttpResponse({ body: instructorCollection })));
        const additionalInstructors = [instructor];
        const expectedCollection: IInstructor[] = [...additionalInstructors, ...instructorCollection];
        spyOn(instructorService, 'addInstructorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ course });
        comp.ngOnInit();

        expect(instructorService.query).toHaveBeenCalled();
        expect(instructorService.addInstructorToCollectionIfMissing).toHaveBeenCalledWith(instructorCollection, ...additionalInstructors);
        expect(comp.instructorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const course: ICourse = { id: 456 };
        const instructor: IInstructor = { id: 11018 };
        course.instructor = instructor;

        activatedRoute.data = of({ course });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(course));
        expect(comp.instructorsSharedCollection).toContain(instructor);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const course = { id: 123 };
        spyOn(courseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ course });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: course }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(courseService.update).toHaveBeenCalledWith(course);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const course = new Course();
        spyOn(courseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ course });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: course }));
        saveSubject.complete();

        // THEN
        expect(courseService.create).toHaveBeenCalledWith(course);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const course = { id: 123 };
        spyOn(courseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ course });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(courseService.update).toHaveBeenCalledWith(course);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackInstructorById', () => {
        it('Should return tracked Instructor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackInstructorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
