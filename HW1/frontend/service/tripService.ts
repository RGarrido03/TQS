import { Trip, TripCreate, TripSearchParameters } from "@/types/trip";
import { BASE_API_URL } from "./config";
import { CurrencyParams } from "@/types/currency";
import { Reservation } from "@/types/reservation";

export const createTrip = async (trip: TripCreate): Promise<Trip> =>
  fetch(BASE_API_URL + "trip", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(trip),
  }).then((res) => res.json());

export const getTrips = async (
  params?: TripSearchParameters
): Promise<Trip[]> => {
  const res = await fetch(
    BASE_API_URL +
      "trip?" +
      new URLSearchParams(params as Record<string, string>),
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  const data = await res.json();
  data.forEach((trip: Trip) => {
    trip.departureTime = new Date(trip.departureTime);
    trip.arrivalTime = new Date(trip.arrivalTime);
  });
  return data;
};

export const getTrip = async (
  id: number,
  params?: CurrencyParams
): Promise<Trip> => {
  const res = await fetch(
    BASE_API_URL +
      "trip/" +
      id +
      "?" +
      new URLSearchParams(params as Record<string, string>),
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  const data = await res.json();
  data.departureTime = new Date(data.departureTime);
  data.arrivalTime = new Date(data.arrivalTime);
  return data;
};

export const getTripReservations = async (
  id: number,
  params?: CurrencyParams
): Promise<Reservation[]> =>
  fetch(
    BASE_API_URL +
      "trip/" +
      id +
      "/reservations?" +
      new URLSearchParams(params as Record<string, string>),
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  ).then((res) => res.json());

export const updateTrip = async (id: number, trip: TripCreate): Promise<Trip> =>
  fetch(BASE_API_URL + "trip/" + id, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(trip),
  }).then((res) => res.json());

export const deleteTrip = async (id: number): Promise<any> =>
  fetch(BASE_API_URL + "trip/" + id, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => res.json());
