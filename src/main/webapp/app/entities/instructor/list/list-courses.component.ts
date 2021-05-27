import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-list-courses',
  templateUrl: './list-courses.component.html',
  styleUrls: ['./list-courses.component.scss'],
})
export class ListCoursesComponent implements OnInit {
  // constructor() { }

  ngOnInit(): void {
    console.log('hola list');
  }
}
