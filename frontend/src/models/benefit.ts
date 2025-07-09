export type BenefitData = {
  name: string;
};

export enum Benefit {
  CAN_READ_BASIC_BOOKS = "CAN_READ_BASIC_BOOKS",
  CAN_READ_ALL_BOOKS = "CAN_READ_ALL_BOOKS",
  CAN_EXPLORE_IN_GROUP = "CAN_EXPLORE_IN_GROUP",
  CAN_DOWNLOAD_BOOKS = "CAN_DOWNLOAD_BOOKS",
  CAN_HAVE_FIVE_READINGS = "CAN_HAVE_FIVE_READINGS",
}

export const benefitToText = {
  [Benefit.CAN_READ_BASIC_BOOKS]: "Acesso a maiora dos livos",
  [Benefit.CAN_READ_ALL_BOOKS]: "Acesso a todos os livros",
  [Benefit.CAN_EXPLORE_IN_GROUP]: "Pode explorar em grupo",
  [Benefit.CAN_DOWNLOAD_BOOKS]: "Pode baixar livros",
  [Benefit.CAN_HAVE_FIVE_READINGS]: "Até 5 leituras simultâneas por livro",
};

export type PlanBenefit = {
  value: Benefit;
  description: string;
  actived: boolean;
};
