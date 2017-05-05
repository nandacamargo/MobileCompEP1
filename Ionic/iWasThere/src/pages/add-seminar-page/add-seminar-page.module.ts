import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { AddSeminarPage } from './add-seminar-page';

@NgModule({
  declarations: [
    AddSeminarPage,
  ],
  imports: [
    IonicPageModule.forChild(AddSeminarPage),
  ],
  exports: [
    AddSeminarPage
  ]
})
export class AddSeminarPageModule {}
