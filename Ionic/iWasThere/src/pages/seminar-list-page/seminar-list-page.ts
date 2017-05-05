import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { LoginPage } from '../login-page/login-page'
import { AddSeminarPage } from '../add-seminar-page/add-seminar-page'
import { EditProfilePage } from '../edit-profile-page/edit-profile-page'
import { RegisterPage } from '../register-page/register-page'
import { StudentListPage } from '../student-list-page/student-list-page'

/**
 * Generated class for the SeminarListPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-seminar-list-page',
  templateUrl: 'seminar-list-page.html',
})
export class SeminarListPage {

  seminars: any

  addSeminarPage = AddSeminarPage
  editProfilePage = EditProfilePage
  registerPage = RegisterPage
  studentListPage = StudentListPage

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  	this.seminars = ["Seminar"]
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SeminarListPage');
  }

  logOut() {
  	this.navCtrl.setRoot(LoginPage)
  }

  openPage(page) {
  	this.navCtrl.push(page)
  }

}
