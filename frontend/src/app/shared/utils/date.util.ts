export function parseBrDateTime(dateTimeStr: string): Date | null {
  if (!dateTimeStr || typeof dateTimeStr !== 'string') {
    return null;
  }

  const clean = dateTimeStr.trim();
  const regex = /^(\d{2})\/(\d{2})\/(\d{4})\s+(\d{2}):(\d{2})(?::(\d{2}))?$/;
  const match = clean.match(regex);

  if (!match) return null;

  const [, day, month, year, hour, minute, second = '0'] = match;
  const date = new Date(
    parseInt(year, 10),
    parseInt(month, 10) - 1,
    parseInt(day, 10),
    parseInt(hour, 10),
    parseInt(minute, 10),
    parseInt(second, 10)
  );

  return isNaN(date.getTime()) ? null : date;
}