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
  MatCardModule,
  MatCheckboxModule,
  MatDialogModule,
  MatDividerModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatProgressSpinnerModule,
  MatSelectModule,
  MatSidenavModule,
  MatToolbarModule
} from "@angular/material";
import {FormsModule} from "@angular/forms";
import {BoardComponent} from './boards/board/board.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {ColumnComponent} from './boards/board/column/column.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {CardFormComponent} from './boards/board/card-form/card-form.component';

@NgModule({
  declarations: [
    AppComponent,
    BoardsComponent,
    NewBoardModalComponent,
    BoardComponent,
    NotFoundComponent,
    ColumnComponent,
    CardFormComponent
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
    MatCardModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatSelectModule,
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
