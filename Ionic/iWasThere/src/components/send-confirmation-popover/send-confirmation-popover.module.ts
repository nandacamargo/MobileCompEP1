import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SendConfirmationPopover } from './send-confirmation-popover';

@NgModule({
  declarations: [
    SendConfirmationPopover,
  ],
  imports: [
    IonicPageModule.forChild(SendConfirmationPopover),
  ],
  exports: [
    SendConfirmationPopover
  ]
})
export class SendConfirmationPopoverModule {}
