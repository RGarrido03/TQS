"use client";

import {
  Autocomplete,
  AutocompleteItem,
  Input,
  Skeleton,
} from "@nextui-org/react";
import { City } from "@/types/city";
import { useQuery } from "@tanstack/react-query";
import { getCities } from "@/service/cityService";
import { Trip } from "@/types/trip";
import { getTrips } from "@/service/tripService";
import TripCard from "@/components/TripCard";
import { useCookies } from "next-client-cookies";
import { useState } from "react";
import { Currency } from "@/types/currency";
import { Spinner } from "@nextui-org/react";

export default function Trips() {
  const cookies = useCookies();
  const currency: Currency = (cookies.get("currency") as Currency) || "EUR";

  const [departure, setDeparture] = useState<number>(
    parseInt(cookies.get("departure") || "0")
  );
  const [arrival, setArrival] = useState<number>(
    parseInt(cookies.get("arrival") || "0")
  );
  const [seats, setSeats] = useState<number>(
    parseInt(cookies.get("seats") || "0")
  );
  const [departureTime, setDepartureTime] = useState<string>("");

  const { isPending: isCitiesPending, data: cities } =
    useQuery<City[]>({
      queryKey: ["cities"],
      queryFn: () => getCities(),
    }) || [];

  const { isPending: isTripsPending, data: trips } =
    useQuery<Trip[]>({
      queryKey: [
        "trips",
        { departure, arrival, seats, departureTime, currency },
      ],
      queryFn: () =>
        getTrips({
          departure,
          arrival,
          seats,
          departureTime,
          currency,
        }),
    }) || [];

  return (
    <div className="flex flex-col gap-8">
      <div className="p-8 lg:p-24 flex flex-col items-center justify-center gap-8">
        <h1 className="font-bold text-4xl lg:text-6xl text-center text-balance">
          Choose a trip.
        </h1>
        <div className="flex flex-col lg:flex-row gap-4 items-center justify-center">
          <div className="flex flex-col md:flex-row gap-4">
            <Skeleton isLoaded={!isCitiesPending} className="rounded-lg">
              <Autocomplete
                label="Departure"
                id="departure"
                className="max-w-xs"
                defaultSelectedKey={departure}
                onSelectionChange={(value) => {
                  setDeparture(value ? parseInt(value.toString()) : 0);
                }}
              >
                {cities
                  ? cities.map((city: City) => (
                      <AutocompleteItem
                        key={city.id}
                        value={city.id}
                        id={"departure" + city.id}
                      >
                        {city.name}
                      </AutocompleteItem>
                    ))
                  : []}
              </Autocomplete>
            </Skeleton>
            <Skeleton isLoaded={!isCitiesPending} className="rounded-lg">
              <Autocomplete
                label="Arrival"
                id="arrival"
                className="max-w-xs"
                defaultSelectedKey={arrival}
                onSelectionChange={(value) => {
                  setArrival(value ? parseInt(value.toString()) : 0);
                }}
              >
                {cities
                  ? cities.map((city) => (
                      <AutocompleteItem
                        key={city.id}
                        value={city.id}
                        id={"arrival" + city.id}
                      >
                        {city.name}
                      </AutocompleteItem>
                    ))
                  : []}
              </Autocomplete>
            </Skeleton>
          </div>
          <div className="flex flex-col md:flex-row gap-4">
            <Input
              type="number"
              label="People"
              id="seatsInput"
              min={1}
              className="w-full lg:max-w-24"
              defaultValue={seats.toString()}
              onValueChange={(value) => {
                setSeats(value ? parseInt(value.toString()) : 0);
              }}
            />
            <Input
              type="date"
              label="Date"
              id="departureTimeInput"
              className="max-w-xs"
              onValueChange={(value) => {
                setDepartureTime(value);
              }}
            />
          </div>
        </div>
      </div>
      {isTripsPending ? (
        <div className="flex flex-col gap-4 items-center">
          <Spinner />
          <p>Loading trips...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 px-4 md:px-8 lg:px-16 gap-8">
          {trips?.map((trip) => (
            <TripCard key={trip.id} isLoaded={!isTripsPending} trip={trip} />
          ))}
        </div>
      )}
    </div>
  );
}
