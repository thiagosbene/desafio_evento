import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { routes } from './app.routes';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { PtBrPaginatorIntl } from './shared/components/pt-br-paginator';
import { ConfirmDeleteComponent } from './shared/components/confirm-delete/confirm-delete.component';


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimations(),
    ConfirmDeleteComponent,
    provideHttpClient(withFetch()),
    { provide: MatPaginatorIntl, useClass: PtBrPaginatorIntl },
    
  ]
};