import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, LoadingController } from 'ionic-angular';

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

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

  nusps: any
  attendees: any
  filteredAttendees: any
  seminar: any
  loading: any

  constructor(public navCtrl: NavController, public navParams: NavParams, private http: Http, private loadingCtrl: LoadingController) {
    this.attendees = []
    this.filteredAttendees = []
    this.seminar = navParams.get("seminar")
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad StudentListPage');
  }

  ionViewDidEnter() {
    this.filteredAttendees = []
    this.getNusps()
  }

  private getNusps() {
    let body = new FormData()
    body.append('seminar_id', this.seminar.id)
    let url = "http://207.38.82.139:8001/attendence/listStudents"
    this.loading = this.loadingCtrl.create({
        content: 'Please wait...'
    });
    this.loading.present();

    this.http.post(url, body)
              .map(res => res.json())
              .subscribe(
                res => {
                  console.log(res.data)
                  this.nusps = res.data
                },
                error => {
                  this.nusps = []
                  console.log(error)
                },
                () => {
                  this.getAttendees()
                })
  }

  private getAttendees() {
    return new Promise((resolve, reject) => {
    this.attendees = []
    this.filteredAttendees = []
    if (this.nusps == null || this.nusps.length < 1) this.loading.dismiss();
    var count = 0;
    for (let nusp in this.nusps) {
      let url = "http://207.38.82.139:8001/student/get/" + nusp
      this.http.get(url)
            .map(res => res.json())
                  .subscribe(
                    res => {
                      if (res.data != null)
                        this.attendees.push(res.data)
                    },
                    error => {
                      console.log(error)
                    },
                    () => {
                      count += 1
                      if (count == this.nusps.length) {
                        this.attendees = this.attendees.sort((x, y) => {
                        if (x.name.toLowerCase() > y.name.toLowerCase()) return 1
                        if (x.name.toLowerCase() < y.name.toLowerCase()) return -1
                        return 0
                      })
                      this.filteredAttendees = this.attendees.slice()
                      this.loading.dismiss();
                      }
                    })
    }
    })

    
  }

  getFilteredAttendees(event: any) {
    this.filteredAttendees = this.attendees.slice()
    let query = event.target.value;
    if (query && query.trim() != '') {
      this.filteredAttendees = this.filteredAttendees.filter((attendee) => {
        return (attendee.name.toLowerCase().indexOf(query.toLowerCase()) > -1)
      });
    }
  }
}
