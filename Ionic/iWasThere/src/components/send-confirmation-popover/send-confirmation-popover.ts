import { Component } from '@angular/core';
import { NavController, ToastController, NavParams } from 'ionic-angular';
import { BarcodeScanner } from '@ionic-native/barcode-scanner';

import { UserSingleton } from '../../util/user-singleton.ts'

import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
/**
 * Generated class for the SendConfirmationPopover component.
 *
 * See https://angular.io/docs/ts/latest/api/core/index/ComponentMetadata-class.html
 * for more info on Angular Components.
 */
@Component({
  selector: 'send-confirmation-popover',
  templateUrl: 'send-confirmation-popover.html'
})
export class SendConfirmationPopover {

  user: any

  constructor(public navCtrl: NavController, public navParams: NavParams, private toastCtrl: ToastController, private http: Http) {
    console.log('Hello SendConfirmationPopover Component');
    this.user = new UserSingleton()
  }

  openPage(page) {
  	this.navCtrl.push(page)
  }

  qrCode() {
    var barcodeScanner = new BarcodeScanner()
    barcodeScanner.scan()
          .then((barcodeData) => {
            if (barcodeData.text != this.navParams.get("seminar_id")) {
              let toast = this.toastCtrl.create({
                  message: "This QR code doesn't belong to this seminar (" + barcodeData.text + " != " + this.navParams.get("seminar_id") + ")",
                  duration: 3000,
                  position: 'middle'
                });

                toast.onDidDismiss(() => {
                  console.log('Dismissed toast');
                });

                toast.present();
            }
            else {
              var url = "http://207.38.82.139:8001/attendence/submit"
              let body = new FormData()
              body.append('nusp', this.user.getInstance().nusp)
              body.append('seminar_id', barcodeData.text)
              this.http.post(url, body)
                        .map(res => res.json())
                        .subscribe(
                          res => {
                            console.log(res)
                            if (res.success) {
                              let toast = this.toastCtrl.create({
                                message: "Your attendance was successfully confirmed",
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
                                message: "Something went wrong. Please try again later." + barcodeData.text,
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

           

            

            console.log(barcodeData.text)
          }, (err) => {
            console.log("Deu ruim")
          });
  }

  bluetooth() {

  }

}
