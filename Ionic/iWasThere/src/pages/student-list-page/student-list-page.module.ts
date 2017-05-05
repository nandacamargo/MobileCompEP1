import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StudentListPage } from './student-list-page';

@NgModule({
  declarations: [
    StudentListPage,
  ],
  imports: [
    IonicPageModule.forChild(StudentListPage),
  ],
  exports: [
    StudentListPage
  ]
})
export class StudentListPageModule {}
