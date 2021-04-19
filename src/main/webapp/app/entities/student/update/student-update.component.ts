import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;

  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [],
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    name: [null, [Validators.required]],
    lastname: [null, [Validators.required]],
    image: [],
    courses: [],
  });

  constructor(
    protected studentService: StudentService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  getSelectedCourse(option: ICourse, selectedVals?: ICourse[]): ICourse {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      username: student.username,
      password: student.password,
      name: student.name,
      lastname: student.lastname,
      image: student.image,
      courses: student.courses,
    });

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(
      this.coursesSharedCollection,
      ...(student.courses ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourse[]) =>
          this.courseService.addCourseToCollectionIfMissing(courses, ...(this.editForm.get('courses')!.value ?? []))
        )
      )
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
      image: this.editForm.get(['image'])!.value,
      courses: this.editForm.get(['courses'])!.value,
    };
  }
}
