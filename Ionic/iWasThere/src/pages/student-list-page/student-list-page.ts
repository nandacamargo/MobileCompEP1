import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

/**
 * Generated class for the StudentListPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-student-list-page',
  templateUrl: 'student-list-page.html',
})
export class StudentListPage {

  students: any

  constructor(public navCtrl: NavController, public navParams: NavParams) {
  	this.students = ["João"]
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad StudentListPage');
  }

}
