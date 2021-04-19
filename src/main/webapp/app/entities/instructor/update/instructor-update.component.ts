import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IInstructor, Instructor } from '../instructor.model';
import { InstructorService } from '../service/instructor.service';

@Component({
  selector: 'jhi-instructor-update',
  templateUrl: './instructor-update.component.html',
})
export class InstructorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    name: [null, [Validators.required]],
    lastname: [null, [Validators.required]],
    image: [],
  });

  constructor(protected instructorService: InstructorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ instructor }) => {
      this.updateForm(instructor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const instructor = this.createFromForm();
    if (instructor.id !== undefined) {
      this.subscribeToSaveResponse(this.instructorService.update(instructor));
    } else {
      this.subscribeToSaveResponse(this.instructorService.create(instructor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstructor>>): void {
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

  protected updateForm(instructor: IInstructor): void {
    this.editForm.patchValue({
      id: instructor.id,
      username: instructor.username,
      password: instructor.password,
      name: instructor.name,
      lastname: instructor.lastname,
      image: instructor.image,
    });
  }

  protected createFromForm(): IInstructor {
    return {
      ...new Instructor(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
      image: this.editForm.get(['image'])!.value,
    };
  }
}
