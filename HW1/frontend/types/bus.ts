export type Bus = {
  id: number;
  capacity: number;
};

export type BusCreate = Omit<Bus, "id">;

export type BusReference = Pick<Bus, "id">;
