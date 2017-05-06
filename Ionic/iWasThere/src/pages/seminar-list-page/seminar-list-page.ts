import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { LoginPage } from '../login-page/login-page'
import { AddSeminarPage } from '../add-seminar-page/add-seminar-page'
import { EditProfilePage } from '../edit-profile-page/edit-profile-page'
import { RegisterPage } from '../register-page/register-page'
import { StudentListPage } from '../student-list-page/student-list-page'

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

import { UserSingleton } from '../../util/user-singleton.ts'
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

  filteredSeminars: any
  seminars: any
  results: any

  user: any;

  addSeminarPage = AddSeminarPage
  editProfilePage = EditProfilePage
  registerPage = RegisterPage
  studentListPage = StudentListPage

  constructor(public navCtrl: NavController, public navParams: NavParams, private http: Http) {
    this.user = new UserSingleton();
  }

  ionViewDidEnter() {
    console.log("ionViewDidEnter SeminarListPage")
    this.getSeminars()
  }

  logOut() {
  	this.navCtrl.setRoot(LoginPage)
  }

  openPage(page, params?: {}) {
  	this.navCtrl.push(page, params)
  }

  private getSeminars() {
    this.http.get("http://207.38.82.139:8001/seminar")
                .map(res => res.json())
                .subscribe(
                  res => {
                    this.seminars = res.data.sort((x, y) => {
                        if (x.name.toLowerCase() > y.name.toLowerCase()) return 1
                        if (x.name.toLowerCase() < y.name.toLowerCase()) return -1
                        return 0
                      })
                    this.filteredSeminars = this.seminars.slice()
                  },
                  error => {
                    this.seminars = []
                    this.filteredSeminars = []
                    console.log(error)
                  })
  }

  getFilteredSeminars(event: any) {
    this.filteredSeminars = this.seminars.slice()
    let query = event.target.value;
    if (query && query.trim() != '') {
      this.filteredSeminars = this.filteredSeminars.filter((seminar) => {
        return (seminar.name.toLowerCase().indexOf(query.toLowerCase()) > -1)
      });
    }
  }

}
