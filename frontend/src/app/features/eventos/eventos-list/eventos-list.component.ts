import { Component, AfterViewInit, ViewChild, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatTooltipModule } from '@angular/material/tooltip';
import { startWith, switchMap, catchError, of } from 'rxjs';
import { EventoService } from '../../../core/services/evento.service';
import { EventoDto } from '../models/evento.model';
import { Page } from '../models/page.model';
import { parseBrDateTime } from '../../../shared/utils/date.util';

@Component({
  selector: 'app-eventos-list',
  templateUrl: './eventos-list.component.html',
  styleUrls: ['./eventos-list.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatTooltipModule
  ]
})
export class EventosListComponent implements AfterViewInit {
  dataSource: EventoDto[] = [];
  totalElements = 0;

  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;

  private eventoService = inject(EventoService);

  ngAfterViewInit() {
    setTimeout(() => {
      if (!this.paginator) {
        console.error('[EventosList] Paginator não encontrado no DOM');
        return;
      }

      console.log('[EventosList] Paginator encontrado, configurando stream...');

      this.paginator.page
        .pipe(
          startWith(null),
          switchMap(() =>
            this.eventoService.getAll(
              this.paginator.pageIndex,
              this.paginator.pageSize,
              'dataEvento,asc'
            )
          ),
          catchError(err => {
            console.error('Erro ao buscar eventos:', err);
            return of({
              content: [],
              totalElements: 0,
              totalPages: 0,
              size: this.paginator?.pageSize || 10,
              number: this.paginator?.pageIndex || 0
            } as Page<EventoDto>);
          })
        )
        .subscribe(page => {
          // ✅ Converte dataEvento para Date válido, com segurança
          this.dataSource = page.content.map(evento => {
            let dataEventoDate: Date;

            // Tenta parsear como string no formato brasileiro (dd/MM/yyyy HH:mm)
            if (typeof evento.dataEvento === 'string') {
              const parsed = parseBrDateTime(evento.dataEvento);
              if (parsed && !isNaN(parsed.getTime())) {
                dataEventoDate = parsed;
              } else {
                // Tenta como string ISO ou formato natural
                dataEventoDate = new Date(evento.dataEvento);
              }
            } else if (evento.dataEvento instanceof Date) {
              // Já é Date
              dataEventoDate = evento.dataEvento;
            } else {
              // Outros casos (null, undefined, etc.)
              dataEventoDate = new Date();
            }

            // Garante que é uma data válida
            if (isNaN(dataEventoDate.getTime())) {
              dataEventoDate = new Date();
            }

            return {
              ...evento,
              dataEvento: dataEventoDate
            };
          });

          this.totalElements = page.totalElements;
          this.paginator.length = page.totalElements;
        });
    }, 0);
  }
}