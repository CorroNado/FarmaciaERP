export class AccessReport {
  constructor({ userId, userName, accessCount, lastAccess, history }) {
    this.userId      = userId;
    this.userName    = userName    ?? '';
    this.accessCount = accessCount ?? 0;
    this.lastAccess  = lastAccess  ?? null;
    this.history     = history     ?? [];
  }

  static fromApi(raw) {
    return new AccessReport({
      userId:      raw.user_id      ?? raw.userId,
      userName:    raw.user_name    ?? raw.userName,
      accessCount: raw.access_count ?? raw.accessCount,
      lastAccess:  raw.last_access  ?? raw.lastAccess,
      history:     raw.history      ?? [],
    });
  }
}