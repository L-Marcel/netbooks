export interface ClassificationData {
  value: number;
}

export class Classification {
  readonly value: number;

  constructor(data: ClassificationData) {
    this.value = data.value;
  }
}
