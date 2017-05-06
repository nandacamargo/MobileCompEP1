import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/**
 * Generated class for the AddSeminarPage page.
 *
 * See http://ionicframework.com/docs/components/#navigation for more info
 * on Ionic pages and navigation.
 */
@IonicPage()
@Component({
  selector: 'page-add-seminar-page',
  templateUrl: 'add-seminar-page.html',
})
export class AddSeminarPage {

  missingFields: boolean;
  name: string;

  constructor(public navCtrl: NavController, public navParams: NavParams, private http: Http) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad AddSeminarPage');
  }

  addSeminar() {
    if (!this.name) {
      this.missingFields = true
      return false
    }
    var url = "http://207.38.82.139:8001/seminar/add"
    let body = new FormData()
    body.append('name', this.name)
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

}
