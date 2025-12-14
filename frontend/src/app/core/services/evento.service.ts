import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../features/eventos/models/page.model';
import { EventoDto, CreateEventoDto, UpdateEventoDto } from '../../features/eventos/models/evento.model';

@Injectable({ providedIn: 'root' })
export class EventoService {
  private url = '/api/eventos';

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 10, sort = 'dataEvento,desc'): Observable<Page<EventoDto>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<EventoDto>>(this.url, { params });
  }

  getById(id: number): Observable<EventoDto> {
    return this.http.get<EventoDto>(`${this.url}/${id}`);
  }

  create(dto: CreateEventoDto): Observable<EventoDto> {
    return this.http.post<EventoDto>(this.url, dto);
  }

  update(id: number, dto: UpdateEventoDto): Observable<UpdateEventoDto> {
    return this.http.put<UpdateEventoDto>(`${this.url}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}