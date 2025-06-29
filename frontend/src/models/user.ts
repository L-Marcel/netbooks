import { UUID } from "crypto";

export enum Role {
  ADMINISTRATOR = "ADMINISTRATOR",
  SUBSCRIBER = "SUBSCRIBER",
  UNKNOWN = "UNKNOWN",
}

export interface UserData {
  uuid: UUID;
  name: string;
  email: string;
  roles: Role[];
  automaticBilling: boolean;
}

export class User {
  readonly uuid: UUID;
  readonly name: string;
  readonly email: string;
  readonly roles: Role[];
  readonly avatar: string;
  readonly automaticBilling: boolean;

  constructor(data: UserData) {
    this.uuid = data.uuid;
    this.name = data.name;
    this.email = data.email;
    this.roles = data.roles;
    this.avatar = `http://localhost:8080/users/${this.uuid}.webp`;
    this.automaticBilling = data.automaticBilling;
  }

  isSubscriber(): boolean {
    return this.roles.includes(Role.SUBSCRIBER);
  }

  isAdmin(): boolean {
    return this.roles.includes(Role.ADMINISTRATOR);
  }

  getInitials(): string {
    if (!this.name) return "?";

    const parts = this.name.trim().split(" ");
    if (parts.length === 1 && parts[0].length > 0) {
      return parts[0][0].toUpperCase();
    } else if (parts.length > 1) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }

    return "?";
  }
}
