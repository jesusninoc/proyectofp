import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILesson, Lesson } from '../lesson.model';

import { LessonService } from './lesson.service';

describe('Service Tests', () => {
  describe('Lesson Service', () => {
    let service: LessonService;
    let httpMock: HttpTestingController;
    let elemDefault: ILesson;
    let expectedResult: ILesson | ILesson[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LessonService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        link: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Lesson', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Lesson()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Lesson', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            link: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Lesson', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          new Lesson()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Lesson', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            link: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Lesson', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLessonToCollectionIfMissing', () => {
        it('should add a Lesson to an empty array', () => {
          const lesson: ILesson = { id: 123 };
          expectedResult = service.addLessonToCollectionIfMissing([], lesson);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lesson);
        });

        it('should not add a Lesson to an array that contains it', () => {
          const lesson: ILesson = { id: 123 };
          const lessonCollection: ILesson[] = [
            {
              ...lesson,
            },
            { id: 456 },
          ];
          expectedResult = service.addLessonToCollectionIfMissing(lessonCollection, lesson);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Lesson to an array that doesn't contain it", () => {
          const lesson: ILesson = { id: 123 };
          const lessonCollection: ILesson[] = [{ id: 456 }];
          expectedResult = service.addLessonToCollectionIfMissing(lessonCollection, lesson);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lesson);
        });

        it('should add only unique Lesson to an array', () => {
          const lessonArray: ILesson[] = [{ id: 123 }, { id: 456 }, { id: 28506 }];
          const lessonCollection: ILesson[] = [{ id: 123 }];
          expectedResult = service.addLessonToCollectionIfMissing(lessonCollection, ...lessonArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lesson: ILesson = { id: 123 };
          const lesson2: ILesson = { id: 456 };
          expectedResult = service.addLessonToCollectionIfMissing([], lesson, lesson2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lesson);
          expect(expectedResult).toContain(lesson2);
        });

        it('should accept null and undefined values', () => {
          const lesson: ILesson = { id: 123 };
          expectedResult = service.addLessonToCollectionIfMissing([], null, lesson, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lesson);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
