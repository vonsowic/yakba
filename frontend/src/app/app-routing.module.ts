import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BoardsComponent} from "./boards/boards.component";
import {MatButtonModule, MatDialogModule, MatIconModule, MatListModule, MatToolbarModule} from "@angular/material";

const routes: Routes = [{
  path: 'boards', component: BoardsComponent
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
