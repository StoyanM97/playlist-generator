import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConfirmationService } from '../confirmation-dialog/confirmation.service';

@Component({
  selector: 'not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.scss']
})
export class NotFoundComponent implements OnInit {
  
  defaultMessage: string;

  constructor(private route: ActivatedRoute, private confirmationService: ConfirmationService) {
    this.defaultMessage = "The page you are looking for was not found!";
   }

  ngOnInit() {}

  openConfirmationDialog() {
    this.confirmationService.confirm('Please confirm..', 'Do you really want to ... ?')
    .then((confirmed) => console.log('User confirmed:', confirmed))
    .catch(() => console.log('User dismissed the dialog (e.g., by using ESC, clicking the cross icon, or clicking outside the dialog)'));
  }

}
