import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EventoService } from '../../../core/services/evento.service';
import { EventoDto } from '../models/evento.model';
import { ConfirmDeleteComponent } from '../../../shared/components/confirm-delete/confirm-delete.component';

@Component({
  selector: 'app-evento-detail',
  templateUrl: './evento-detail.component.html',
  styleUrls: ['./evento-detail.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ]
})
export class EventoDetailComponent implements OnInit {
  evento: EventoDto | null = null;
  loading = true;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private eventoService = inject(EventoService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventoService.getById(+id).subscribe({
        next: (evento) => {
          this.evento = evento;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
    }
  }

  openConfirmDelete() {
    if (!this.evento) return;

    const dialogRef = this.dialog.open(ConfirmDeleteComponent, {
      data: { titulo: this.evento.titulo }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteEvento();
      }
    });
  }

  deleteEvento() {
    if (!this.evento) return;

    this.eventoService.delete(this.evento.id).subscribe({
      next: () => {
        this.snackBar.open('Evento excluído com sucesso!', 'Fechar', { duration: 3000 });
        this.router.navigate(['/events']);
      },
      error: () => {
        this.snackBar.open('Erro ao excluir evento.', 'Fechar', { duration: 3000 });
      }
    });
  }
}