export interface IAdmin {
  id?: number;
  username?: string;
  password?: string;
  name?: string;
  lastname?: string;
}

export class Admin implements IAdmin {
  constructor(public id?: number, public username?: string, public password?: string, public name?: string, public lastname?: string) {}
}

export function getAdminIdentifier(admin: IAdmin): number | undefined {
  return admin.id;
}
