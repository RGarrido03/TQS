import { Bus, BusReference } from "./bus";
import { City, CityReference } from "./city";
import { CurrencyParams } from "./currency";

export type Trip = {
  id: number;
  bus: Bus;
  departure: City;
  departureTime: Date;
  arrival: City;
  arrivalTime: Date;
  price: number;
  freeSeats: number;
};

export type TripCreate = Omit<Trip, "id"> & {
  bus: BusReference;
  departure: CityReference;
  arrival: CityReference;
};

export type TripReference = Pick<Trip, "id">;

export type TripSearchParameters = CurrencyParams & {
  departure?: number;
  arrival?: number;
  departureTime?: string;
  seats?: number;
};
