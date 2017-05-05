import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SeminarListPage } from './seminar-list-page';

@NgModule({
  declarations: [
    SeminarListPage,
  ],
  imports: [
    IonicPageModule.forChild(SeminarListPage),
  ],
  exports: [
    SeminarListPage
  ]
})
export class SeminarListPageModule {}
