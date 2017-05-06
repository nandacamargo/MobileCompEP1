import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { RegisterPage } from '../register-page/register-page';
import { SeminarListPage } from '../seminar-list-page/seminar-list-page';

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/**
 * Generated class for the LoginPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-login-page',
  templateUrl: 'login-page.html',
})

export class LoginPage {

  password: string
  nusp: string
  teacher: boolean
  loginFailed: boolean

  constructor(public navCtrl: NavController, public navParams: NavParams, private http: Http) {
    this.teacher = false;
    this.loginFailed = false;
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

  logIn(){
    var url;
    if (this.teacher) url = "http://207.38.82.139:8001/login/teacher"
    else url = "http://207.38.82.139:8001/login/student"
    let body = new FormData()
    body.append('nusp', this.nusp)
    body.append('pass', this.password)
    this.http.post(url, body)
              .map(res => res.json())
              .subscribe(
                res => {
                  console.log(res)
                  if (res.success) this.navCtrl.setRoot(SeminarListPage)
                  else this.loginFailed = true
                }, 
                error => console.log(error)
              )
  }

  register() {
    var params = {
      nusp: this.nusp,
      password: this.password,
      teacher: false
    }
    this.navCtrl.push(RegisterPage, params)
  }
}
