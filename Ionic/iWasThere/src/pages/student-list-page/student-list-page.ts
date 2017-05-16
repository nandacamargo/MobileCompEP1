import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, LoadingController, PopoverController, ToastController } from 'ionic-angular';

import { SendConfirmationPopover } from '../../components/send-confirmation-popover/send-confirmation-popover'

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

import { UserSingleton } from '../../util/user-singleton.ts'

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
  confirmed: number
  searchQuery: string
  title: string
  user: any
  alreadyConfirmed: boolean

  constructor(public navCtrl: NavController, 
    public navParams: NavParams, 
    private http: Http, 
    private loadingCtrl: LoadingController, 
    private popoverCtrl: PopoverController, 
    private toastCtrl: ToastController) {
      this.alreadyConfirmed = false
      this.attendees = []
      this.filteredAttendees = []
      this.user = new UserSingleton().getInstance()
      this.seminar = navParams.get("seminar")
      this.confirmed = navParams.get("confirmed")
      if (this.confirmed == 1) this.title = "Confirmed attendees of " + this.seminar.name
      else this.title = "Unconfirmed attendees of " + this.seminar.name
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
                  console.log(res.data)
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
      let skipped = 0
      if (this.studentList != null && this.studentList != undefined) {
        var count = 0;
        for (let i = 0; i < this.studentList.length; i++) {
          let s = this.studentList[i]
          if (s.confirmed != this.confirmed) {
            skipped++
            continue
          }
          if (s.student_nusp == this.user.nusp) this.alreadyConfirmed = true
          let url = "http://207.38.82.139:8001/student/get/" + s.student_nusp
          this.http.get(url)
                .map(res => res.json())
                      .subscribe(
                        res => {
                          if (res.data != null) {
                            console.log(res)
                            this.attendees.push(res.data)
                          }
                        },
                        error => {
                          console.log(error)
                        },
                        () => {
                          count += 1
                          if (count == this.studentList.length - skipped) {
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
      this.filteredAttendees = this.attendees.slice()
      refresher.complete();
    }, 2000);
  }

  confirmSelected() {
    this.searchQuery = ""
    for (let i = 0; i < this.attendees.length; i++) {
      let s = this.attendees[i]
      if (s.data) {
        var url = "http://207.38.82.139:8001/attendence/submit"
        let body = new FormData()
        body.append('nusp', s.nusp)
        body.append('seminar_id', this.seminar.id)
        body.append('confirmed', 1)
        this.http.post(url, body)
                  .map(res => res.json())
                  .subscribe(
                    res => {
                      console.log(res)
                      if (res.success) {
                        this.attendees.splice(i, 1)
                        this.filteredAttendees = this.attendees.slice()
                        let toast = this.toastCtrl.create({
                          message: "Confirmation request successfully sent",
                          duration: 3000,
                          position: 'middle'
                        });

                        toast.onDidDismiss(() => {
                          console.log('Dismissed toast');
                        });

                        toast.present();
                      }
                      else {
                        let toast = this.toastCtrl.create({
                          message: "Something went wrong. Please try again later.",
                          duration: 3000,
                          position: 'middle'
                        });

                        toast.onDidDismiss(() => {
                          console.log('Dismissed toast');
                        });

                        toast.present();
                      }
                    }, 
                    error => console.log(error),
                  )
      }
    }
  }
}
