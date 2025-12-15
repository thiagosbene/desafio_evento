import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DateAdapter, MatNativeDateModule } from '@angular/material/core';
import { EventoService } from '../../../core/services/evento.service';
import { CreateEventoDto, UpdateEventoDto } from '../models/evento.model';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BRDateAdapter } from '../../../shared/components/br-date-adapter';

@Component({
  selector: 'app-evento-form',
  templateUrl: './evento-form.component.html',
  styleUrls: ['./evento-form.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule
  ],
  providers: [
    { provide: DateAdapter, useClass: BRDateAdapter }
  ]
})
export class EventoFormComponent implements OnInit {
  form: FormGroup;
  isEdit = false;
  eventoId: number | null = null;

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private eventoService = inject(EventoService);
  private snackBar = inject(MatSnackBar);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.eventoId = +id;
      this.eventoService.getById(this.eventoId).subscribe(evento => {
        // Extrair data e hora do ISO string
        const isoDate = new Date(evento.dataEvento);
        const formattedDate = new Date(
          isoDate.getFullYear(),
          isoDate.getMonth(),
          isoDate.getDate()
        );
        const formattedTime = `${isoDate.getHours().toString().padStart(2, '0')}:${isoDate.getMinutes().toString().padStart(2, '0')}`;

        this.form.patchValue({
          titulo: evento.titulo,
          descricao: evento.descricao,
          local: evento.local,
          data: formattedDate,
          hora: formattedTime
        });
      });
    }
  }

  constructor() {
    this.form = this.fb.group({
      titulo: ['', [Validators.required, Validators.maxLength(100)]],
      data: [null, Validators.required],
      hora: ['', [Validators.required, Validators.pattern(/^([01]?[0-9]|2[0-3]):[0-5][0-9]$/)]],
      descricao: ['', Validators.maxLength(1000)],
      local: ['', Validators.maxLength(200)]
    });
  }

 onSubmit() {
  if (this.form.invalid) return;

  const { titulo, data, hora, descricao, local } = this.form.getRawValue();

  // Validar data e hora no frontend
  if (!(data instanceof Date)) {
    this.snackBar.open('Data inválida.', 'Fechar', { duration: 3000 });
    return;
  }

  const timeParts = hora.split(':');
  const hours = parseInt(timeParts[0], 10);
  const minutes = parseInt(timeParts[1], 10);

  if (isNaN(hours) || isNaN(minutes) || hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
    this.snackBar.open('Hora inválida.', 'Fechar', { duration: 3000 });
    return;
  }

  // Converter para string ISO (sem fuso)
  const fullDate = new Date(data.getFullYear(), data.getMonth(), data.getDate(), hours, minutes);
  const dataEventoISO = fullDate.toISOString().slice(0, 19); // "YYYY-MM-DDTHH:mm:ss"

  const dto: CreateEventoDto | UpdateEventoDto = {
    titulo,
    descricao,
    dataEvento: fullDate,
    local
    };

    const request$ = this.isEdit && this.eventoId
      ? this.eventoService.update(this.eventoId, dto)
      : this.eventoService.create(dto);

    request$.subscribe({
      next: () => {
        const msg = this.isEdit ? 'Evento atualizado com sucesso!' : 'Evento criado com sucesso!';
        this.snackBar.open(msg, 'Fechar', { duration: 3000 });
        this.router.navigate(['/events']);
      },
      error: (err) => {
        console.error('Erro ao salvar evento:', err);

        let errorMessage = 'Erro ao salvar evento. Tente novamente.';
        
        if (err?.error?.message && err?.status != 400 ) {
          errorMessage = err.error.message;
        } else if (err?.error?.error) {
          errorMessage = err.error.error;
        } else if (err?.status === 400 && err?.error) {
          // Se for resposta de validação do Spring
          try {
            const errors = err.error;
            if (err.error.errors.length > 0.) {
              errorMessage = err.error.errors[0];
            } else if (typeof errors === 'object') {
              errorMessage = Object.values(errors)[0] as string;
            } else {
              errorMessage = 'Dados inválidos.';
            }
          } catch (e) {            
          }
        }

        // Exibir no snackbar
        this.snackBar.open(errorMessage, 'Fechar', {
          duration: 5000,
          panelClass: ['error-snackbar']
        });

        // Opcional: definir erro no campo 'data' se for relacionado a data
        if (errorMessage.toLowerCase().includes('data') || errorMessage.toLowerCase().includes('date')) {
          this.form.get('data')?.setErrors({ backend: errorMessage });
        }
      }
    });
  }
}