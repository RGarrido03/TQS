import { Bus, BusReference } from "./bus";
import { City, CityReference } from "./city";

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
