import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BoardsComponent} from "./boards/boards.component";
import {MatButtonModule, MatDialogModule, MatIconModule, MatListModule, MatToolbarModule} from "@angular/material";
import {BoardComponent} from "./boards/board/board.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {RegistrationComponent} from "./registration/registration.component";
import {LoginComponent} from "./login/login.component";

const routes: Routes = [{
  path: '', pathMatch: 'full', redirectTo: 'board'
}, {
  path: 'board', component: BoardsComponent
}, {
  path: 'login', component: LoginComponent
}, {
  path: 'register', component: RegistrationComponent
}, {
  path: 'board/:boardId', component: BoardComponent
}, {
  path: '**', component: NotFoundComponent
}];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    MatListModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
