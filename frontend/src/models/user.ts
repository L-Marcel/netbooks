import { UUID } from "crypto";
import { Subscription } from "./subscription";
import { Benefit } from "./benefit";
import { Book } from "./book";

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
  readonly benefits: Benefit[];
  readonly subscription?: Subscription;

  constructor(
    data: UserData,
    benefits: Benefit[],
    subscription?: Subscription
  ) {
    this.uuid = data.uuid;
    this.name = data.name;
    this.email = data.email;
    this.roles = data.roles;
    this.avatar = `http://localhost:8080/users/${this.uuid}.webp`;
    this.automaticBilling = data.automaticBilling;
    this.benefits = benefits;
    this.subscription = subscription;
  }

  public isSubscriber(): boolean {
    return this.roles.includes(Role.SUBSCRIBER);
  }

  public isAdmin(): boolean {
    return this.roles.includes(Role.ADMINISTRATOR);
  }

  public getInitials(): string {
    if (!this.name) return "?";

    const parts = this.name.trim().split(" ");
    if (parts.length === 1 && parts[0].length > 0) {
      return parts[0][0].toUpperCase();
    } else if (parts.length > 1) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }

    return "?";
  }

  public canHaveFiveReadings(): boolean {
    return (
      this.isAdmin() ||
      this.benefits.some(
        (benefit) => benefit === Benefit.CAN_HAVE_FIVE_READINGS
      )
    );
  }

  public canDownloadBook(book: Book): boolean {
    return (
      this.isAdmin() ||
      (this.benefits.some(
        (benefit) => benefit === Benefit.CAN_DOWNLOAD_BOOKS
      ) &&
        book.requirements.every((requirement) =>
          this.benefits.includes(requirement)
        ))
    );
  }
}
