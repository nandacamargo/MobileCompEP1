import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, AlertController } from 'ionic-angular';
import { LoginPage } from '../login-page/login-page';

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

import { UserSingleton } from '../../util/user-singleton.ts'

/**
 * Generated class for the EditProfilePage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-edit-profile-page',
  templateUrl: 'edit-profile-page.html',
})
export class EditProfilePage {

  missingFields: boolean;
  passwordMismatch: boolean;
  name: string;
  password: string;
  confirmPassword: string;
  user: any;

  constructor(public navCtrl: NavController, public navParams: NavParams, private http: Http, private alertCtrl: AlertController) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad EditProfilePage')
    this.missingFields = false
    this.passwordMismatch = false
    this.user = new UserSingleton()
    this.name = this.user.getInstance().name
  }

  saveChanges() {
    if (!this.name || !this.password || !this.confirmPassword) {
      this.missingFields = true
      return false
    }
    this.missingFields = false

    if (this.password != this.confirmPassword) {
      this.passwordMismatch = true
      return false
    }
    this.passwordMismatch = false

    var url;
    if (this.user.getInstance().teacher) url = "http://207.38.82.139:8001/teacher/edit"
    else url = "http://207.38.82.139:8001/student/edit"
    let body = new FormData()
    body.append('name', this.name)
    body.append('nusp', this.user.getInstance().nusp)
    body.append('pass', this.password)
    this.http.post(url, body)
              .map(res => res.json())
              .subscribe(
                res => {
                  console.log(res)
                  if (res.success) this.navCtrl.pop()
                }, 
                error => console.log(error)
              )
  }

  deleteAccount() {
    let alert = this.alertCtrl.create({
      title: 'Delete account',
      message: 'Are you sure you want to delete your account?',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: () => {
            console.log('Cancel clicked');
          }
        },
        {
          text: 'Delete',
          handler: () => {
            var url;
            if (this.user.getInstance().teacher) url = "http://207.38.82.139:8001/teacher/delete"
            else url = "http://207.38.82.139:8001/student/delete"
            let body = new FormData()
            body.append('nusp', this.user.getInstance().nusp)
            this.http.post(url, body)
                      .map(res => res.json())
                      .subscribe(
                        res => {
                          console.log(res)
                          if (res.success) this.navCtrl.setRoot(LoginPage)
                        }, 
                        error => console.log(error)
                      )
          }
        }
      ]
    });
    alert.present();
  }

}
