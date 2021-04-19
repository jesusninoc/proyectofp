import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdmin, Admin } from '../admin.model';
import { AdminService } from '../service/admin.service';

@Component({
  selector: 'jhi-admin-update',
  templateUrl: './admin-update.component.html',
})
export class AdminUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    name: [null, [Validators.required]],
    lastname: [null, [Validators.required]],
  });

  constructor(protected adminService: AdminService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ admin }) => {
      this.updateForm(admin);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const admin = this.createFromForm();
    if (admin.id !== undefined) {
      this.subscribeToSaveResponse(this.adminService.update(admin));
    } else {
      this.subscribeToSaveResponse(this.adminService.create(admin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdmin>>): void {
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

  protected updateForm(admin: IAdmin): void {
    this.editForm.patchValue({
      id: admin.id,
      username: admin.username,
      password: admin.password,
      name: admin.name,
      lastname: admin.lastname,
    });
  }

  protected createFromForm(): IAdmin {
    return {
      ...new Admin(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
    };
  }
}
