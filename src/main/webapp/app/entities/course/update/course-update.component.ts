import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map, retry } from 'rxjs/operators';

import { ICourse, Course } from '../course.model';
import { CourseService } from '../service/course.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ILesson } from 'app/entities/lesson/lesson.model';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;
  account: Account | null = null;
  instructor: IUser | null = null;

  instructorsSharedCollection: IUser[] = [];
  lesson = {
    error: false,
  };
  lessons: ILesson[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    image: [null, [Validators.required]],
    instructor: [null, [Validators.required]],
    // nameLesson: [],
    // descLesson: [],
    // videoLesson: [],
    linkCourse: [null, [Validators.required]],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected courseService: CourseService,
    protected instructorService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    this.activatedRoute.data.subscribe(({ course }) => {
      this.instructor = course.instructor;
      console.log(this.instructor?.login);
      this.updateForm(course);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('academiaonlineApp.error', { message: err.message })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.createFromForm();
    console.log(course);
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  trackInstructorById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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

  protected updateForm(course: ICourse): void {
    this.editForm.patchValue({
      id: course.id,
      name: course.name,
      description: course.description,
      image: course.image,
      instructor: course.instructor,
    });

    this.instructorsSharedCollection = this.instructorService.addUserToCollectionIfMissing(
      this.instructorsSharedCollection,
      course.instructor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.instructorService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((instructors: IUser[]) =>
          this.instructorService.addUserToCollectionIfMissing(instructors, this.editForm.get('instructor')!.value)
        )
      )
      .subscribe((instructors: IUser[]) => (this.instructorsSharedCollection = instructors));
  }

  protected createFromForm(): ICourse {
    this.instructor = {
      id: this.account?.id,
      login: this.account?.login,
    };
    return {
      ...new Course(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      image: this.editForm.get(['image'])!.value,
      instructor: this.instructor,
      link: this.editForm.get(['linkCourse'])!.value,
    };
  }
}
