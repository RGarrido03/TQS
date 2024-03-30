import { Trip, TripReference } from "./trip";
import { User, UserReference } from "./user";

export type Reservation = {
  id: number;
  seats: number;
  trip: Trip;
  user: User;
  price: number;
};

export type ReservationCreate = Omit<Reservation, "id"> & {
  trip: TripReference;
  user: UserReference;
};

export type ReservationReference = Pick<Reservation, "id">;
