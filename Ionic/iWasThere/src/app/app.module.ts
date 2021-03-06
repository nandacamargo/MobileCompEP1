import { NgModule, ErrorHandler } from '@angular/core';
import { HttpModule } from '@angular/http';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';

import { LoginPage } from '../pages/login-page/login-page';
import { RegisterPage } from '../pages/register-page/register-page';
import { SeminarListPage } from '../pages/seminar-list-page/seminar-list-page'
import { AddSeminarPage } from '../pages/add-seminar-page/add-seminar-page'
import { EditProfilePage } from '../pages/edit-profile-page/edit-profile-page'
import { StudentListPage } from '../pages/student-list-page/student-list-page'
import { SendConfirmationPopover } from '../components/send-confirmation-popover/send-confirmation-popover'

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

@NgModule({
  declarations: [
    MyApp,
    LoginPage,
    RegisterPage,
    SeminarListPage,
    AddSeminarPage,
    EditProfilePage,
    StudentListPage,
    SendConfirmationPopover
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    HttpModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    LoginPage,
    RegisterPage,
    SeminarListPage,
    AddSeminarPage,
    EditProfilePage,
    StudentListPage,
    SendConfirmationPopover
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
  ]
})
export class AppModule {}
