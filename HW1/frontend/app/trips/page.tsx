"use client";

import { Autocomplete, AutocompleteItem, Input } from "@nextui-org/react";
import { City } from "@/types/city";
import { useQuery } from "@tanstack/react-query";
import { getCities } from "@/service/cityService";
import { Trip } from "@/types/trip";
import { getTrips } from "@/service/tripService";
import TripCard from "@/components/TripCard";
import { useCookies } from "next-client-cookies";
import { useState } from "react";

export default function Trips() {
  const cookies = useCookies();

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

  const cities =
    useQuery<City[]>({
      queryKey: ["cities"],
      queryFn: () => getCities(),
    }).data || [];

  const trips =
    useQuery<Trip[]>({
      queryKey: ["trips", { departure, arrival, seats, departureTime }],
      queryFn: () => getTrips({ departure, arrival, seats, departureTime }),
    }).data || [];

  return (
    <div className="flex flex-col gap-8">
      <div className="p-8 lg:p-24 flex flex-col items-center justify-center gap-8">
        <h1 className="font-bold text-4xl lg:text-6xl text-center text-balance">
          Choose a trip.
        </h1>
        <div className="flex flex-col lg:flex-row gap-4 items-center justify-center">
          <div className="flex flex-col md:flex-row gap-4">
            <Autocomplete
              label="Departure"
              className="max-w-xs"
              defaultSelectedKey={departure}
              onSelectionChange={(value) => {
                setDeparture(value ? parseInt(value.toString()) : 0);
              }}
            >
              {cities.map((city: City) => (
                <AutocompleteItem key={city.id} value={city.id}>
                  {city.name}
                </AutocompleteItem>
              ))}
            </Autocomplete>
            <Autocomplete
              label="Arrival"
              className="max-w-xs"
              defaultSelectedKey={arrival}
              onSelectionChange={(value) => {
                setArrival(value ? parseInt(value.toString()) : 0);
              }}
            >
              {cities.map((city) => (
                <AutocompleteItem key={city.id} value={city.id}>
                  {city.name}
                </AutocompleteItem>
              ))}
            </Autocomplete>
          </div>
          <div className="flex flex-col md:flex-row gap-4">
            <Input
              type="number"
              label="People"
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
              className="max-w-xs"
              onValueChange={(value) => {
                setDepartureTime(value);
              }}
            />
          </div>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 px-4 md:px-8 lg:px-16 gap-8">
        {trips.map((trip) => (
          <TripCard key={trip.id} trip={trip} />
        ))}
      </div>
    </div>
  );
}
