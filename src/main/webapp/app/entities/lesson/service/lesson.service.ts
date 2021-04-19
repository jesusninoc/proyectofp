import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILesson, getLessonIdentifier } from '../lesson.model';

export type EntityResponseType = HttpResponse<ILesson>;
export type EntityArrayResponseType = HttpResponse<ILesson[]>;

@Injectable({ providedIn: 'root' })
export class LessonService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/lessons');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(lesson: ILesson): Observable<EntityResponseType> {
    return this.http.post<ILesson>(this.resourceUrl, lesson, { observe: 'response' });
  }

  update(lesson: ILesson): Observable<EntityResponseType> {
    return this.http.put<ILesson>(`${this.resourceUrl}/${getLessonIdentifier(lesson) as number}`, lesson, { observe: 'response' });
  }

  partialUpdate(lesson: ILesson): Observable<EntityResponseType> {
    return this.http.patch<ILesson>(`${this.resourceUrl}/${getLessonIdentifier(lesson) as number}`, lesson, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILesson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILesson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLessonToCollectionIfMissing(lessonCollection: ILesson[], ...lessonsToCheck: (ILesson | null | undefined)[]): ILesson[] {
    const lessons: ILesson[] = lessonsToCheck.filter(isPresent);
    if (lessons.length > 0) {
      const lessonCollectionIdentifiers = lessonCollection.map(lessonItem => getLessonIdentifier(lessonItem)!);
      const lessonsToAdd = lessons.filter(lessonItem => {
        const lessonIdentifier = getLessonIdentifier(lessonItem);
        if (lessonIdentifier == null || lessonCollectionIdentifiers.includes(lessonIdentifier)) {
          return false;
        }
        lessonCollectionIdentifiers.push(lessonIdentifier);
        return true;
      });
      return [...lessonsToAdd, ...lessonCollection];
    }
    return lessonCollection;
  }
}
