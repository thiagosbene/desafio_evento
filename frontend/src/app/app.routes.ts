import { Routes } from '@angular/router';
import { EventosListComponent } from './features/eventos/eventos-list/eventos-list.component';
import { EventoFormComponent } from './features/eventos/evento-form/evento-form.component';
import { EventoDetailComponent } from './features/eventos/evento-detail/evento-detail.component';



export const routes: Routes = [
  { path: '', redirectTo: '/events', pathMatch: 'full' },
  { path: 'events', component: EventosListComponent },
  { path: 'events/new', component: EventoFormComponent },
  { path: 'events/:id/edit', component: EventoFormComponent },
  { path: 'events/:id', component: EventoDetailComponent },
];