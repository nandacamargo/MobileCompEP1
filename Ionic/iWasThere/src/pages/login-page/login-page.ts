import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { RegisterPage } from '../register-page/register-page';
import { SeminarListPage } from '../seminar-list-page/seminar-list-page';

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

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

  logIn(){
  	this.navCtrl.setRoot(SeminarListPage)
  }

  register() {
    this.navCtrl.push(RegisterPage)
  }
}
