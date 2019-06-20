import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {BoardsComponent} from './boards/boards.component';
import {BoardsService} from "./services/boards.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {NewBoardModalComponent} from './boards/new-board-modal/new-board-modal.component';
import {
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatDialogModule,
  MatDividerModule,
  MatFormFieldModule,
  MatIconModule,
  MatIconRegistry,
  MatInputModule,
  MatListModule,
  MatMenuModule,
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
import {CardFormSimpleComponent} from './boards/board/card-form-simple/card-form-simple.component';
import {ColumnFormSimpleComponent} from './boards/board/column-form-simple/column-form-simple.component';
import {RegistrationComponent} from './registration/registration.component';
import {LoginComponent} from './login/login.component';
import {AuthInterceptor} from "./auth-interceptor.service";

@NgModule({
  declarations: [
    AppComponent,
    BoardsComponent,
    NewBoardModalComponent,
    BoardComponent,
    NotFoundComponent,
    ColumnComponent,
    CardFormComponent,
    CardFormSimpleComponent,
    ColumnFormSimpleComponent,
    RegistrationComponent,
    LoginComponent
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
    MatMenuModule,
    DragDropModule
  ],
  entryComponents: [
    NewBoardModalComponent
  ],
  providers: [
    BoardsService,
    MatIconRegistry, {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
