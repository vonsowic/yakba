import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {BoardsComponent} from './boards/boards.component';
import {BoardsService} from "./services/boards.service";
import {HttpClientModule} from "@angular/common/http";
import {NewBoardModalComponent} from './boards/new-board-modal/new-board-modal.component';
import {
  MatButtonModule,
  MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatToolbarModule
} from "@angular/material";
import {FormsModule} from "@angular/forms";
import {BoardComponent} from './boards/board/board.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {ColumnComponent} from './boards/board/column/column.component';
import {CardComponent} from './boards/board/column/card/card.component';
import {DragDropModule} from "@angular/cdk/drag-drop";

@NgModule({
  declarations: [
    AppComponent,
    BoardsComponent,
    NewBoardModalComponent,
    BoardComponent,
    NotFoundComponent,
    ColumnComponent,
    CardComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    MatListModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
    DragDropModule
  ],
  entryComponents: [
    NewBoardModalComponent
  ],
  providers: [BoardsService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
