"use client";

import {
  Autocomplete,
  AutocompleteItem,
  Button,
  Input,
} from "@nextui-org/react";
import { ChangeEvent, createContext, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { City } from "@/types/city";
import { getCities } from "@/service/cityService";
import Link from "next/link";

type a = {
  departure: number;
  setDeparture: (id: number) => void;
  arrival: number;
  seats: number;
  departureTime: string;
};

export const TripSearchContext = createContext<a>({
  departure: 0,
  setDeparture: () => {},
  arrival: 0,
  seats: 0,
  departureTime: "",
});

export default function Form() {
  const [departure, setDeparture] = useState<number>(0);
  const [arrival, setArrival] = useState<number>(0);
  const [seats, setSeats] = useState<number>(1);

  const cities =
    useQuery<City[]>({
      queryKey: ["cities"],
      queryFn: () => getCities(),
    }).data || [];

  return (
    <div className="flex flex-col lg:flex-row gap-4 items-center justify-center">
      <Autocomplete
        label="Departure"
        className="max-w-xs"
        onSelectionChange={(id) =>
          setDeparture(id ? parseInt(id.toString()) : 0)
        }
      >
        {cities.map((city: City) => (
          <AutocompleteItem key={city.id} value={city.name}>
            {city.name}
          </AutocompleteItem>
        ))}
      </Autocomplete>
      <Autocomplete
        label="Arrival"
        className="max-w-xs"
        onSelectionChange={(id) => setArrival(id ? parseInt(id.toString()) : 0)}
      >
        {cities.map((city) => (
          <AutocompleteItem key={city.id} value={city.name}>
            {city.name}
          </AutocompleteItem>
        ))}
      </Autocomplete>
      <Input
        type="number"
        label="People"
        min={0}
        defaultValue="1"
        className="lg:max-w-24"
        onChange={(event: ChangeEvent<HTMLInputElement>) =>
          setSeats(parseInt(event.target.value))
        }
      />
      <Link
        href={{
          pathname: "/trips",
          query: {
            departure,
            arrival,
            seats,
          },
        }}
        as={{
          pathname: "/trips",
          query: {
            departure,
            arrival,
            seats,
          },
        }}
      >
        <Button color="primary" type="submit">
          Search
        </Button>
      </Link>
    </div>
  );
}
