import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

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

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  	this.seminars = []
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad SeminarListPage');
  }

}
