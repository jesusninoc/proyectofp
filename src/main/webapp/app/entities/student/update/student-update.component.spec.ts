jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentService } from '../service/student.service';
import { IStudent, Student } from '../student.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { StudentUpdateComponent } from './student-update.component';

describe('Component Tests', () => {
  describe('Student Management Update Component', () => {
    let comp: StudentUpdateComponent;
    let fixture: ComponentFixture<StudentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let studentService: StudentService;
    let courseService: CourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StudentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      studentService = TestBed.inject(StudentService);
      courseService = TestBed.inject(CourseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Course query and add missing value', () => {
        const student: IStudent = { id: 456 };
        const courses: ICourse[] = [{ id: 25860 }];
        student.courses = courses;

        const courseCollection: ICourse[] = [{ id: 38321 }];
        spyOn(courseService, 'query').and.returnValue(of(new HttpResponse({ body: courseCollection })));
        const additionalCourses = [...courses];
        const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
        spyOn(courseService, 'addCourseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(courseService.query).toHaveBeenCalled();
        expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
        expect(comp.coursesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const student: IStudent = { id: 456 };
        const courses: ICourse = { id: 38052 };
        student.courses = [courses];

        activatedRoute.data = of({ student });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(student));
        expect(comp.coursesSharedCollection).toContain(courses);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = { id: 123 };
        spyOn(studentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(studentService.update).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = new Student();
        spyOn(studentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: student }));
        saveSubject.complete();

        // THEN
        expect(studentService.create).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const student = { id: 123 };
        spyOn(studentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ student });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(studentService.update).toHaveBeenCalledWith(student);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCourseById', () => {
        it('Should return tracked Course primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedCourse', () => {
        it('Should return option if no Course is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCourse(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Course for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCourse(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Course is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCourse(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
