import { Component, OnInit } from '@angular/core';
import { ConfirmationService } from '../confirmation-dialog/confirmation.service';

@Component({
  selector: 'dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  title = 'Playlist Generator';

  constructor(private confirmationService: ConfirmationService) { }

  ngOnInit() {
  }

  public openConfirmationDialog() {
    this.confirmationService.confirm('Please confirm..', 'Do you really want to ... ?')
    .then((confirmed) => console.log('User confirmed:', confirmed))
    .catch(() => console.log('User dismissed the dialog (e.g., by using ESC, clicking the cross icon, or clicking outside the dialog)'));
  }

}
