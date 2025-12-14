export interface EventoDto {
  id: number;
  titulo: string;
  dataEvento: Date; 
  descricao: string;
  local: string;
}


export type CreateEventoDto = Omit<EventoDto, 'id'>;
export type UpdateEventoDto = CreateEventoDto;