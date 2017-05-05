import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})

export class HomePage {

  posts: any

  constructor(public navCtrl: NavController, private http:Http) {

  }

  getUsers(event) {
    var url = "http://207.38.82.139:8001/student"
    this.http.get(url)
                .map(res => res.json())
                .subscribe(res => this.posts = res)
  }

}
