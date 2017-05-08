import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, LoadingController, PopoverController } from 'ionic-angular';

import { SendConfirmationPopover } from '../../components/send-confirmation-popover/send-confirmation-popover'

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

  studentList: any
  attendees: any
  filteredAttendees: any
  seminar: any

  constructor(public navCtrl: NavController, public navParams: NavParams, private http: Http, private loadingCtrl: LoadingController, private popoverCtrl: PopoverController) {
    this.attendees = []
    this.filteredAttendees = []
    this.seminar = navParams.get("seminar")
    this.getNusps()
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad StudentListPage');
  }

  private getNusps() {
    let body = new FormData()
    body.append('seminar_id', this.seminar.id)
    let url = "http://207.38.82.139:8001/attendence/listStudents"
    this.http.post(url, body)
              .map(res => res.json())
              .subscribe(
                res => {
                  this.studentList = res.data
                },
                error => {
                  this.studentList = []
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
      if (this.studentList != null && this.studentList != undefined) {
        var count = 0;
        for (let i = 0; i < this.studentList.length; i++) {
          let s = this.studentList[i]
          let url = "http://207.38.82.139:8001/student/get/" + s.student_nusp
          this.http.get(url)
                .map(res => res.json())
                      .subscribe(
                        res => {
                          if (res.data != null) {
                            console.log("User foi:" + res.data)
                            this.attendees.push(res.data)
                          }
                        },
                        error => {
                          console.log(error)
                        },
                        () => {
                          count += 1
                          if (count == this.studentList.length) {
                            this.attendees = this.attendees.sort((x, y) => {
                            if (x.name.toLowerCase() > y.name.toLowerCase()) return 1
                            if (x.name.toLowerCase() < y.name.toLowerCase()) return -1
                            return 0
                          })
                          this.filteredAttendees = this.attendees.slice()
                          }
                        })
        }
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

  iWasThere(event: Event) {
    let popover = this.popoverCtrl.create(SendConfirmationPopover, {seminar_id: this.seminar.id});
    popover.present({ev: event});
  }

  doRefresh(refresher) {
    this.getNusps()

    setTimeout(() => {
      console.log('Async operation has ended');
      refresher.complete();
    }, 2000);
  }
}
