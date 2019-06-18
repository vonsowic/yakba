import {Component, OnInit} from '@angular/core';
import {ColumnService} from "../../../services/column.service";
import {ActivatedRoute} from "@angular/router";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-column-form-simple',
  templateUrl: './column-form-simple.component.html',
  styleUrls: ['./column-form-simple.component.scss']
})
export class ColumnFormSimpleComponent implements OnInit {

  public columnName: string = '';

  constructor(
    private route: ActivatedRoute,
    private columnService: ColumnService
  ) {
  }

  ngOnInit() {
  }

  addColumn() {
    this.route.params
      .pipe(map(params => params['boardId']))
      .subscribe(boardId => {
        this.columnService.addColumn(boardId, this.columnName)
          .subscribe(res => {
            this.columnName = '';
          }, () => {

          });
      })
  }
}
