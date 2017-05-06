import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/**
 * Generated class for the RegisterPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-register-page',
  templateUrl: 'register-page.html',
})
export class RegisterPage {

  name: string
  nusp: string
  password: string
  confirmPassword: string
  teacher: boolean
  registerFailed: boolean
  missingFields: boolean
  passwordMismatch: boolean


  constructor(public navCtrl: NavController, public navParams: NavParams, private http: Http) {
    this.registerFailed = false
    this.missingFields = false
    this.passwordMismatch = false
    this.teacher = navParams.get('teacher')
    this.nusp = navParams.get('nusp')
    this.password = navParams.get('password')
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RegisterPage');
  }

  register() {
    if (!this.name || !this.nusp || !this.password || !this.confirmPassword) {
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
    if (this.teacher) url = "http://207.38.82.139:8001/teacher/add"
    else url = "http://207.38.82.139:8001/student/add"
    let body = new FormData()
    body.append('name', this.name)
    body.append('nusp', this.nusp)
    body.append('pass', this.password)
    this.http.post(url, body)
              .map(res => res.json())
              .subscribe(
                res => {
                  console.log(res)
                  if (res.success) this.navCtrl.pop()
                  else this.registerFailed = true
                }, 
                error => console.log(error)
              )
  }

}
