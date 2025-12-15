import { NativeDateAdapter } from '@angular/material/core';

export class BRDateAdapter extends NativeDateAdapter {
  override format(date: Date, displayFormat: any): string {
    if (!date) return '';
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  override parse(value: any): Date | null {
    if (typeof value === 'string') {
      const parts = value.split('/');
      if (parts.length === 3) {
        const day = parseInt(parts[0], 10);
        const month = parseInt(parts[1], 10) - 1; // JS months are 0-indexed
        const year = parseInt(parts[2], 10);
        if (day && month >= 0 && month < 12 && year) {
          return new Date(year, month, day);
        }
      }
    }
    return super.parse(value);
  }
}